import pygame

class Spike(pygame.sprite.Sprite):
    def __init__(self, x, y, size=50):
        super().__init__()
        self.image = pygame.Surface((size, size), pygame.SRCALPHA)
        pygame.draw.polygon(self.image, (255, 0, 0), [(0, size), (size / 2, 0), (size, size)])
        self.rect = self.image.get_rect(topleft=(x, y))
