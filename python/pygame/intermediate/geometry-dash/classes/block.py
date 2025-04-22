import pygame

class Block(pygame.sprite.Sprite):
    def __init__(self, x, y, width=50, height=50):
        super().__init__()
        self.image = pygame.Surface((width, height))
        self.image.fill((100, 100, 100))  # Gray color
        self.rect = self.image.get_rect(topleft=(x, y))
