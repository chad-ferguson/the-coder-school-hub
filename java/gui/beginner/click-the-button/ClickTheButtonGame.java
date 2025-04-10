import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ClickTheButtonGame extends JFrame {
    private JButton button;
    private JLabel scoreLabel;
    private int score = 0;
    private Random rand = new Random();

    public ClickTheButtonGame() {
        setTitle("Click the Button Game");
        setSize(400, 400);
        setLayout(null); // Allow manual positioning
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setBounds(10, 10, 100, 30);
        add(scoreLabel);

        button = new JButton("Click me!");
        button.setBounds(150, 150, 100, 50);
        add(button);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                score++;
                scoreLabel.setText("Score: " + score);
                moveButton();
            }
        });

        setVisible(true);
    }

    private void moveButton() {
        int x = rand.nextInt(getWidth() - 120);
        int y = rand.nextInt(getHeight() - 120);
        button.setLocation(x, y);
    }

    public static void main(String[] args) {
        new ClickTheButtonGame();
    }
}
