import java.awt.image.BufferedImage;

public class GameObject {
    /**
     * GameObject is an object in the map, like a tree or road.
     * @param sprite texture for the object
     * @param x position x for the object
     * @param y position y for the object
     * @param collisions whether or not the object has collisions enabled.
     */
    int x;
    int y;
    String sprite;
    float scale;
    boolean collisions;
    public GameObject (String sprite, int x, int y, boolean collisions, float scale) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.collisions = collisions;
        this.scale = scale;
    }
}