import pygame
import sys
from classes.player import Player
from classes.level import Level
from classes.camera import Camera

# Initialize Pygame
pygame.init()
WIDTH, HEIGHT = 800, 600
screen = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF)
pygame.display.set_caption("Geometry Dash")
clock = pygame.time.Clock()

# Create the level object
level = Level(level_file="level2.csv", screen_width=WIDTH, screen_height=HEIGHT)

# Use the player's starting position from the level if defined, else use a default value.
if level.player_start is not None:
    player = Player(level.player_start[0], level.player_start[1])
else:
    player = Player(100, 500)

# Sprite groups
all_sprites = pygame.sprite.Group()
all_sprites.add(player)
all_sprites.add(level.blocks)
all_sprites.add(level.spikes)

# Camera with margins
camera = Camera(WIDTH, HEIGHT)

# Game loop
running = True
while running:
    dt = clock.tick(60) / 1000  # Delta time in seconds

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_SPACE:
                player.jump()

    # Update game objects
    player.update(dt, level.scroll_speed)
    camera.scroll_x += level.scroll_speed * dt * 60
    camera.update(player.rect)

    # Collision with blocks
    block_collisions = pygame.sprite.spritecollide(player, level.blocks, False)
    if block_collisions:
        player.rect.bottom = block_collisions[0].rect.top
        player.velocity_y = 0

    # Collision with spikes
    if pygame.sprite.spritecollide(player, level.spikes, False):
        print("Game Over")
        running = False

    # Draw everything with camera offset
    screen.fill((135, 206, 235))
    for sprite in all_sprites:
        screen.blit(sprite.image, camera.apply(sprite.rect))

    pygame.display.flip()

pygame.quit()
sys.exit()
