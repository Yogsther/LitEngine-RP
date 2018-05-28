import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Game {

    /* Define textures to use */
    public static String[] textures = {"sprite.png", "flame_throw.png", "wood.png"};
    /* Map for the world */
    public static ArrayList<GameObject> map = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        LitRP.init(); // Start LitEngine RP
        Map01.populate(); // Load map
    }

    public static void logic(){
        /* Logic method */

    }
    public static void draw(Graphics g){
        /* Draw method */
    }
}