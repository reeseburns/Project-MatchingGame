import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Planets {
    private BufferedImage planetImage;
    private String[] planetName = {
        "0star.png", "1earth.png", "2jupiter.png", "3saturn.png",
        "4venus.png", "5neptune.png", "6moon.png", "7sun.png", "8astronaut.png"
    };

    private int show = 0;
    private int planetX = 0;
    private int planetY = 0;
    private int planetValue = 0;
    private boolean active = true; // New flag to manage visibility

    public Planets(int x, int y, int planet) {
        planetX = x;
        planetY = y;
        planetValue = planet;
    }

    public void display(Graphics g) {
        if (active) { // Only draw if active
            try {
                planetImage = ImageIO.read(new File(planetName[show]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(planetImage, planetX, planetY, null);
        }
    }

    // SETTER METHODS
    public void displayPlanet() {show = planetValue;}
    public void hidePlanet() {show = 0;}
    public void matched() {active = false;} // Set inactive when matched

    // GETTER METHODS
    public int getX() {return planetX;}
    public int getY() {return planetY;}
    public boolean isVisible() {return show != 0 && active;}
    public int getValue() {return planetValue;}
}