import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class CatchTheApples extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int score = 0;
    private int highScore = 0;

    // Basket position and physics variables
    private double basketX = 375;  // Using double for smoother physics
    private final int basketY = 600;
    private double basketVelocity = 0.0;
    private final double acceleration = 0.5;
    private final double friction = 0.95;

    // Apple position and falling speed
    private int appleX = 0;
    private int appleY = 0;
    private int appleSpeed = 5;  // initial falling speed
    private final int maxAppleSpeed = 15;

    // Images
    private BufferedImage basketImgOriginal;
    private BufferedImage appleImgOriginal;
    private BufferedImage basketImg;
    private BufferedImage appleImg;
    private BufferedImage treeBackgroundImg;

    // Dimensions
    private final int BASKET_WIDTH = 140;
    private final int BASKET_HEIGHT = 100;
    private final int APPLE_WIDTH = 80;
    private final int APPLE_HEIGHT = 80;

    private final int WIDTH = 750;
    private final int HEIGHT = 750;
    private Random rand = new Random();

    // Flags for key presses
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    // Game state flag
    private boolean gameOver = false;
    
    // Buttons for game over screen (added to the panel)
    private JButton playAgainButton;
    private JButton exitButton;

    public CatchTheApples() {
        // Set up the panel with null layout so we can position buttons absolutely
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Create and add the Play Again button to this panel
        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(WIDTH / 2 - 110, HEIGHT / 2 + 40, 200, 40);
        playAgainButton.setFocusable(false);
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 18));
        playAgainButton.setBackground(new Color(0, 150, 0));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 2));
        playAgainButton.setFocusPainted(false);
        playAgainButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        playAgainButton.setVisible(false);
        add(playAgainButton);

        // Create and add the Exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(WIDTH / 2 - 60, HEIGHT / 2 + 90, 120, 40);
        exitButton.setFocusable(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBackground(new Color(150, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 0), 2));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitButton.setVisible(false);
        add(exitButton);

        // Set up the frame and add this panel as its content pane
        JFrame frame = new JFrame("Catch the Apples");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(this); // Frame listens for key events

        // Load images
        try {
            basketImgOriginal = ImageIO.read(new File("assets/basket_img.png"));
            appleImgOriginal = ImageIO.read(new File("assets/apple_img.png"));
            treeBackgroundImg = ImageIO.read(new File("assets/tree_img.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        // Scale images
        basketImg = getScaledImage(basketImgOriginal, BASKET_WIDTH, BASKET_HEIGHT);
        appleImg = getScaledImage(appleImgOriginal, APPLE_WIDTH, APPLE_HEIGHT);
        treeBackgroundImg = getScaledImage(treeBackgroundImg, WIDTH, HEIGHT);

        // Initialize apple position
        appleX = rand.nextInt(WIDTH - APPLE_WIDTH);

        timer = new Timer(20, this);  // roughly 50 FPS
        timer.start();
    }

    // Utility method to scale images
    private BufferedImage getScaledImage(BufferedImage src, int w, int h) {
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(src, 0, 0, w, h, null);
        g2d.dispose();
        return scaled;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background tree image
        g.drawImage(treeBackgroundImg, 0, 0, WIDTH, HEIGHT, this);

        if (!gameOver) {
            // Draw game elements
            g.drawImage(basketImg, (int)basketX, basketY, this);
            g.drawImage(appleImg, appleX, appleY, this);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            g.drawString("Score: " + score, 10, 25);
            g.drawString("High Score: " + highScore, 10, 50);
        } else {
            // Draw in-panel game over screen with semi-transparent background
            g.setColor(new Color(0, 0, 0, 150)); // semi-transparent black background
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            String overText = "Game Over";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(overText);
            g.drawString(overText, (WIDTH - textWidth) / 2, HEIGHT / 2 - 40);

            g.setFont(new Font("Arial", Font.BOLD, 25));
            String scoreText = "Final Score: " + score;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText, (WIDTH - scoreWidth) / 2, HEIGHT / 2);

            String highScoreText = "High Score: " + highScore;
            int highScoreWidth = g.getFontMetrics().stringWidth(highScoreText);
            g.drawString(highScoreText, (WIDTH - highScoreWidth) / 2, HEIGHT / 2 + 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Update basket physics
            if (leftPressed) {
                if (basketVelocity > 0) {
                    basketVelocity -= acceleration * 1.5;
                } else {
                    basketVelocity -= acceleration;
                }
            }
            if (rightPressed) {
                if (basketVelocity < 0) {
                    basketVelocity += acceleration * 1.5;
                } else {
                    basketVelocity += acceleration;
                }
            }
            basketVelocity *= friction;
            basketX += basketVelocity;
            // Clamp basket to screen edges (no bounce)
            if (basketX < 0) {
                basketX = 0;
                basketVelocity = 0;
            } else if (basketX > WIDTH - BASKET_WIDTH) {
                basketX = WIDTH - BASKET_WIDTH;
                basketVelocity = 0;
            }

            // Move apple
            appleY += appleSpeed;

            // Collision detection
            if (pixelPerfectCollision(basketImg, (int)basketX, basketY,
                                      appleImg, appleX, appleY)) {
                score++;
                if (score % 3 == 0 && appleSpeed < maxAppleSpeed) {
                    appleSpeed++;
                }
                resetApple();
            }
            // Game over condition: apple falls past the bottom
            if (appleY > HEIGHT) {
                gameOver = true;
                if (score > highScore) {
                    highScore = score;
                }
                timer.stop();
                // Show the buttons
                playAgainButton.setVisible(true);
                exitButton.setVisible(true);
            }
        }
        repaint();
    }

    // Reset apple to the top at a random horizontal position
    private void resetApple() {
        appleY = 0;
        appleX = rand.nextInt(WIDTH - APPLE_WIDTH);
    }
    
    // Restart the game
    private void restartGame() {
        score = 0;
        appleSpeed = 5;
        basketX = 375;
        basketVelocity = 0;
        appleX = rand.nextInt(WIDTH - APPLE_WIDTH);
        appleY = 0;
        gameOver = false;
        playAgainButton.setVisible(false);
        exitButton.setVisible(false);
        timer.start();
    }

    /**
     * Pixel-perfect collision detection method.
     */
    public boolean pixelPerfectCollision(BufferedImage img1, int x1, int y1,
                                           BufferedImage img2, int x2, int y2) {
        Rectangle r1 = new Rectangle(x1, y1, img1.getWidth(), img1.getHeight());
        Rectangle r2 = new Rectangle(x2, y2, img2.getWidth(), img2.getHeight());
        Rectangle intersection = r1.intersection(r2);
        if (intersection.isEmpty()) {
            return false;
        }
        int startX1 = intersection.x - x1;
        int startY1 = intersection.y - y1;
        int startX2 = intersection.x - x2;
        int startY2 = intersection.y - y2;

        for (int y = 0; y < intersection.height; y++) {
            for (int x = 0; x < intersection.width; x++) {
                int pixel1 = img1.getRGB(startX1 + x, startY1 + y);
                int pixel2 = img2.getRGB(startX2 + x, startY2 + y);
                int alpha1 = (pixel1 >> 24) & 0xff;
                int alpha2 = (pixel2 >> 24) & 0xff;
                if (alpha1 > 0 && alpha2 > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    public static void main(String[] args) {
        new CatchTheApples();
    }
}
