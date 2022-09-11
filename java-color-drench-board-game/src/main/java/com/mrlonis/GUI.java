package com.mrlonis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;

/**
 * Builds the gui interface for an interactive run of the game.
 */
public class GUI extends JFrame {

    /**
     * This is an auto-generated serial ID to remove the Eclipse warning.
     */
    private static final long serialVersionUID = -3353395325455077732L;

    // Boilerplate setup.
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private final JPanel grid;
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu steps = new JMenu();
    private final Board board;
    private final Game game;

    /**
     * Creates a gui to view the given game.
     */
    public GUI(Game game) {
        setTitle(Constants.TITLE);
        this.game = game;
        board = game.getBoard();
        int size = board.getSize();

        grid = new JPanel(new GridLayout(size, size));
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				grid.add(new TileButton(board.get(new Coord(x, y))));
			}
		}

        setSteps();
        initMenu();
        setContentPane(grid);
        setPreferredSize(new Dimension(640, 640));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Updates the display to show the number of steps used by the player so far.
     */
    public void setSteps() {
        steps.setText(game.getSteps() + "/" + game.getStepLimit());
    }

    /**
     * Sets up the menu.
     */
    private void initMenu() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(gameMenu);

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> game.resize(board.getSize()));
        gameMenu.add(restart);

        JMenuItem resize = new JMenuItem("Resize");
        resize.addActionListener(e -> {
            String msg = JOptionPane.showInputDialog("Please type in the size");
            game.resize(Integer.parseInt(msg));
        });
        gameMenu.add(resize);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        gameMenu.add(exit);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        menuBar.add(help);

        JMenuItem rules = new JMenuItem("HowToPlay");
        rules.addActionListener(e -> JOptionPane.showMessageDialog(this, Constants.HINT));
        help.add(rules);

        JMenuItem suggest = new JMenuItem("Suggest");
        suggest.addActionListener(e -> JOptionPane.showMessageDialog(this, board.suggest()));
        help.add(suggest);

        menuBar.add(steps);
        setJMenuBar(menuBar);
    }

    /**
     * An inner class to represent the physical manifestation of a Tile on a game board. These are clickable objects, so we'll take advantage of the functionality already
     * implemented by JButton.
     */
    class TileButton extends JButton {

        /**
         * This is an auto-generated serial ID to remove the Eclipse warning.
         */
        private static final long serialVersionUID = 1212931495709531989L;
        private final Tile tile;

        public TileButton(Tile tile) {
            this.tile = tile;
            // Add a listener to process player moves and deal with game
            // win/lose conditions.
            addMouseListener(new MouseInputAdapter() {
                public void mousePressed(MouseEvent e) {
                    // Update the model
                    game.select(tile.getColor());
                    // Update the view
                    setSteps();
                    GUI.this.repaint();
					if (board.fullyFlooded()) {
						youWin();
					} else if (game.noMoreSteps()) {
						youLose();
					}
                }
            });
        }

        /**
         * Handles a game win condition.
         */
        private void youWin() {
            JOptionPane.showMessageDialog(GUI.this, "You Win!");
            game.resize(board.getSize());
        }

        /**
         * Handles a game loss condition.
         */
        private void youLose() {
            JOptionPane.showMessageDialog(GUI.this, "You lose");
            game.resize(board.getSize());
        }

        /**
         * Draws the tile on this button.
         */
        public void paintComponent(Graphics gr) {
            setBackground(tile.getColor()
                              .get());
            super.paintComponent(gr);
        }
    }
}
