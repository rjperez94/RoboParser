package mainProgram;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;


public class RoboGame extends JFrame {
    // Load required assets using a JFileChooser
    private static JFileChooser fileChooser = new JFileChooser();
    private static final String FUEL = "fuel.png";
    private static File ASSETS_DIR;

    private static WorldComponent worldComp;
    private File code1, code2;

    public static boolean debugDisplay = true;


    public RoboGame() {
        super("Robots");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        add(worldComp, BorderLayout.CENTER);

        createMenu();
        pack();

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private static void chooseDir() {
        File test = null;

        // set up the file chooser
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Select asset directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // run the file chooser and check the user didn't hit cancel
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            // get the files in the selected directory and match them to
            // the files we need.
            File directory = fileChooser.getSelectedFile();
            File[] files = directory.listFiles();

            for (File f : files) {
                if (f.getName().equals(FUEL)) {
                    test = f;
                }
            }

            // check none of the files are missing, and call the load
            // method in your code.
            if (test == null) {
                JOptionPane.showMessageDialog(null, "Directory does not contain correct files", "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } else {
                ASSETS_DIR = new File(test.getParent());    //  Set assets directory
            }
        }
    }

    private void createMenu() {
        JMenuBar menu = new JMenuBar();

        final JMenu loadMenu = new JMenu("Load Program");
        final JMenu debugMenu = new JMenu("Debug ");
        final JMenuItem load1 = new JMenuItem("Robot 1");
        final JMenuItem load2 = new JMenuItem("Robot 2");
        final JMenuItem start = new JMenuItem("Start");
        final JMenuItem reset = new JMenuItem("Reset");
        final JMenuItem debugOn = new JMenuItem("On");
        final JMenuItem debugOff = new JMenuItem("Off");
        JMenuItem quit = new JMenuItem("Quit");

        menu.add(loadMenu);
        loadMenu.add(load1);
        loadMenu.add(load2);
        menu.add(start);
        menu.add(reset);
        menu.add(debugMenu);
        debugMenu.add(debugOn);
        debugMenu.add(debugOff);
        menu.add(quit);
        setJMenuBar(menu);

        // Add listeners to menu items.

        load1.addActionListener(e -> {
            code1 = getCodeFile();
            if (code1 != null) {
                worldComp.loadRobotProgram(1, code1);
                worldComp.repaint();
            }
        });

        load2.addActionListener(e -> {
            code2 = getCodeFile();
            if (code2 != null) {
                worldComp.loadRobotProgram(2, code2);
                worldComp.repaint();
            }
        });

        start.addActionListener(e -> {
            load1.setEnabled(false);
            load2.setEnabled(false);
            start.setEnabled(false);
            worldComp.start();
        });

        reset.addActionListener(e -> {
            worldComp.reset();
            if (code1 != null) {
                worldComp.loadRobotProgram(1, code1);
            }
            if (code2 != null) {
                worldComp.loadRobotProgram(2, code2);
            }
            worldComp.repaint();
            load1.setEnabled(true);
            load2.setEnabled(true);
            start.setEnabled(true);
        });

        debugOn.addActionListener(e -> debugDisplay = true);

        debugOff.addActionListener(e -> debugDisplay = false);

        quit.addActionListener(e -> System.exit(0));
		
	/*
	} catch (FileNotFoundException e) {e.printStackTrace();}
	JMenuItem startSimple = new JMenuItem("Simple Interpreter");
	JMenuItem startIntermediate = new JMenuItem("Intermediate Interpreter");
	JMenuItem startAdvanced = new JMenuItem("Advanced Interpreter");
	start.add(startSimple);
	start.add(startIntermediate);
	start.add(startAdvanced);
	startSimple.addActionListener(new ActionListener() {@Override
		public void actionPerformed(ActionEvent e) {
		    load1.setEnabled(false);
		    load2.setEnabled(false);
		    start.setEnabled(false);
		    worldComp.start(World.INTERPRETER_SIMPLE);
		}
	    });

	startIntermediate.addActionListener(new ActionListener() {@Override
		public void actionPerformed(ActionEvent e) {
		    load1.setEnabled(false);
		    load2.setEnabled(false);
		    start.setEnabled(false);
		    worldComp.start(World.INTERPRETER_INTERMEDIATE);
		}
	    });
	startAdvanced.addActionListener(new ActionListener() {@Override
		public void actionPerformed(ActionEvent e) {
		    load1.setEnabled(false);
		    load2.setEnabled(false);
		    start.setEnabled(false);
		    worldComp.start(World.INTERPRETER_ADVANCED);
		}
	    });
	*/
    }


    public File getCodeFile() {
        JFileChooser chooser = new JFileChooser(".");//System.getProperty("user.dir"));
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public static void main(String[] args) {
        chooseDir();

        worldComp = new WorldComponent(ASSETS_DIR.toPath());

        new RoboGame();
    }
}
