import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class LitRP extends JPanel {

    JFrame frame;
    public static ArrayList<NPC> npcs = new ArrayList<>();
    public static ArrayList<Texture> textures = new ArrayList<>();
    public static ArrayList<Particle> particles = new ArrayList<>();

    final static int WIDTH = 192;
    final static int HEIGHT = 108;
    final static int SCALE = 7;

    public int camX = (WIDTH ) / 2;
    public int camY = (HEIGHT) / 2;

    public static LitRP rp;
    public static String title = "LitEngineRP";

    ImageIcon img = new ImageIcon("icon.png");

    public static Set<Integer> keysDown = new HashSet<>();

    public LitRP(){
        this.frame = new JFrame(title);
        this.frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
        this.frame.add(this);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.decode("#111111"));
        this.frame.setIconImage(img.getImage());
        this.addKeyListener(new Listener());
        this.setFocusable(true);
        this.frame.setVisible(true);
        //this.frame.setLayout(new BorderLayout());
        //this.frame.pack();
        // Import test
        try {
            sprite = ImageIO.read(new File("textures/sprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void init() throws IOException {
        importTextures();
        rp = new LitRP();
    }

    public static void importTextures() throws IOException {
        String[] texturesToImport = Game.textures;
        for(int i = 0; i < texturesToImport.length; i++){
            String source = texturesToImport[i].substring(0, texturesToImport[i].indexOf('.'));
            textures.add(new Texture(source, ImageIO.read(new File("textures/" + texturesToImport[i]))));
        }
    }

    public static BufferedImage getTexture(String textureName){

        Iterator<Texture> foreach = textures.iterator();
        Texture result = null;
        while(foreach.hasNext()){
            Texture texture = foreach.next();
            if(texture.source.equals(textureName)) result = texture;
        }
        return result.texture;
    }

    public static int randomInt(int low, int high){
        double r = Math.floor(Math.random() * (high-low)) + low;
        return (int)r;
    }

    public static void spawnParticle(){
        /* Spawn particles on player */
        BufferedImage sprite = getTexture("sprite");
        int spriteOffset = sprite.getWidth() / 2;
        int spread = 7;
        particles.add(new Particle(randomInt(x + spriteOffset - spread, x + spriteOffset + spread), randomInt(y - spread, y + spread)));
    }

    public class Listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(!keysDown.contains(e.getKeyCode())) keysDown.add(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(e.getKeyCode());
        }
    }

    private static int x = 0;
    private static int y = 0;

    BufferedImage sprite;

    @Override
    public void paintComponent(Graphics g) {

        if(g == null){
            System.out.println("Null");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
            return;
        }

        /* Logic */
        int speed = 2;
        if(keysDown.size() > 1) speed /= 2; /* Slow down if diagonal */
        /* Player movement */
        if(keysDown.contains(87)) y-=speed;
        if(keysDown.contains(83)) y+=speed;
        if(keysDown.contains(65)) x-=speed;
        if(keysDown.contains(68)) x+=speed;

        spawnParticle();



        /* Clear background */
        g.setColor(Color.decode("#111111"));
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

        // TODO: Render tiles and background

        renderParticles(g); // Render particles behind player and NPCs and GUI

        /* Draw NPCs */
        Iterator<NPC> foreach = npcs.iterator();
        while(foreach.hasNext()){
            NPC npc = foreach.next();
            drawImage(getTexture("sprite"), npc.x, npc.y, g);
        }


        drawImage(getTexture("sprite"), x, y, g);


        drawOverlay(g); // Draw GUI

        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        repaint();
    }

    public void drawImage(BufferedImage img, int x, int y, Graphics g){
        /* This method renders relative to scale and camera position. */
        try{
            g.drawImage(img, (x + camX) * SCALE, (y + camY) * SCALE, img.getWidth() * SCALE, img.getHeight() * SCALE, this);
        } catch(NullPointerException e){
            System.out.println("Problem drawing image. " + e.getMessage());
        }

    }

    public void renderParticles(Graphics g){
        Iterator<Particle> foreach = particles.iterator();
        while(foreach.hasNext()){
            Particle particle = foreach.next();
            if(particle.opacity < 0){
                foreach.remove();
            } else {
                particle.opacity -= .1;
                particle.lift++;

                drawImage(getTexture("flair"), particle.x, particle.y - particle.lift, g);
            }
        }
    }

    public static void addNPC(int x, int y, BufferedImage img){ npcs.add(new NPC(x, y, img)); }

    public void drawOverlay(Graphics g){
        String printText = "LitEngineRP";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ubuntu", Font.BOLD, 20));
        g.drawString(printText,(WIDTH * SCALE) - (printText.length() * 15), (HEIGHT * SCALE) - 75);
    }

}

