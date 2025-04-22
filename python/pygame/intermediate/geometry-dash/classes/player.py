import pygame

class Player(pygame.sprite.Sprite):
    def __init__(self, x, y, size=50):
        super().__init__()
        self.image = pygame.Surface((size, size))
        self.image.fill((0, 255, 0))  # Green color
        self.rect = self.image.get_rect(topleft=(x, y))
        self.velocity_y = 0
        self.jump_strength = -25
        self.gravity = 1

    def update(self, dt, scroll_speed):
        self.velocity_y += self.gravity * dt * 60
        self.rect.y += self.velocity_y * dt * 60
        self.rect.x += scroll_speed * dt * 60

    def jump(self):
        self.velocity_y = self.jump_strength