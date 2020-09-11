import java.awt.*;
import java.awt.image.BufferedImage;

public class Water {

    float depths[][];
    int dimx, dimy; // data dimensions
    BufferedImage img; // image for displaying the water top-down

    public Water(int x, int y){
        dimx = x;
        dimy = y;
        depths = new float[x][y];
        img = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
    }

    // overall number of elements in the height grid
    int dim(){
        return dimx*dimy;
    }

    // get x-dimensions (number of columns)
    int getDimX(){
        return dimx;
    }

    // get y-dimensions (number of rows)
    int getDimY(){
        return dimy;
    }

    // get water image
    public BufferedImage getImage() {
        return img;
    }

    public void fill(int x, int y, float depth){
        depths[x][y] = depth;
        Color col = new Color(0,0,255);
        img.setRGB(x, y, col.getRGB());
    }



}
