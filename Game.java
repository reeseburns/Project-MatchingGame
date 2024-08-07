import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {

    // VARIABLES
    BufferedImage background;
    Font myFont = new Font("Silom", Font.BOLD, 40);
    int numStars = 16;
    int startX = 60;
    int startY = 80;
    int count = 1;

    int[] xArr = new int[numStars];
    int[] yArr = new int[numStars];
    int[] planet = new int[numStars];
    Planets[] myPlanets = new Planets[numStars];

    int[] planetShown = new int[2];
    boolean[] clicked = new boolean[numStars];
    int flipped = 0;
    int pairs = 0;

    // METHOD: GAME SETUP
    public Game() {
        try {
            background = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numStars; i++) {
            clicked[i] = false;
            xArr[i] = startX;       // planet's x val
            yArr[i] = startY;       // planet's y val
            planet[i] = count;      // pairs made
            count += 1;

            if (count == 9) {count = 1;}        // 8 total pairs
            if (startX < 500) {startX += 175;}  // border = 500
            else if (startX > 500) {
                startX = 60;                    // move row back to startX
                startY += 150;                  // move row down
            }
        }

        mixPlanets();   // call method to mix planets

        for (int i = 0; i < numStars; i++) {
            myPlanets[i] = new Planets(xArr[i], yArr[i], planet[i]); // init each planet
        }

        // MOUSE ANIMATION
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                for (int i = 0; i < numStars; i++) {
                    int cardX = xArr[i];
                    int cardY = yArr[i];
                    boolean withinX = mouseX > cardX && mouseX < (cardX + 150);
                    boolean withinY = mouseY > cardY && mouseY < (cardY + 150);

                    if (withinX && withinY && !clicked[i]) {
                        clicked[i] = true;
                        myPlanets[i].displayPlanet();
                        planetShown[flipped] = i;
                        flipped++;

                        if (flipped == 2) {     // flip 2 cards --> check if match
                            flipped = 0;

                            // cards match
                            if (planet[planetShown[0]] == planet[planetShown[1]]) {
                                myPlanets[planetShown[0]].matched();
                                myPlanets[planetShown[1]].matched();
                                pairs += 1;
                            } else {
                                // Timer to hide planets after 1 second if not matching
                                Timer timer = new Timer(500, new ActionListener() { // Shorter delay
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        for (int j = 0; j < 2; j++) {
                                            myPlanets[planetShown[j]].hidePlanet(); // hide planet
                                            clicked[planetShown[j]] = false; // not clicked anymore
                                        }
                                        repaint();
                                    }
                                });
                                timer.setRepeats(false); // One-time delay
                                timer.start();
                            }
                        }
                        repaint();
                    }
                }
            }
        });
    }

    // METHOD: MIX PLANETS
    public void mixPlanets() {
        ArrayList<Integer> tempList = new ArrayList<>();
        for (int val : planet) {tempList.add(val);}
        Collections.shuffle(tempList);
        for (int i = 0; i < planet.length; i++) {planet[i] = tempList.get(i);}
    }

    // METHOD: A
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.setFont(myFont);
        g.setColor(new Color(139, 0, 139));
        g.drawString("SPACE MATCH", 260, 60);

        // display grid
        for (int i = 0; i < numStars; i++) {myPlanets[i].display(g);}

        // match all 8 pairs
        if (pairs == 8) {
            g.setColor(new Color(139, 0, 139));
            g.drawString("You Win", 310, 360);
            g.drawString("GAME OVER", 280, 400);
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Match");
        Game gamePanel = new Game();

        frame.add(gamePanel);
        frame.setSize(800, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}