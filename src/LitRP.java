import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
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
    public static Player player;
    public static String title = "LitEngineRP";

    ImageIcon img = new ImageIcon("icon.png");

    public static Set<Integer> keysDown = new HashSet<>();

    public static boolean displayDevTools = false;

    public LitRP(){
        /* Set up canvas and JFrame */
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
    }


    public static void init() throws IOException {
        /**
         * Initiate the game, run this when you want your game to start running.
         */
        importTextures();
        player = new Player(0, 0, getTexture("sprite"));
        rp = new LitRP();
    }

    public static void importTextures() throws IOException {
        /**
         * Imports all textures provided in Game.java
         */
        String[] texturesToImport = Game.textures; // Get textures from Game.java
        for(int i = 0; i < texturesToImport.length; i++){
            // Loop through and import each texture.
            String source = texturesToImport[i].substring(0, texturesToImport[i].indexOf('.'));
            textures.add(new Texture(source, ImageIO.read(new File("textures/" + texturesToImport[i]))));
        }
    }

    public static BufferedImage getTexture(String textureName){
        /**
         * Get a texture from source String.
         * @param textureName name of the texture.
         *
         * @return the texture.
         */
        Iterator<Texture> foreach = textures.iterator();
        Texture result = null;
        while(foreach.hasNext()){
            Texture texture = foreach.next();
            if(texture.source.equals(textureName)) result = texture;
        }
        return result.texture;
    }

    public static int randomInt(int low, int high){
        /**
         * Get a random int between low and high
         *
         * @param low Set the lower range.
         * @param high Set the higher range.
         *
         * @return random int between low and high
         */
        double r = Math.floor(Math.random() * (high-low)) + low;
        return (int)r;
    }

    public static void spawnParticle(int x, int y, String color){
        /**
         * Spawn a particle at x,y with a custom color.
         *
         * @param x position x for the particle
         * @param y position y for the particle
         * @param color the color for your particle (HEX)
         */
        /* Spawn particles on player */
        particles.add(new Particle(x, y, color));
    }

    public static void spawnParticleOnPlayer(){
        /**
         * Spawns particles on the player if any are active.
         */
        int spriteOffset = player.sprite.getWidth() / 2;
        int spread = 10;
        spawnParticle(randomInt(player.x + spriteOffset - spread, player.x + spriteOffset + spread), player.y, "#599cf4");
    }


    public class Listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(!keysDown.contains(e.getKeyCode())) keysDown.add(e.getKeyCode());
            if(e.getKeyCode() == 69){
                /* Key E to open the map-editor */
                displayDevTools = !displayDevTools; // Toggle
            }
            if(displayDevTools){
                System.out.println(e.getKeyCode()); // Print keycode in devmode
                /* Place tree */
                if(e.getKeyCode() == 10) Game.map.add(new GameObject("wood", player.x, player.y, true, 0.2f));

                if(e.getKeyCode() == 67){
                    /* Copy map to clipboard */
                    String mapString = "/* LitEngineRP Map - Size: " + Game.map.size() + " */\n";
                    Iterator<GameObject> foreachMAP = Game.map.iterator();
                    while(foreachMAP.hasNext()){
                        /* Draw each GameObject (obj) */
                        GameObject obj = foreachMAP.next();
                        mapString += "Game.map.add(new GameObject(\"" + obj.sprite + "\", " + obj.x + ", " + obj.y + ", " + obj.collisions + ", " + obj.scale + "f));\n";
                    }
                    StringSelection stringSelection = new StringSelection(mapString);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);

                }
                if(e.getKeyCode() == 82){
                    /* Reset map */
                    Game.map.clear();
                }
                if(e.getKeyCode() == 88){
                    /* Delete last placed object */
                    if(Game.map.size() > 0) Game.map.remove(Game.map.size()-1);
                }
            }


        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(e.getKeyCode());
        }
    }

    public boolean checkCollision(GameObject obj) {
        /**
         * Check if the player is colliding with
         * a GameObject.
         */
        if (player.x < obj.x + getTexture(obj.sprite).getWidth() * obj.scale &&
                player.x + player.sprite.getWidth() > obj.x &&
                player.y < obj.y + getTexture(obj.sprite).getHeight() * obj.scale &&
                player.sprite.getHeight() + player.y > obj.y) {

            return true;
        }
        return false;
    }

    public void logic(){
        /**
         * Logic method - Runs each frame
         */
        // LOGIC
        int speed = 2;
        if(keysDown.size() > 1) speed /= 2; /* Slow down if diagonal */
        /* Save player position, if there is a collision, the player position will be reverted */
        int pastX = player.x;
        int pastY = player.y;

        /* Player movement */
        if(keysDown.contains(87)) player.y-=speed;
        if(keysDown.contains(83)) player.y+=speed;
        if(keysDown.contains(65)) player.x-=speed;
        if(keysDown.contains(68)) player.x+=speed;

        for(int i = 0; i < 2 /* Amount of particles per frame */; i++){
            spawnParticleOnPlayer();
        }

        /* Draw out map */
        Iterator<GameObject> foreachMAP = Game.map.iterator();
        while(foreachMAP.hasNext()){
            /* Draw each GameObject (obj) */
            GameObject obj = foreachMAP.next();
            if(!displayDevTools && checkCollision(obj)){
                player.x = pastX;
                player.y = pastY;
            }
        }

    }

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

        // LOGIC

        logic();

        // END OF LOGIC

        /* Clear background */
        g.setColor(Color.decode("#111111"));
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

        /* Draw out map */
        Iterator<GameObject> foreachMAP = Game.map.iterator();
        while(foreachMAP.hasNext()){
            /* Draw each GameObject (obj) */
            GameObject obj = foreachMAP.next();
            drawImage(getTexture(obj.sprite), obj.x, obj.y, g, obj.scale);
        }


        renderParticles(g); // Render particles behind player and NPCs and GUI

        /* Draw NPCs */
        Iterator<NPC> foreachNPC = npcs.iterator();
        while(foreachNPC.hasNext()){
            NPC npc = foreachNPC.next();
            drawImage(getTexture("sprite"), npc.x, npc.y, g);
        }


        drawImage(player.sprite, player.x, player.y, g);

        Game.draw(g);


        // END OF DRAW
        drawOverlay(g); // Draw GUI

        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        repaint();
    }

    public void drawImage(BufferedImage img, int x, int y, Graphics g){
        /**
         * Draw an image from BufferedImage on the canvas. This will account for camera and scale.
         *
         * @param img the image you want to print, use getTexture() to get this.
         * @param x position x for the image left top
         * @param y position y for the image left top
         * @param g graphics component
         */
        try{
            g.drawImage(img, (x + camX) * SCALE, (y + camY) * SCALE, img.getWidth() * SCALE, img.getHeight() * SCALE, this);
        } catch(NullPointerException e){
            System.out.println("Problem drawing image. " + e.getMessage());
        }

    }

    public void drawImage(BufferedImage img, int x, int y, Graphics g, float scale){
        /**
         * Draw an image from BufferedImage on the canvas. This will account for camera and scale.
         *
         * @param img the image you want to print, use getTexture() to get this.
         * @param x position x for the image left top
         * @param y position y for the image left top
         * @param g graphics component
         * @param scale Scale of the image, proportional to the global SCALE, default would be 1.0
         */
        try{
            g.drawImage(img, (x + camX) * SCALE, Math.round((y + camY) * SCALE), Math.round(img.getWidth() * SCALE * scale), Math.round(img.getHeight() * SCALE * scale), this);
        } catch(NullPointerException e){
            System.out.println("Problem drawing image. " + e.getMessage());
        }

    }

    public void renderParticles(Graphics g){
        /**
         * Renders and moves all active particles
         */
        Iterator<Particle> foreach = particles.iterator();
        while(foreach.hasNext()){
            Particle particle = foreach.next();
            if(particle.opacity < 0){
                foreach.remove();
            } else {
                particle.opacity -= .05;
                particle.lift++;
                int[] positionsX = {1, 0, 1, 2, 1};
                int[] positionsY = {0, 1, 1, 1, 2};
                for(int i = 0; i < positionsX.length; i++){
                    int multiplier = 1;
                    if(i == 2) multiplier = 3;
                    int alpha = (int)Math.round(255 * particle.opacity * multiplier);
                    if(alpha > 255) alpha = 255;
                    if(alpha < 0) alpha = 0;
                    g.setColor(new Color(particle.color[0], particle.color[1], particle.color[2], alpha));
                    g.fillRect((particle.x + positionsX[i] + camX) * SCALE, (particle.y + positionsY[i] - particle.lift + camY + 10) * SCALE, 1 * SCALE, 1 * SCALE);
                }
            }
        }
    }

    public static void addNPC(int x, int y, BufferedImage img){ npcs.add(new NPC(x, y, img)); }

    public void drawOverlay(Graphics g){
        /**
         * Draws overlay, last in the render loop
         */
        String printText = "LitEngineRP";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ubuntu", Font.BOLD, 20));
        g.drawString(printText,(WIDTH * SCALE) - (printText.length() * 15), (HEIGHT * SCALE) - 75);
        if(displayDevTools){
            String[] instructions = {"ENTER - Place tree", "E - Toggle dev tools", "C - Copy map", "X - Delete last","R - Reset map","Maps size: " + Game.map.size()};
            for(int i = 0; i < instructions.length; i++){
                g.drawString(instructions[i], (WIDTH * SCALE) - 200, 50 + (i*20));
            }
        }
    }

}

