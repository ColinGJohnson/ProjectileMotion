import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationDisplayPanel extends JPanel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private int baselineX = 740;
	public boolean animationRunning = false;
	public boolean animationPaused = false;
	public boolean animationReset = false;

	public int startPosX = 20;
	public int startPosY = 450;

	ArrayList<Integer> pathArrayX = new ArrayList<Integer>();
	ArrayList<Integer> pathArrayY = new ArrayList<Integer>();

	public double x; // current x pos. of ball
	public double y; // current y pos. of ball
	public double Dx; // total x displacement from initial position
	public double Dy; // total y displacement from initial position
	public double Vi = 5; // user input velocity
	public double Vnet = 0;
	public double Vx = 0;
	public double Vy = 0;
	public double Ax = 0; // Acceleration in px/s/s
	public double Ay = 15;
	public double Angle = 320;
	public int tps = 60; // logic loops per second
	public double coefficientOfRestutution = 0.8; // the bounciness of the ball
	public double timeRunning = 0;

	// user preferences
	public int ballDiameter = 10;
	public boolean displayBallPath = true;
	public boolean displayAngle = true;
	public boolean bouncy = true;

	/**
	 * Constructs an AnimationDisplayPanel.
	 */
	public AnimationDisplayPanel() {
		super(new FlowLayout(), true);
		setBackground(Color.red);

		// set variables to defaults
		reset();

		// listen to key presses
		setFocusable(true);
		addKeyListener(this);

		// call Animation loop() at correct rate
		Timer timer = new Timer(1000 / tps, this);
		timer.start();
	}

	/**
	 * Resets this animationDisplayPanel to it's default values
	 */
	public void reset() {
		x = startPosX;
		y = startPosY;
		timeRunning = 0;

		// calculate x and y velocities depending on angle
		Vx = Vi * Math.cos(Math.toRadians(Angle));
		Vy = Vi * Math.sin(Math.toRadians(Angle));
		
		// reset ball path
		pathArrayX.clear();
		pathArrayY.clear();
	} // reset

	public void actionPerformed(ActionEvent e) {
		AnimationLoop();
	} // actionPerformed

	public void AnimationLoop() {
		if (animationRunning && !animationPaused) {

			// update logic
			update();
			
			// check collisions
			checkCollisions();
			
			// move ball
			moveBall();
			
		} else if (!animationRunning && !animationPaused) {

			// calculate correct initial values
			reset();
		}

		// repaint JPanel
		repaint();
	} // AnimationLoop

	public void checkCollisions() {
		if (bouncy) {
			if (y + ballDiameter > this.getWidth()) {
				Vy = -Vy * coefficientOfRestutution;
				y = 470 - ballDiameter;
			}
			
			if (x + ballDiameter > 740) {
				Vx = -Vx * coefficientOfRestutution;
				x = 740;
			}
			
			if (x + ballDiameter < 5) {
				Vx = -Vx * coefficientOfRestutution;
				x = 5;
			}
		} else {
			if (y + ballDiameter > 470) {
				Vy = 0;
				Vx = 0;
				y = 470; 
			}
		}
	} // checkCollisions

	public void moveBall() {

		// move ball in x and y directions
		x += Vx;
		y += Vy;
	}

	public void update() {

		// add point to arrayList
		pathArrayX.add((int) x + ballDiameter/2);
		pathArrayY.add((int) y + ballDiameter/2);

		// apply acceleration
		Vx += Ax / tps;
		Vy += Ay / tps;
		
		// recalculate new velocity
		Vnet = Math.sqrt(Math.pow(Vx, 2) + Math.pow(Vy, 2));

		// increment time
		timeRunning++;
	}

	/**
	 * Draws all required graphics onto this AnimationDisplayPanel
	 * @param g The graphics object to be used.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// background
		g.setColor(new Color(250, 250, 250));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// baseline
		g.setColor(Color.black);
		g.drawLine(0, 470, 750, 470);

		// ball
		g.setColor(new Color(10, 10, 200));
		g.fillOval((int) Math.round(x), (int) Math.round(y), ballDiameter, ballDiameter);

		g.setFont(new Font("Tahoma 11", Font.TRUETYPE_FONT, 12));
		g.setColor(Color.BLACK);

		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.CEILING);
		g.drawString("Time: " + (df.format(timeRunning / 60)) + "s", 20, 20);

		// ball path
		if (displayBallPath) {
			for (int i = 0; i < pathArrayX.size(); i++) {
				if (i > 0) {
					g.drawLine(pathArrayX.get(i - 1), pathArrayY.get(i - 1), pathArrayX.get(i), pathArrayY.get(i));
				}
			}
		}

		// angle display
		if (!animationRunning) {
			if (displayAngle) {
				int endPosX = (int) ((startPosX + 60 * Math.cos(Math.toRadians(Angle))) + ballDiameter / 2);
				int endPosY = (int) ((startPosY + 60 * Math.sin(Math.toRadians(Angle))) + ballDiameter / 2);

				// arrowhead
				drawArrow(g, startPosX + ballDiameter / 2, startPosY + ballDiameter / 2, (int) (endPosX),
						(int) (endPosY));

			}
		}
	} // paintComponent

	/**
	 * Draws an arrow between two points
	 * @param g1 The graphics object to be used
	 * @param x1 The first x coordinate.
	 * @param y1 The first y coordinate.
	 * @param x2 The second x coordinate.
	 * @param y2 The second x coordinate.
	 */
	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		int ARR_SIZE = 4;
		Graphics2D g = (Graphics2D) g1.create();
		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 },4);
	} // drawArrow

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
} // AnimationDisplayPanel
