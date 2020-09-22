/**Flow.java: Provides main method and GUI. Used as the bridge between the users interactions and the model manipulation
 * Takes in one argument: The file that stores the grid information
 * @author Unknown, Modified by Thaddeus Owl
*/

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.util.concurrent.atomic.AtomicInteger;


public class Flow {
	static int frameX;
	static int frameY;
	static FlowPanel fp;

	static FlowPanel fp1;
	static FlowPanel fp2;
	static FlowPanel fp3;
	static FlowPanel fp4;
	static AtomicInteger numThreads = new AtomicInteger();
	static AtomicInteger numThreads2 = new AtomicInteger();
	static AtomicInteger count = new AtomicInteger();
	static JLabel countLabel;
	//static AtomicInteger countWaterUnits = new AtomicInteger();

	/**Sets up GUI for user to control the model manipulation. Takes in the dimensions and the terrain and water object to be manipulated*/
	public static void setupGUI(int frameX,int frameY,Terrain landdata, Water water) {

		numThreads.set(0);
		numThreads2.set(0);
		count.set(0);

		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   
		fp = new FlowPanel(landdata, water, 0, 0, "Painting thread");
		fp.setPreferredSize(new Dimension(frameX,frameY));
		g.add(fp);

		fp1 = new FlowPanel(landdata, water, 0, landdata.dim()/4, "Thread Number 1");
		fp2 = new FlowPanel(landdata, water, landdata.dim()/4, landdata.dim()/2, "Thread Number 2");
		fp3 = new FlowPanel(landdata, water, landdata.dim()/2, (landdata.dim()*3)/4, "Thread Number 3");
		fp4 = new FlowPanel(landdata, water, (landdata.dim()*3)/4, landdata.dim(), "Thread Number 4");

		countLabel = new JLabel("0");
		JLabel timeStepCount = new JLabel(" Timestep count: ");

		// to do: add a MouseListener, buttons and ActionListeners on those buttons
		fp.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				water.fill(e.getX(), e.getY(), (float) 1);
				fp.refresh();
			}
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
		});
	   	
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));

	    /*JButton countWater = new JButton("Count Water");
	    countWater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(water.countWater());
			}
		});*/

	    JButton endB = new JButton("End");;
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				//fp.pauseRun();
				fp1.pauseRun();
				fp2.pauseRun();
				fp3.pauseRun();
				fp4.pauseRun();
				frame.dispose();
				fp.end();
			}
		});

		JButton playB = new JButton("Play");
		playB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//fp.startRun();
				fp1.startRun();
				fp2.startRun();
				fp3.startRun();
				fp4.startRun();
			}
		});


		JButton pauseB = new JButton("Pause");
		pauseB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//fp.pauseRun();
				fp1.pauseRun();
				fp2.pauseRun();
				fp3.pauseRun();
				fp4.pauseRun();
			}
		});

		JButton resetB = new JButton("Reset");
		resetB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fp.reset();
				count.set(0);
				//countWaterUnits.set(0);
				countLabel.setText("0");
				fp.refresh();
			}
		});

		b.add(resetB);
		b.add(pauseB);
		b.add(playB);
		b.add(endB);
		b.add(timeStepCount);
		//b.add(countWater);
		b.add(countLabel);
		g.add(b);
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);

		//Thread fpt = new Thread(fp);
		//fpt.start();
        Thread fp1t = new Thread(fp1);
		Thread fp2t = new Thread(fp2);
		Thread fp3t = new Thread(fp3);
		Thread fp4t = new Thread(fp4);
		fp1t.start();
		fp2t.start();
		fp3t.start();
		fp4t.start();

	}
	
	/**The main method that runs the program*/
	public static void main(String[] args) {
		Terrain landdata = new Terrain();

		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		// 
		landdata.readData(args[0]);
		landdata.genPermute();
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		Water water = new Water(frameX,frameY, landdata.getArray());

		//int numThreads = 4;
		//numThreads.set(0);

		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata, water));
		
		// to do: initialise and start simulation
	}
}
