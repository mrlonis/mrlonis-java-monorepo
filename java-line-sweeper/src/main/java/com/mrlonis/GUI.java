package com.mrlonis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * [read-only]
 */

class GUI extends JFrame {
    /**
     * Added default serial ID to remove Eclipse warning.
     */
    private static final long serialVersionUID = 1L;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private LineSegment sweepLine = null;
    private List<LineSegment> world = new ArrayList<>();
    private boolean showDetails;

    public GUI(int treeType) {
        setTitle(Constants.TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> {
            save(world);
            repaint();
        });
        fileMenu.add(save);
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(e -> {
            load(world);
            repaint();
        });
        fileMenu.add(load);
        setJMenuBar(menuBar);

        JPanel main = new JPanel() {
            /**
             * Added default serial ID to remove Eclipse warning.
             */
            private static final long serialVersionUID = 1L;

            {
                setLayout(new BorderLayout());
                setBackground(Color.WHITE);
            }
        };

        JComponent drawingSurface = new JComponent() {
            /**
             * Added default serial ID to remove Eclipse warning.
             */
            private static final long serialVersionUID = 1L;

            private LineSegment currentSeg;

            {
                setOpaque(true);
                setPreferredSize(new Dimension(Constants.SURFACE_WIDTH, Constants.SURFACE_HEIGHT));
                MouseAdapter mouseAdapter = new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        currentSeg = new LineSegment(e.getPoint());
                        repaint();
                    }

                    public void mouseDragged(MouseEvent e) {
                        currentSeg.setP2(e.getPoint());
                        repaint();
                    }

                    public void mouseReleased(MouseEvent e) {
                        if (currentSeg.isZeroLength()) {
                            // treat zero length segments as a click and remove
                            // the possibly selected lines
                            for (int i = world.size() - 1; i >= 0; i--) {
                                LineSegment seg = world.get(i);
                                if (seg.intersects(e.getPoint())) {
                                    world.remove(i);
                                }
                            }
                        } else {
                            currentSeg.fix();
                            world.add(currentSeg);
                        }
                        currentSeg = null;
                        repaint();
                    }
                };
                addMouseListener(mouseAdapter);
                addMouseMotionListener(mouseAdapter);
            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (showDetails && sweepLine != null) {
                    sweepLine.draw(g2d, showDetails);
                }
                for (LineSegment seg : world) {
                    seg.draw(g2d, showDetails);
                }
                if (currentSeg != null) {
                    currentSeg.draw(g2d, showDetails);
                }
            }
        };

        JPanel controls = new JPanel();
        JButton sweep = new JButton("Sweep");
        JButton reset = new JButton("Reset");
        JButton clear = new JButton("Clear");
        JButton quit = new JButton("Quit");
        JCheckBox details = new JCheckBox("Details");

        controls.add(sweep);
        controls.add(reset);
        controls.add(clear);
        controls.add(quit);
        controls.add(details);

        sweep.addActionListener(e -> {
            Sweeper sweeper = new Sweeper(world, treeType);
            try {
                for (LineSegment seg : world) {
                    seg.unhighlight();
                }
                sweeper.run();
                sweepLine = new SweepLine(sweeper.getSweepX());
            } catch (SweeperException ex) {
                // Something bad happened! Show the current sweep line and the
                // segment that caused the problem. Also print the current state
                // of the tree.
                sweepLine = new SweepLine(sweeper.getSweepX());
                sweepLine.highlight();
                ex.getSeg().highlight();
                System.out.println(ex.getMessage());
                System.out.println(ex.getTree());
            }
            repaint();
        });

        reset.addActionListener(e -> {
            sweepLine = null;
            for (LineSegment seg : world) {
                seg.unhighlight();
            }
            repaint();
        });

        clear.addActionListener(e -> {
            world.clear();
            sweepLine = null;
            repaint();
        });

        quit.addActionListener(e -> dispose());

        details.addItemListener(e -> {
            showDetails = details.isSelected();
            repaint();
        });

        main.add(drawingSurface, BorderLayout.CENTER);
        main.add(controls, BorderLayout.SOUTH);
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void load(List<LineSegment> world) {
        world.clear();
        try {
            Scanner in = new Scanner(new File(Constants.FILENAME));
            while (in.hasNext()) {
                int x1 = in.nextInt();
                int y1 = in.nextInt();
                int x2 = in.nextInt();
                int y2 = in.nextInt();
                LineSegment line = new LineSegment(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
                line.fix();
                world.add(line);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("The input file: [" + Constants.FILENAME + "] is not found.\n.");
        }

    }

    private void save(List<LineSegment> world) {
        if (world.isEmpty()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(new File(Constants.FILENAME));
            for (LineSegment line : world) {
                Endpoint left = line.getLeftEndpoint(), right = line.getRightEndpoint();
                int x1 = (int) left.getX(), y1 = (int) left.getY();
                int x2 = (int) right.getX(), y2 = (int) right.getY();
                out.println(x1 + " " + y1 + " " + x2 + " " + y2);
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Problem creating the file: [" + Constants.FILENAME + "]\n.");
        }
    }
}
