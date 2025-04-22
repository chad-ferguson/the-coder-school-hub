class Camera:
    def __init__(self, screen_width, screen_height, margin_ratio=0.15):
        self.camera_x = 0
        self.camera_y = 0
        self.scroll_x = 0  # New horizontal scroll position
        self.screen_width = screen_width
        self.screen_height = screen_height
        self.margin_top = screen_height * margin_ratio
        self.margin_bottom = screen_height * (1 - margin_ratio)
        self.margin_left = screen_width * margin_ratio
        self.margin_right = screen_width * (1 - margin_ratio)

    def update(self, target_rect):
        # Follow scroll_x directly for horizontal scrolling
        self.camera_x = self.scroll_x

        # Vertical scrolling with margin
        if target_rect.top - self.camera_y < self.margin_top:
            self.camera_y = target_rect.top - self.margin_top
        elif target_rect.bottom - self.camera_y > self.margin_bottom:
            self.camera_y = target_rect.bottom - self.margin_bottom

    def apply(self, rect):
        return rect.move(-self.camera_x, -self.camera_y)