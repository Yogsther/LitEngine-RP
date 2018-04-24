import java.awt.image.BufferedImage;

public class Player {
    int x;
    int y;
    BufferedImage sprite;
    boolean alive = true;
    // TODO: Invertory, everything bla bla bla
    public Player(int x, int y, BufferedImage sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
}
