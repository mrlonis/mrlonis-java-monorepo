package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.*;
import com.mrlonis.project2.procsimu.util.ArgumentChecker;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/** The main class to control the Processor simulation. */
public class Control extends JFrame implements Runnable, ActionListener {

    // The vectors to store the listeners
    private Vector workListenerList = new Vector();
    private Vector clockChangeListenerList = new Vector();

    // A thread for the "run" button
    private Thread thread;

    // *** The elements of the processor ***

    public Memory memory;
    public Register register;

    public StageIF stageIF;
    public StageID stageID;
    public StageEXE stageEXE;
    public StageMEM stageMEM;
    public StageWB stageWB;

    /** A counter to store the number of cycle executed */
    public int cycle = 0;

    /**
     * Main method. Some parameters can be passed to the program. Use <CODE>java Control -help</CODE> to see how to use
     * them.
     */
    public static void main(String[] args) {
        String usage = " -version | -help | [ -f mipsfile ]";

        if (!(ArgumentChecker.isValid(usage, args))) {
            System.out.println("usage: java Control " + usage);
            return;
        }
        // if we get here, then the arguments are legal.

        if (ArgumentChecker.switchFound("-help", args)) {
            System.out.println("usage: java Control " + usage);
            return;
        }

        if (ArgumentChecker.switchFound("-version", args)) {
            System.out.println("Processor simulation in Java, version " + Configuration.VERSION);
            return;
        }

        Control control = new Control();
        control.setVisible(true);

        control.initProcElements();

        String filename = ArgumentChecker.paramAtSwitch("-f", args);
        if (filename != null) {
            // If the user gave a data file name to load
            control.memory.load(filename);
        }
    }

    /** Constructor */
    public Control() {
        initButtons();
        initMenu();
        this.setBounds(200, 20, 450, 80);
        printInfos();
    }

    /** Initialization of the processor elements. */
    private void initProcElements() {
        register = new Register(this);
        stageIF = new StageIF(this);
        stageID = new StageID(this);
        stageEXE = new StageEXE(this);
        stageMEM = new StageMEM(this);
        stageWB = new StageWB(this);
        memory = new Memory(this);
    }

    /** Init of the buttons */
    private void initButtons() {
        JButton btnStop = new JButton("Stop");
        btnStop.setActionCommand("stop");
        btnStop.addActionListener(this);

        JButton btnEnd = new JButton("Quit");
        btnEnd.setActionCommand("end");
        btnEnd.addActionListener(this);

        JButton btnStep = new JButton("Step");
        btnStep.setActionCommand("step");
        btnStep.addActionListener(this);

        JButton btnRun = new JButton("Run");
        btnRun.setActionCommand("run");
        btnRun.addActionListener(this);

        JButton btnReset = new JButton("Reset");
        btnReset.setActionCommand("reset");
        btnReset.addActionListener(this);

        this.getContentPane().setLayout(new GridLayout());
        this.setTitle("Control");
        this.getContentPane().add(btnStop, null);
        this.getContentPane().add(btnStep, null);
        this.getContentPane().add(btnRun, null);
        this.getContentPane().add(btnReset, null);
        this.getContentPane().add(btnEnd, null);
    }

    /** Init of the menu */
    private void initMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menuLoad = new Menu("Load");
        MenuItem menuItemLoad = new MenuItem("Load");
        menuItemLoad.setActionCommand("load");
        menuItemLoad.addActionListener(this);
        menuLoad.add(menuItemLoad);
        menuBar.add(menuLoad);

        Menu menuPrint = new Menu("Print");
        MenuItem menuItemBPStat = new MenuItem("BP statistics");
        menuItemBPStat.setActionCommand("bpstat");
        menuItemBPStat.addActionListener(this);
        menuPrint.add(menuItemBPStat);
        MenuItem menuItemIPC = new MenuItem("IPC");
        menuItemIPC.setActionCommand("ipc");
        menuItemIPC.addActionListener(this);
        menuPrint.add(menuItemIPC);
        MenuItem menuItemInfos = new MenuItem("Configuration");
        menuItemInfos.setActionCommand("infos");
        menuItemInfos.addActionListener(this);
        menuPrint.add(menuItemInfos);
        menuBar.add(menuPrint);

        setMenuBar(menuBar);
    }

    /** Deletes (dispose) all processor elements */
    private void disposeProcElements() {
        register.dispose();
        stageIF.dispose();
        stageID.dispose();
        stageEXE.dispose();
        stageMEM.dispose();
        stageWB.dispose();
        memory.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if ("stop".equals(ac)) {
            stop();
        } else if ("end".equals(ac)) {
            System.exit(0);
        } else if ("step".equals(ac)) {
            step();
        } else if ("run".equals(ac)) {
            start();
        } else if ("reset".equals(ac)) {
            reset();
        } else if ("load".equals(ac)) {
            memory.load(memory.getFileName());
        } else if ("ipc".equals(ac)) {
            printIPC();
        } else if ("infos".equals(ac)) {
            printInfos();
        }
    }

    public synchronized void addWorkListener(WorkListener workListener) {
        workListenerList.addElement(workListener);
    }

    public synchronized void removeWorkListener(WorkListener workListener) {
        workListenerList.removeElement(workListener);
    }

    public synchronized void addClockChangeListener(ClockChangeListener clockChangeListener) {
        clockChangeListenerList.addElement(clockChangeListener);
    }

    public synchronized void removeClockChangeListener(ClockChangeListener clockChangeListener) {
        clockChangeListenerList.removeElement(clockChangeListener);
    }

    /** Execute all work methods for all registered WorkListener */
    private void processWork() {
        // TODO instead of executing work method of each stage like Project 1, execute all the work methods (see
        // processClockChange)

        /* Beginning of added code */
        for (Enumeration Enum = workListenerList.elements(); Enum.hasMoreElements(); ) {
            ((WorkListener) (Enum.nextElement())).work();
        }
        /* End of added code */

        cycle++;
    }

    /** Execute all clockChanged methods for all registered ClockChangeListeners */
    private void processClockChange() {
        for (Enumeration Enum = clockChangeListenerList.elements(); Enum.hasMoreElements(); ) {
            ((ClockChangeListener) (Enum.nextElement())).clockChanged();
        }
    }

    /** Execute one step */
    void step() {
        processWork();
        processClockChange();
    }

    /** Reset */
    void reset() {
        stop();
        cycle = 0;

        clockChangeListenerList.removeAllElements();
        workListenerList.removeAllElements();

        disposeProcElements();
        initProcElements();
    }

    /** Start the thread */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /** To safely stop the thread */
    public void stop() {
        thread = null;
    }

    /** Run method for the thread */
    public void run() {
        Thread thisThread = Thread.currentThread();
        try {
            while (thread == thisThread) {
                processWork();
                processClockChange();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught " + e);
            this.stop();
        }
    }

    /**
     * Prints some information on the configuration
     *
     * @see Configuration
     */
    public void printInfos() {}

    /** Prints the IPC. */
    public void printIPC() {
        System.out.println("-- IPC --");
        int nbInstrExecuted;
        nbInstrExecuted = stageEXE.nbInstrExecuted;
        System.out.println("Cycles:            " + cycle);
        System.out.println("Nb instr executed: " + nbInstrExecuted);
        if (cycle > 0) {
            double ipc = (float) nbInstrExecuted / (float) cycle;
            System.out.println("IPC: " + ipc);
        }
    }
}
