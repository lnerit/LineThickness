/*
 * Lenz L Nerit
 * Department of Mathematics and Computer Science
 * PNG University of Technology
 * BSCS/4-Final Year Project-2008
 * 
 * Description: This projects was carried out to determine the thickness of lines
 * using Raster Graphics (Pixel).
 * Specifically, the project implements Breshenham's line drawing algorithm
 * to determine the thickness of lines drawn for different shapes
 * on a raster screen
 */
package linethickness;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/***************************************************************************************************/
public class LineThickness extends JFrame implements ItemListener,ActionListener, MouseListener, MouseMotionListener {

   private final int PEN_OP = 1;
	private final int LINE_OP = 2; //some contants to make the code easy to read
	private final int CLEAR_OP = 4;
	private final int RECT_OP = 5;
	private final int OVAL_OP = 3;
	private char action;
	
	private int mousex = 0;
	private int mousey = 0;
	private int prevx = 0;
	private int prevy = 0;
	

	private boolean initialLine = true;
	private boolean initialRect = true;
	private boolean initialOval = true;
	
	private int Orx= 0;
	private int Ory= 0;
	private int OrWidth= 0;
	private int OrHeight = 0;
	private int drawX= 0;
	private int drawY = 0;
	
	private int opStatus = PEN_OP;

	private JButton lineButton = new JButton("Line");
	private JButton eraserButton = new JButton("Eraser");
	private JButton clearButton = new JButton("Clear");
	private JButton rectButton = new JButton("Rectangle");
	private JButton ovalButton = new JButton("Oval");
	private JButton freehandButton = new JButton("Freehand");
	private JButton bgColor = new JButton("Background Color");
	private JComboBox combo = new JComboBox();
	private JComboBox color = new JComboBox();
	private Choice choice = new Choice();
	private JTextField opStatusBar = new JTextField(20);
	private JLabel mouseStatusbar = new JLabel();
	private JLabel label = new JLabel();
	private JPanel controlPanel = new JPanel(new GridLayout(18, 1, 0, 0));
	private JPanel drawPanel = new JPanel();
	private Container container;
	
  //main method
/*...........................................................................................*/
	
	public static void main(String[] args) 
	{
		JFrame window = new JFrame("Final Year Project"); 
		LineThickness content = new LineThickness();
		content.setSize(1280,800);
		content.setBackground(Color.white);
		content.setVisible(true);
		content.addWindowListener(new WindowAdapter() 
		{
			public void windowCloseing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	//end of main method

	 float rWidth,rHeight,pixelSize;
	 int centerX,centerY,dGrid=14,maxX,maxY;
	
	//method to initialise the grids to be drawn on the screen
	void initgr()
	{
		Dimension d;
		d=getSize();//supplies us with number of pixels on the  horizontal and vertivcal lines of the canvas
		maxX=d.width-1;
		maxY=d.height-1;
		pixelSize=Math.max(rWidth/maxX,rHeight/maxY);//specifies pixel size on the grid
		centerX=maxX/2;centerY=maxY/2;
	}
	
	public LineThickness() {
		super("CS402-Final Year Project-Thickness of Lines and Curves of a given Pixel");
		
		container = getContentPane();
		container.setBackground(Color.black);
		setResizable(true);
		container.setLayout(new BorderLayout());
		controlPanel.setBackground(Color.white);
		drawPanel.setBackground(Color.white);
		container.add(controlPanel, "East");
		container.add(drawPanel, "Center");
		//adds itemListener events to the items
		choice.addItemListener(this);
		lineButton.addActionListener(this);
		clearButton.addActionListener(this);
		rectButton.addActionListener(this);
		ovalButton.addActionListener(this);
		freehandButton.addActionListener(this);
		bgColor.addActionListener(this);
		combo.addActionListener(this);
		drawPanel.addMouseMotionListener(this);
		drawPanel.addMouseListener(this);
		drawPanel.setPreferredSize(new Dimension(1000,1000));
		addMouseListener(this);
		addMouseMotionListener(this);
		menu();
		button();
		repaint();
	}

   public void menu()
	{
	   // Add Menubar and respective menu's
		MenuBar mb = new MenuBar();
		Menu file = new Menu("File");
		file.add(new MenuItem("Draw Grid Lines"));
		file.add(new MenuItem("-"));
		file.add(new MenuItem("Exit"));
		mb.add(file);		// Add File menu to the menubar
		Menu mnuEdit = new Menu("Edit");
		mnuEdit.add(new MenuItem("Clear"));
		mb.add(mnuEdit);		// Add mnuEdit menu to the menubar
		Menu tool = new Menu("Tools");
	
		tool.add(new MenuItem("Freehand Drawing"));
		tool.add(new MenuItem("Line"));
		tool.add(new MenuItem("Circle"));
		tool.add(new MenuItem("Rectangle"));
		mb.add(tool);		// Add tool menu to the menubar
		Menu helpmenu = new Menu("Help");
		mb.add(helpmenu);	// Add Help menu to the menubar
		mb.setHelpMenu(helpmenu);	
		setMenuBar(mb);   
	}// End of menu()	

	public void button()
	{
		Panel buttonBar = new Panel();       // A button panel to hold the controls (i.e. the toolbar for the buttons)
      add(buttonBar, BorderLayout.NORTH);
      
      buttonBar.setBackground(Color.lightGray);
      buttonBar.setLayout(new FlowLayout(FlowLayout.LEFT,2,5));
      //String array to populate the combo box to specify the thickness
  		String thickness[]={"1","2","3","4","5","6","7","8","9","10",
		  						  "11","12","13","14","15","16","17","18","19","20",
								  "21","22","23","24","25","26","27","28","29","30",
								  "31","32","33","34","35","36","37","38","39","40",
								  "41","43","43","44","45","46","47","48","49","50",
								  "51","52","53","54","55","56","57","58","59","60",
								  "61","62","63","64","65","66","67","68","69","70",
								  "71","72","73","74","75","76","77","78","79","80",
								  "81","82","83","84","85","86","87","88","89","90",
								  "91","92","93","94","95","96","97","98","100"};
								  
    //Array of colors
     String paintColor[]={"Black","White","Red","Green","Blue","Orange","Pink","Gray","Light Gray",
     							  "Cyan","Purple","Custom Color"};
    
    	label = new JLabel("Pixel");
      combo = new JComboBox(thickness);//populates the array contents in the combobox
      color = new JComboBox(paintColor);
		
		buttonBar.add(freehandButton);
		buttonBar.add(lineButton);
		buttonBar.add(ovalButton);
		buttonBar.add(rectButton);
		buttonBar.add(clearButton);
		buttonBar.add(bgColor);
		buttonBar.add(label);
		buttonBar.add(combo);
		buttonBar.add(color);
		repaint();
	}
	/*******************************************************************************************************/
	//method to draw grid on the frame
	void showGrid(Graphics g)
		{	//int grid = Integer.parseInt((String) combo.getSelectedItem());
			int grid=14;
			g.setColor(Color.lightGray);	
			for(int x=dGrid;x<=maxX;x+=dGrid)//draws the rows of grid
				for(int y=dGrid;y<=maxY;y+=dGrid)//draws the columns
					g.drawRect(x,y,x,y);//draws the rectangular grids on the panel
							
		}
//method to put pixel by drawing a square shape for the given pixel point
	void putPixel(Graphics g,int x,int y)
	{  
		int x1=x*dGrid,y1=y*dGrid,h=dGrid/2;
		g.drawRect(x1-h,y1-h,dGrid,dGrid);//traces the grid lines of given pixel specified by the user
		g.fillOval(x1-h,y1-h,dGrid,dGrid);//a selected raster point along the line is drawn
				
	}
//This is the implememtation of Bresenham line drawing algorithm
//Given two points (xP,yP0 and (xQ,yQ)
	public void drawLine(Graphics g,int xP,int yP,int xQ,int yQ)
	{ 
		int x=xP;
		int y=yP;
		int D=0;
		int dx=xQ-xP;//change in x value
		int dy=yQ-yP;//change in y
		int c,M;//
		int xInc=1;//variable to determine the increment value in the x axis
		int yInc=1;//variable to determine the increment value in the y axis
		if(dx<0)//change in x values
		{
			xInc=-1;//increment x by -1
			dx=-dx;//the difference in the x variable is assigned a negative number
		}
		if(dy<0)
		{
			yInc=-1;//increment y by -1
			dy=-dy;//since dy is less than 0,dy becomes negative
		}
		if(dy<=dx)
		{
			c=2*dx;//converts change in dx to an integer value
			M=2*dy;//converts change in dy to an integer value
			for(;;)//an empty loop to loop through as long as the statement dy<=dx is true
			{ 
				putPixel(g,x,y);
				if(x==xQ) break;//jumps out of the loop
				x+=xInc;//increments x by adding xInc each time through the loop
				D+=M;
				if(D>dx)
				{
					y+=yInc;//increments y by adding yInc each time through the loop
					D-=c;//decrease the value of variable D by subtracting c from D each time as long as D>dx
				}
			}
		}
		else
		{
			c=2*dy;//converts change in dx to an integer value
			M=2*dx;//converts change in dy to an integer value

			for(;;)//an empty loop to loop through as long as the statement dy<=dx is true
			{  
				putPixel(g,x,y);
				if(y==yQ) break;
				y+=yInc;
				D+=M;
				if(D>dy)
				{
					x+=xInc;
					D -= c;
				}

			}
		}
	}
	//method to draw circle on the grid
	public void drawCircle(Graphics g,int xCenter,int yCenter,int r)
		{ 
			int x=0,y=r,u=1,v=2*r-1,E=0;
			while(x<y)
			{  
				putPixel(g,xCenter+x,yCenter+y);//put pixel in the 2nd octant 
				putPixel(g,xCenter+y,yCenter-x);//select pixel in the 8th octant 
				putPixel(g,xCenter-x,yCenter-y);//selects pixel in the 6th octant 
				putPixel(g,xCenter-y,yCenter+x);// selected pixel in the 4th octant 
				x++;//increments the x value
				E+=u;
				u+=2;
				if(v<2*E)
				{
					y--;//decrements the y value
					E-=v;//decrement the value of E each time with the value of v
					v-=2;
				}
				if (x>y) break;
				putPixel(g,xCenter+y,yCenter+x);//puts pixel in the 1st octant 
				putPixel(g,xCenter+x,yCenter-y);//puts pixel in the 7th octant
				putPixel(g,xCenter-y,yCenter-x);//puts pixel in the 5th octant
				putPixel(g,xCenter-x,yCenter+y);//puts pixel in the 3rd octant
			}
		
		}
/************************************************************************************************************/
	//method to determine which action to perform when the user selects the the command buttons on the tool bar
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		if (e.getActionCommand() == "Line")
			opStatus = LINE_OP;
		if (command.equals("Free Hand Drawing")|| (command.equals("Freehand")))
		 	opStatus = PEN_OP;
		if (e.getActionCommand() == "Clear")
			opStatus = CLEAR_OP;
		if (e.getActionCommand() == "Rectangle")
			opStatus = RECT_OP;
		if (e.getActionCommand() == "Oval")
			opStatus = OVAL_OP;
	
		switch (opStatus) 
		{
		case CLEAR_OP:
			clearPanel(drawPanel);
		}	
	}
	//method to clear the panel of any images drawn on it
	public void clearPanel(JPanel p) 
	{
		opStatusBar.setText("Clear");
		Graphics g = p.getGraphics();
		g.setColor(p.getBackground());
		g.fillRect(0, 0, p.getBounds().width, p.getBounds().height);//clears the entire panel for new images to be drawn
	}
	//method to carry out the line operation done by the mouse event
	public void lineOperation(MouseEvent e) 
	{
		Graphics g = drawPanel.getGraphics();
		int pixel = Integer.parseInt((String) combo.getSelectedItem()); //converts the the number selected from the combo box to integer
		Graphics2D g2d = (Graphics2D)g;
		if (initialLine) 
		{
			setGraphicalDefaults(e);//loads default values for the graphical displays
			initialLine = false;//no initial line drawn
		}
		if (mouseHasMoved(e)) {
			g.setXORMode(colorSelection(g));
			
			drawLine(g,Orx,Ory,mousex,mousey);//draws
			mousex = e.getX();//updates the mouse cordinates
			mousey = e.getY();
			g.setXORMode(colorSelection(g));
			drawLine(g,Orx, Ory,mousex,mousey);		
			//repaint();	
		}
	
	}
	//method to carry out the rectangle operation
	public void rectOperation(MouseEvent e) 
	{
		Graphics g = drawPanel.getGraphics();
		if (initialRect) 
		{
			setGraphicalDefaults(e);
			initialRect = false;
		}
		if (mouseHasMoved(e)) 
		{
			g.setXORMode(drawPanel.getBackground());
			colorSelection(g);
			g.drawRect(drawX, drawY, OrWidth, OrHeight);
			mousex = e.getX();
			mousey = e.getY();
			setActualBoundry();
			g.drawRect(drawX, drawY, OrWidth, OrHeight);
		}
		//repaint();
	}
	//method to carry out the oval operation which is done by the mouseEvent
	public void ovalOperation(MouseEvent e) 
	{
		Graphics g = drawPanel.getGraphics();
		
	
		if (initialOval)
		{
			setGraphicalDefaults(e);//loads initial values
			initialOval = false;
		}
		if (mouseHasMoved(e)) 
		{
			g.setXORMode(colorSelection(g));
				drawCircle(g,drawX, drawY, OrWidth);//draws the enlarged circle
				g.drawOval(drawX, drawY, OrWidth, OrHeight);//draws default circle 
				mousex = e.getX();//updates the mouse coordinates
				mousey = e.getY();
				setActualBoundry();
				g.drawOval(drawX, drawY, OrWidth, OrHeight);//updated circle is drawn
				drawCircle(g,drawX, drawY, OrWidth);
				repaint();
		}
		
	}
	
	public void freeHandOperation(MouseEvent e)
	{  
		Graphics g = drawPanel.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		colorSelection(g);//calls the colorSelection method to set color for drawing
      mousex = e.getX();//updates the mouse coordinates
      mousey = e.getY();
      g2d.drawOval( mousex, mousey,setThickness(g2d),setThickness(g2d) );
      repaint();
	}
   
   //method to set thickness of lines and curves drawn on the frame
	public int setThickness(Graphics2D g)
	{
			int size = Integer.parseInt((String) combo.getSelectedItem());//converts the string value in combobox to integer
			g.setStroke(new BasicStroke(size));//sets the thickness when the user selects a value from the combobox
			return size;	//returns an integer
	}
	
	// method to set color for drawing when the user selects a color from the commbobox
	public Color colorSelection(Graphics g)
	{
		Color strColor = new Color(255,255,255);
		String selectColor = (String) color.getSelectedItem();//converts the value in the combobox to string value
		if(selectColor.equals("Black"))
			g.setColor(Color.black);
		if(selectColor.equals("White"))
			g.setColor(Color.white);
		if(selectColor.equals("Red"))
			g.setColor(Color.red);
		if(selectColor.equals("Green"))
			g.setColor(Color.green);
		if(selectColor.equals("Orange"))
			g.setColor(Color.orange);
		if(selectColor.equals("Blue"))
			g.setColor(Color.blue);
		if(selectColor.equals("Pink"))
			g.setColor(Color.pink);
		if(selectColor.equals("Gray"))
			g.setColor(Color.GRAY);
		if(selectColor.equals("Light Gray"))
			g.setColor(Color.lightGray);
		if(selectColor.equals("Cyan"))
			g.setColor(Color.cyan);
		if(selectColor.equals("Yellow"))
			g.setColor(Color.yellow);
		return strColor;//returns color
		
	}
	//Mouse Operations
	/********************************************************************************************/
	//this actions are invoked when the user clicks on the menu items for the MenuBar
	public boolean action( Event e, Object arg)
	{
		Graphics g = drawPanel.getGraphics();
		String label = (String)arg;
		if (e.target instanceof MenuItem)
		{
			if (label.equals("Exit"))
			 {
			 	doExit();
			 }
			else if (label.equals("Draw Grid Lines"))
			{
				showGrid(g);
			}
			else if (label.equals("Freehand Drawing"))
			{
				opStatus = PEN_OP;
			}
			else if (label.equals("Line"))
			{
				opStatus = LINE_OP;
			}
			else if (label.equals("Rectangle"))
			{
				opStatus = RECT_OP;
			}
			else if (label.equals("Circle"))
			{
				opStatus  = OVAL_OP;
			}
			else if (label.equals("Clear"))
			{
				clearPanel(drawPanel);
			}
		return true;
		}
		else return false;  	
	}// End of action
	
	void doExit()
	{
		//Prompt the user before terminating the program
		JOptionPane.showMessageDialog(null,"Thankyou for using the trial paint program.Programmed by Bata Lenz L Nerit-Version 1.0");
		System.exit(0);
	}// End of doExit()

	
	public void itemStateChanged(ItemEvent e){
	
	}
	//method to determine whether the mouse has moved
	public boolean mouseHasMoved(MouseEvent e) 
	{
		return (mousex != e.getX() || mousey != e.getY());
	}
	//method to set boundry for the images drawn on the panel
	public void setActualBoundry() 
	{
		if (mousex < Orx || mousey < Ory) 
		{
			if (mousex < Orx) 
			{
				OrWidth = Orx - mousex;
				drawX = Orx - OrWidth;
			} 
			else 
			{
				drawX = Orx;
				OrWidth = mousex - Orx;
			}
			if (mousey < Ory) 
			{
				OrHeight = Ory - mousey;
				drawY = Ory - OrHeight;
			}
			else 
			{
				drawY = Ory;
				OrHeight = mousey - Ory;
			}
		}
		else 
		{
			drawX = Orx;
			drawY = Ory;
			OrWidth = mousex - Orx;
			OrHeight = mousey - Ory;
		}
	}
	
	//method to set default values for the mouse coordinates
	//ie the variables are initialised
	public void setGraphicalDefaults(MouseEvent e)
	{
		mousex = e.getX();
		mousey = e.getY();
		prevx = e.getX();
		prevy = e.getY();
		Orx = e.getX();
		Ory = e.getY();
		drawX = e.getX();
		drawY = e.getY();
		OrWidth = 0;
		OrHeight = 0;
	}
	
	//In the event of dragging the mouse,the specific case is executed
	public void mouseDragged(MouseEvent e)
	{
		Graphics g = drawPanel.getGraphics();
		updateMouseCoordinates(e);
		switch (opStatus)
		{
		case PEN_OP:
			freeHandOperation(e);
		break;
		case LINE_OP:
			lineOperation(e);
			break;
		case RECT_OP:
			rectOperation(e);
			break;
		case OVAL_OP:
			ovalOperation(e);
			break;
		}
		
	}
	//In the event of releasing the mouse,the method (operation) specified in each case is executed
	//i.e.the images are released onto the panel
	public void mouseReleased(MouseEvent e)
	{
		Graphics g = drawPanel.getGraphics();
		updateMouseCoordinates(e);
		switch (opStatus) {
		case PEN_OP:
			freeHandOperation(e);
			repaint();
			break;
		case LINE_OP:
			releasedLine();
			repaint();
			break;
		case RECT_OP:
			releasedRect();
			repaint();
			break;
		case OVAL_OP:
			releasedOval();
			repaint();
			break;
		}
	}
	
	public void releasedLine()
	{
		boolean right=true;
		Graphics g = drawPanel.getGraphics();
		int pixel = Integer.parseInt((String) combo.getSelectedItem()); 
		if ((Math.abs(Orx - mousex) + Math.abs(Ory - mousey)) != 0)
		{ 
			initialLine = true;
			Graphics2D g2d = (Graphics2D)g;
			colorSelection(g);
			
			//drawLine(g,Orx, Ory, mousex, mousey);
			
			if (Math.abs(Orx-mousex)!=0)//if the difference between the y-coordinates is not zero then the pixels are set along the x-axis
			{	
				for(int i=1;i<=pixel;i++)//
				{ 
					drawLine(g,Orx, Ory+pixel-i, mousex, mousey+pixel-i);	//draws the line with a given thickness(pixel)and step along the x-axis 
					
				}
			}
			else
			{	for(int j=1;j<=pixel;j++)//for a given pixel,the grid lines are traced again to determine the thicness on the panel
				{	
					drawLine(g,Orx+pixel-j, Ory, mousex+pixel-j, mousey);//draws a line with  a given thickness stepping along the y-axis on the grid
					
				}
			}
			setThickness(g2d);//sets thickness for the line from the pixel value selected from the combo box on the interface
			g.drawLine(Orx, Ory,mousex,mousey);
		}
		
		repaint();
	}
	//method to draw the complete rectangle upon the release of the mouse
	public void releasedRect()
	{
		int pixel = Integer.parseInt((String) combo.getSelectedItem());
		initialRect = true;
		Graphics g = drawPanel.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		colorSelection(g);
		setThickness(g2d);
		g.drawRect(drawX, drawY, OrWidth, OrHeight);
		repaint();
	}
	//method to draw the complete oval upon the release of the mouse
	public void releasedOval()
	{
		int pixel = Integer.parseInt((String) combo.getSelectedItem());
		initialOval = true;
		Graphics g = drawPanel.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		colorSelection(g);
		
		for(int i=1;i<=pixel;i++)//draws thick grid lines for a given pixel on the panel
		{
			drawCircle(g,drawX, drawY,OrWidth+pixel-i );
		}
		setThickness(g2d);//sets thickness for the circle for pixel value selected from the combobox on the interface
		g.drawOval(drawX, drawY, OrWidth, OrHeight);
		repaint();
	}
	
  public void BgroundColor()
  {
  		Graphics g = drawPanel.getGraphics();
  		drawPanel = new JPanel();
 	 	drawPanel.setBackground(Color.black);
  }
  
  //method to update coordinates of the mouse
	public void updateMouseCoordinates(MouseEvent e)
	{
		String xCoor = "";//initialised to an empty string
		String yCoor = "";//initialised to an empty string
		if (e.getX() < 0)
			xCoor = "0";
		else
		 {
			xCoor = String.valueOf(e.getX());
		 }
		if (e.getY() < 0)
			xCoor = "0";
		else 
		{
			yCoor = String.valueOf(e.getY());//y coordinate value is converted to string and assigned to the variable yCoor
		}
		getContentPane().setBackground(Color.red);
		mouseStatusbar.setText("x:" + xCoor + "   y:" + yCoor);
	}
	
	//method to update the mouse coordinates when the mouse enters the drawPanel.
	public void mouseEntered(MouseEvent e)
	{
		updateMouseCoordinates(e);
	}
	//In the event of mouse clicck,the coordinates are updates
	public void mouseClicked(MouseEvent e)
   {
		updateMouseCoordinates(e);
	}
	//In the event of mouse exit,the coodinates are updated
	public void mouseExited(MouseEvent e) 
	{
		updateMouseCoordinates(e);
	}
	//In the event of mouse moved,the coodinates are updated
	public void mouseMoved(MouseEvent e)
	{
		updateMouseCoordinates(e);
		repaint();
	}
	//In the event of mouse pressed,the coodinates are updated
	public void mousePressed(MouseEvent e)
	{
		updateMouseCoordinates(e);
	}	
	//paints components on the panel
	
	public void paint(Graphics g)
	{
		initgr();
		
	}	
}
