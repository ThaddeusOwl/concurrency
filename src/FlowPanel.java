//package FlowSkeleton;

import java.awt.Graphics;
import javax.swing.JPanel;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	Water water;
	
	FlowPanel(Terrain terrain, Water w) {
		land=terrain;
		water = w;
	}
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null) {
			g.drawImage(land.getImage(), 0, 0, null);
		}

			g.drawImage(water.getImage(), 0, 0, null);

	}

	public void refresh(){
		repaint();
	}

	public void run() {	
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
	    repaint();
	}
}