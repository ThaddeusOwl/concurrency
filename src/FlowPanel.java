/**FlowPanel.java: Provides methods for manipulating the model and updating the GUI
 * @author: Unknown, Modified by Thaddeus Owl
 */

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JPanel;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	Water water;
	AtomicBoolean running = new AtomicBoolean(false);
	AtomicBoolean end = new AtomicBoolean(false);
	//Boolean end = false;
	//Boolean running = false;
	int lo;
	int hi;
	String threadNumber;

	/**Constructor takes in a Terrain Object, a water object, The beginning index and ending index of which the given thread should process, and the thread number for debugging purposes*/
	FlowPanel(Terrain terrain, Water w, int lo, int hi, String threadNumber) {
		land = terrain;
		water = w;
		this.lo = lo;
		this.hi = hi;
		this.threadNumber = threadNumber;
	}

	// responsible for painting the terrain and water
	// as images
	@Override
	/** Responsible for painting the grid. The terrain info overlayed by the water info*/
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


	/**Update the GUI to reflect the latest water infomation*/
	public void refresh() {
		repaint();
	}

	/**Start the water simulation by changing a boolean value*/
	public void startRun(){
		running.set(true);
		//running = true;
	}

	/**Pause the water simulation by changing a boolean value*/
	public void pauseRun() {
		running.set(false);
		//running = false;
	}

	/**Zeroes all the water values in the grid and updates the grid colour to reflect it*/
	public void reset(){
		running.set(false);
		//running = false;
		water.resetAndRecolour();
	}

	/**Leaves the simulation loop by changing a boolean value and then ends the program*/
	public void end(){
		end.set(true);
		//end = true;
		System.exit(0);
	}

	/**Runs the threads for their respective section and syncs them so that there is no repainting until all the threads have been processed*/
	public void run(){
		while (!end.get()) {
		//while (!end){
			while (running.get()) {
			//while (running){
				//for (int i = 0; i < land.getDimX() * land.getDimY(); i++) {
				for (int i = lo; i < hi ; i++) {
					int[] coordinates = new int[2];
					land.getPermute(i, coordinates);
					if (coordinates[0] < (land.getDimX() - 1) && coordinates[1] < (land.getDimY() - 1)
						&& coordinates[0] > 0 && coordinates[1] > 0 && water.getDepth(coordinates[0], coordinates[1]) != 0f) {
						int[] newCoordinates = water.getMin(coordinates[0], coordinates[1]);
						water.transfer(coordinates[0], coordinates[1], newCoordinates[0], newCoordinates[1]);
					}

				}

				//repaint();

				//Beautiful piece of code to make sure all threads have been processed before repainting

				Flow.numThreads.getAndIncrement();
				//System.out.println(threadNumber + " before check 1");
				while(Flow.numThreads.get() < 4){ }
				//System.out.println(threadNumber + " after check 1");
				if( Flow.numThreads2.get() == 4){
					Flow.numThreads2.set(0);
					//System.out.println(threadNumber + " after numthreads2 set to zero");
				}

				Flow.fp.repaint();

				Flow.numThreads2.getAndIncrement();
				//System.out.println(threadNumber + " before check 2");
				while (Flow.numThreads2.get() < 4){}
				//System.out.println(threadNumber + " after check 2");
				if(Flow.numThreads.get() == 4){
					Flow.numThreads.set(0);
					int a = Flow.count.incrementAndGet();
					Flow.countLabel.setText(Integer.toString(a));
					//System.out.println(threadNumber + " after numthreads1 set to zero");
				}

			}

		}


	}
}