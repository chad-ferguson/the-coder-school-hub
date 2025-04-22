import os
import pygame
import csv
from classes.block import Block
from classes.spike import Spike

class Level:
    def __init__(self, level_file="level1.csv", scroll_speed=5, screen_width=800, screen_height=600):
        self.blocks = pygame.sprite.Group()
        self.spikes = pygame.sprite.Group()
        self.scroll_speed = scroll_speed
        self.tile_size = 50  # Each tile is 50x50 pixels
        self.level_data = []
        self.player_start = None  # This will hold the (x, y) position for the player

        # Prepend the levels directory relative to your project root.
        full_path = os.path.join("python/pygame/intermediate/geometry-dash/levels", level_file)
        self.load_level(full_path)
        self.create_level(screen_height)

    def load_level(self, full_path):
        """Loads the level layout from a CSV file."""
        try:
            with open(full_path, newline='') as csvfile:
                reader = csv.reader(csvfile)
                # Read each non-empty row from the CSV file.
                self.level_data = [row for row in reader if row]
        except Exception as e:
            print(f"Error loading level file '{full_path}': {e}")

    def create_level(self, screen_height=600):
        """Align the row with the platform as the bottom row, player just above it."""
        platform_row_index = None
        player_row_index = None

        for i, row in enumerate(self.level_data):
            if any(cell.strip() == "P" for cell in row):
                player_row_index = i

        if player_row_index is not None:
            # Align the player row to appear ~tile_size above bottom of screen
            y_offset = screen_height - ((player_row_index + 2) * self.tile_size)
        else:
            # Default fallback (e.g. align bottom of level with bottom of screen)
            level_rows = len(self.level_data)
            level_height = level_rows * self.tile_size
            y_offset = screen_height - level_height if level_height < screen_height else 0

        for row_index, row in enumerate(self.level_data):
            for col_index, tile in enumerate(row):
                tile = tile.strip()
                x = col_index * self.tile_size
                y = row_index * self.tile_size + y_offset
                if tile == "B":
                    block = Block(x, y, self.tile_size, self.tile_size)
                    self.blocks.add(block)
                elif tile == "S":
                    spike = Spike(x, y, self.tile_size)
                    self.spikes.add(spike)
                elif tile == "P":
                    self.player_start = (x, y)

