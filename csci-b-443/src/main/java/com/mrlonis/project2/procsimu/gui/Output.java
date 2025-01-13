package com.mrlonis.project2.procsimu.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Default output for the simulation.
 *
 * @author Modified by Vincent Oberle.
 */
public abstract class Output extends JFrame {
    // The text area to print some messages
    private TextArea txtArea;

    /** Builds a new window */
    public Output() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                setVisible(false);
            }
        });
    }

    /** Window initialization */
    public void initWindow(String title, int x, int y) {
        initWindow(title, x, y, 200, 200);
    }

    /** Window initialization */
    public void initWindow(String title, int x, int y, int w, int h) {
        getContentPane().setLayout(new GridLayout());

        txtArea = new TextArea();
        txtArea.setFont(new Font("", 0, 10));
        txtArea.setSize(150, 150);
        getContentPane().add(txtArea);

        setTitle(title);
        setBounds(x, y, w, h);
        this.setVisible(true);
    }

    /** Prints some text in the window */
    public void print(String str) {
        txtArea.append(str + System.lineSeparator());
    }
}
