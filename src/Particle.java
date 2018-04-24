public class Particle {
    int lift = 0;
    double opacity = (Math.random() * 2) - .5;
    int x;
    int y;
    int[] color;
    public Particle(int x, int y, String color){
        this.x = x;
        this.y = y;
        this.color = convert(color);
    }
    private int[] convert(String color){
        return new int[]{
                Integer.valueOf( color.substring( 1, 3 ), 16 ),
                Integer.valueOf( color.substring( 3, 5 ), 16 ),
                Integer.valueOf( color.substring( 5, 7 ), 16 )
        };
    }
}
