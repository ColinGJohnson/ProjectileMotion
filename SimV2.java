import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SimV2 {
	private JFrame frmPhysicsSimulatorV;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimV2 window = new SimV2();
					window.frmPhysicsSimulatorV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SimV2() {
		initialize();
	}

	/**
	 * Initializes all GUI elements.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		} catch (Exception e) {
		}

		frmPhysicsSimulatorV = new JFrame();
		frmPhysicsSimulatorV.setTitle("Projectile Motion");
		frmPhysicsSimulatorV.setBounds(100, 100, 800, 800);
		frmPhysicsSimulatorV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPhysicsSimulatorV.getContentPane().setLayout(null);

		JPanel displayArea = new JPanel();
		displayArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		displayArea.setBounds(10, 11, 764, 533);
		frmPhysicsSimulatorV.getContentPane().add(displayArea);
		displayArea.setLayout(null);
		AnimationDisplayPanel animationDisplay = new AnimationDisplayPanel();
		animationDisplay.setBounds(7, 8, 750, 517);
		displayArea.add(animationDisplay);

		JPanel UI = new JPanel();
		UI.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		UI.setBounds(10, 565, 764, 141);
		frmPhysicsSimulatorV.getContentPane().add(UI);
		UI.setLayout(null);

		JButton startBtn = new JButton("Start");
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				animationDisplay.animationRunning = true;
				animationDisplay.animationPaused = false;
			}
		});

		startBtn.setBounds(48, 11, 190, 20);
		UI.add(startBtn);
		startBtn.setToolTipText("Start Animation Sequence");

		JButton pauseBtn = new JButton("Pause");
		pauseBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				animationDisplay.animationPaused = true;
			}
		});
		pauseBtn.setBounds(286, 11, 190, 20);
		UI.add(pauseBtn);

		JPanel angleControlPanel = new JPanel();
		angleControlPanel.setBounds(548, 42, 200, 80);
		UI.add(angleControlPanel);
		angleControlPanel.setLayout(null);

		JLabel lblAngle = new JLabel("Angle:");
		lblAngle.setBounds(50, 0, 31, 14);
		angleControlPanel.add(lblAngle);
		lblAngle.setHorizontalAlignment(SwingConstants.CENTER);

		JSlider angleSlider = new JSlider();
		angleSlider.setPaintLabels(true);
		angleSlider.setBounds(0, 25, 200, 45);
		angleControlPanel.add(angleSlider);
		angleSlider.setPaintTicks(true);
		angleSlider.setMinorTickSpacing(45);
		angleSlider.setMaximum(360);
		angleSlider.setValue(320);
		angleSlider.setMajorTickSpacing(90);
		angleSlider.setValue(320);

		JLabel angleDisplayLabel = new JLabel("" + animationDisplay.Angle + "\u00b0");
		angleDisplayLabel.setBounds(90, 0, 46, 14);
		angleControlPanel.add(angleDisplayLabel);

		angleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				animationDisplay.Angle = angleSlider.getValue();
				angleDisplayLabel.setText("" + angleSlider.getValue() + "\u00b0");
			}
		});

		JPanel ViControlPanel = new JPanel();
		ViControlPanel.setBounds(12, 42, 200, 80);
		UI.add(ViControlPanel);
		ViControlPanel.setLayout(null);

		JLabel ViLabel = new JLabel("Vi:");
		ViLabel.setBounds(22, 0, 44, 14);
		ViControlPanel.add(ViLabel);
		ViLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel ViDisplayLabel = new JLabel("" + animationDisplay.Vi + " (px/loop)");
		ViDisplayLabel.setBounds(70, 0, 69, 14);
		ViControlPanel.add(ViDisplayLabel);

		JSlider ViSlider = new JSlider();
		ViSlider.setValue(5);
		ViSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				animationDisplay.Vi = ViSlider.getValue();
				ViDisplayLabel.setText("" + ViSlider.getValue() + " (px/loop)");
			}
		});

		ViSlider.setToolTipText("");
		ViSlider.setBounds(0, 25, 200, 45);
		ViControlPanel.add(ViSlider);
		ViSlider.setPaintTicks(true);
		ViSlider.setPaintLabels(true);
		ViSlider.setMinorTickSpacing(1);
		ViSlider.setMaximum(25);
		ViSlider.setMajorTickSpacing(5);

		JPanel readoutPanel = new JPanel();
		readoutPanel.setBounds(224, 42, 150, 80);
		UI.add(readoutPanel);
		readoutPanel.setLayout(null);

		JLabel lblVx = new JLabel("Vx:");
		lblVx.setBounds(10, 0, 30, 15);
		readoutPanel.add(lblVx);

		JLabel lblVy = new JLabel("Vy:");
		lblVy.setBounds(10, 15, 30, 15);
		readoutPanel.add(lblVy);

		JLabel lblvnet = new JLabel("Vnet:");
		lblvnet.setBounds(10, 30, 30, 15);
		readoutPanel.add(lblvnet);

		JLabel lblAx = new JLabel("Ax:");
		lblAx.setBounds(10, 45, 30, 15);
		readoutPanel.add(lblAx);

		JLabel lblAy = new JLabel("Ay:");
		lblAy.setBounds(10, 60, 30, 15);
		readoutPanel.add(lblAy);

		JLabel VxDisplay = new JLabel("N/A");
		VxDisplay.setBounds(45, 0, 95, 15);
		readoutPanel.add(VxDisplay);

		JLabel VyDisplay = new JLabel("N/A");
		VyDisplay.setBounds(45, 15, 95, 15);
		readoutPanel.add(VyDisplay);

		JLabel VnetDisplay = new JLabel("N/A");
		VnetDisplay.setBounds(45, 30, 95, 15);
		readoutPanel.add(VnetDisplay);

		JLabel AxDisplay = new JLabel("N/A");
		AxDisplay.setBounds(45, 45, 95, 15);
		readoutPanel.add(AxDisplay);

		JLabel AyDisplay = new JLabel("N/A");
		AyDisplay.setBounds(45, 60, 95, 15);
		readoutPanel.add(AyDisplay);

		JButton resetBtn = new JButton("Reset");
		resetBtn.setBounds(524, 10, 190, 20);
		UI.add(resetBtn);
		
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				animationDisplay.reset();
				animationDisplay.animationPaused = true;
				animationDisplay.animationRunning = false;
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(386, 42, 150, 80);
		UI.add(panel);
		
		JCheckBox chckbxDisplayBallPath = new JCheckBox("display ball path");
		chckbxDisplayBallPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				animationDisplay.displayBallPath = !animationDisplay.displayBallPath;
			}
		});
		chckbxDisplayBallPath.setSelected(true);
		chckbxDisplayBallPath.setBounds(6, 0, 138, 23);
		panel.add(chckbxDisplayBallPath);
		
		JCheckBox chckbxDisplayAngle = new JCheckBox("display angle");
		chckbxDisplayAngle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				animationDisplay.displayAngle = !animationDisplay.displayAngle;
			}
		});
		chckbxDisplayAngle.setSelected(true);
		chckbxDisplayAngle.setBounds(6, 25, 138, 23);
		panel.add(chckbxDisplayAngle);
		
		JCheckBox chckbxBouncy = new JCheckBox("bouncy");
		chckbxBouncy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				animationDisplay.bouncy = !animationDisplay.bouncy;
			}
		});
		chckbxBouncy.setSelected(true);
		chckbxBouncy.setBounds(6, 51, 138, 23);
		panel.add(chckbxBouncy);

		JLabel Watermark = new JLabel("Colin Johnson, 2015");
		Watermark.setForeground(new Color(150, 150, 150));
		Watermark.setBounds(670, 715, 97, 14);
		frmPhysicsSimulatorV.getContentPane().add(Watermark);

		JMenuBar menuBar = new JMenuBar();
		frmPhysicsSimulatorV.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Open Help");
		mntmHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JOptionPane.showMessageDialog(frmPhysicsSimulatorV,
						"Control initial angle and velocity using lower sliders or by typing a value into the boxes,"
								+ "\nClick the buttons to start and pause the simulation." + "\n\n"
								+ "<html><i>This application was created by Colin Johnson in 2015.</i></html>",
						"Simulator Help", JOptionPane.QUESTION_MESSAGE);
			}
		});
		mnHelp.add(mntmHelp);

		// add a timer to update jLabels
		int delay = 1000/60; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				VxDisplay.setText(String.valueOf(animationDisplay.Vx));
				VyDisplay.setText(String.valueOf(-animationDisplay.Vy));
				VnetDisplay.setText(String.valueOf(animationDisplay.Vnet));
				AxDisplay.setText(String.valueOf(animationDisplay.Ax));
				AyDisplay.setText(String.valueOf(animationDisplay.Ay));		
			}
		};
		new Timer(delay, taskPerformer).start();
	} // initialize
} // SimV2
