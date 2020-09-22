/** Water.java: Stores information about and provides methods to help use, manipulate and traverse the water depths at each point in the terrain.
 * @author: Thaddeus Owl
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Water {

    float depths[][];
    int dimx, dimy; // data dimensions
    BufferedImage img; // image for displaying the water top-down
    //float[][] landdata;
    float[][] total;
    Color col1 = new Color(0,0,255);
    float unit = 0.01f;

    /**Constructor takes in x and y dimensions and a terrain array*/
    public Water(int x, int y, float[][] ld){
        dimx = x;
        dimy = y;
        depths = new float[x][y];
        img = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
        reset();
        total = ld;

    }

    /**Zeros all the depths in the water array*/
    public void reset(){
        for(int i = 0; i < depths.length; i++){
            for(int j = 0; j < depths[0].length; j++){
                depths[i][j] = 0f;
            }
        }
    }

    /**Zeroes all the water depths in the array and recolours them to transparent*/
    public void resetAndRecolour(){
        for(int i = 0; i < depths.length; i++){
            for(int j = 0; j < depths[0].length; j++){
                depths[i][j] = 0f;
                img.setRGB(i, j, 0);
            }
        }
    }

    /**overall number of elements in the height grid*/
    int dim(){
        return dimx*dimy;
    }

    /** get x-dimensions (number of columns)*/
    int getDimX(){
        return dimx;
    }

    /** get y-dimensions (number of rows) */
    int getDimY(){
        return dimy;
    }

    /** Get the water depth of given coordinate */
    public float getDepth(int x, int y){
        return depths[x][y];
    }

    /** get water image */
    public BufferedImage getImage() {
        return img;
    }

    /** Fill a given location with given water depth*/
    public synchronized void fill(int x, int y, float depth){
        depths[x][y] = depths[x][y] + depth;
        total[x][y] = total[x][y] + depth;
        img.setRGB(x, y, col1.getRGB());
    }

    /*public int countWater(){
        int waterUnits = 0;
        for(int i = 0; i < depths.length; i++){
            for(int j = 0; j < depths[0].length; j++){
                waterUnits = waterUnits + (int)(depths[i][j]*100);
            }
        }
        return Flow.countWaterUnits.addAndGet(waterUnits);
    }*/

    /**Find the neighbour with the minimum combined terrain and water height*/
    public synchronized int[] getMin(int x, int y){
        int[] toReturn = new int[2];
        int minX = x;
        int minY = y;
        float lowest = total[x][y];
        if(lowest > total[x+1][y+1]){
            minX = x+1;
            minY = y+1;
            lowest = total[x+1][y+1];
        }
        if(lowest > total[x+1][y]){
            minX = x+1;
            minY = y;
            lowest = total[x+1][y];
        }
        if(lowest > total[x+1][y-1]){
            minX = x+1;
            minY = y-1;
            lowest = total[x+1][y-1];
        }
        if(lowest > total[x-1][y+1]){
            minX = x-1;
            minY = y+1;
            lowest = total[x-1][y+1];
        }
        if(lowest > total[x-1][y]){
            minX = x-1;
            minY = y;
            lowest = total[x-1][y];
        }
        if(lowest > total[x-1][y-1]){
            minX = x-1;
            minY = y-1;
            lowest = total[x-1][y-1];
        }
        if(lowest > total[x][y+1]){
            minX = x;
            minY = y+1;
            lowest = total[x][y+1];
        }
        if(lowest > total[x][y-1]){
            minX = x;
            minY = y-1;
            lowest = total[x][y-1];
        }

        toReturn[0] = minX;
        toReturn[1] = minY;
        return toReturn;
    }

    /**Transfer one unit of water (0.01 meters) to the lowest neighbour if there is one*/
    public synchronized void transfer(int x1, int y1, int x2, int y2){
        depths[x1][y1] = depths[x1][y1] - unit;
        total[x1][y1] = total[x1][y1] - unit;
        if(x2 == 0 || y2 == 0 || x2 == (getDimX() - 1) || y2 == (getDimY() - 1)){
            //System.out.println(unit + " flowed off edge at "+ x1 +"x"+ y1);
            //Flow.countWaterUnits.incrementAndGet();
        }else {
            fill(x2, y2, unit);
            //System.out.println(unit + " moved from "+ x1 +"x"+ y1 +" to "+ x2 +"x"+ y2);
        }
        if(depths[x1][y1] == 0f){
                img.setRGB(x1, y1, 0);
        }



    }


}
