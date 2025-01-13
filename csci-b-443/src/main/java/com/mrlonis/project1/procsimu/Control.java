package com.mrlonis.project1.procsimu;

import com.mrlonis.project1.procsimu.comp.ClockChangeListener;
import com.mrlonis.project1.procsimu.comp.Const;
import com.mrlonis.project1.procsimu.comp.Memory;
import com.mrlonis.project1.procsimu.comp.Register;
import com.mrlonis.project1.procsimu.comp.WorkListener;
import com.mrlonis.project1.procsimu.util.ArgumentChecker;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/** The main class to control the Processor simulation. */
public class Control extends JFrame implements Runnable, ActionListener {

    // The vectors to store the listeners
    private final Vector workListenerList = new Vector();
    private final Vector clockChangeListenerList = new Vector();
    private int currentStage;

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
    public int cycle;

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
        this.initButtons();
        this.initMenu();
        this.setBounds(200, 20, 450, 80);
        this.printInfos();
    }

    /** Initialization of the processor elements. */
    private void initProcElements() {
        this.register = new Register(this);
        this.stageIF = new StageIF(this);
        this.stageID = new StageID(this);
        this.stageEXE = new StageEXE(this);
        this.stageMEM = new StageMEM(this);
        this.stageWB = new StageWB(this);
        this.memory = new Memory(this);
        this.currentStage = Const.STAGE_IF;
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

        getContentPane().setLayout(new GridLayout());
        setTitle("Control");
        getContentPane().add(btnStop, null);
        getContentPane().add(btnStep, null);
        getContentPane().add(btnRun, null);
        getContentPane().add(btnReset, null);
        getContentPane().add(btnEnd, null);
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

        this.setMenuBar(menuBar);
    }

    /** Deletes (dispose) all processor elements */
    private void disposeProcElements() {
        this.register.dispose();
        this.stageIF.dispose();
        this.stageID.dispose();
        this.stageEXE.dispose();
        this.stageMEM.dispose();
        this.stageWB.dispose();
        this.memory.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if ("stop".equals(ac)) {
            this.stop();
        } else if ("end".equals(ac)) {
            System.exit(0);
        } else if ("step".equals(ac)) {
            this.step();
        } else if ("run".equals(ac)) {
            this.start();
        } else if ("reset".equals(ac)) {
            this.reset();
        } else if ("load".equals(ac)) {
            this.memory.load(this.memory.getFileName());
        } else if ("ipc".equals(ac)) {
            this.printIPC();
        } else if ("infos".equals(ac)) {
            this.printInfos();
        }
    }

    public synchronized void addWorkListener(WorkListener workListener) {
        this.workListenerList.addElement(workListener);
    }

    public synchronized void removeWorkListener(WorkListener workListener) {
        this.workListenerList.removeElement(workListener);
    }

    public synchronized void addClockChangeListener(ClockChangeListener clockChangeListener) {
        this.clockChangeListenerList.addElement(clockChangeListener);
    }

    public synchronized void removeClockChangeListener(ClockChangeListener clockChangeListener) {
        this.clockChangeListenerList.removeElement(clockChangeListener);
    }

    /** I think I finished this section but IDK */
    private void processWork() {
        switch (this.currentStage) {
                // TODO: transition to appropriate next stage
            case Const.STAGE_IF:
                this.stageIF.work();
                this.currentStage = Const.STAGE_ID; // TODO
                break;
            case Const.STAGE_ID:
                this.stageID.work();
                this.currentStage = Const.STAGE_EXE; // TODO
                break;
            case Const.STAGE_EXE:
                this.stageEXE.work();
                this.currentStage = Const.STAGE_MEM; // TODO
                break;
            case Const.STAGE_MEM:
                this.stageMEM.work();
                this.currentStage = Const.STAGE_WB; // TODO
                break;
            case Const.STAGE_WB:
                this.stageWB.work();
                this.currentStage = Const.STAGE_IF; // TODO
                break;
        }

        this.memory.work();
        this.cycle++;
    }

    /** Execute all clockChanged methods for all registered ClockChangeListeners */
    private void processClockChange() {
        for (Enumeration Enum = this.clockChangeListenerList.elements(); Enum.hasMoreElements(); ) {
            ((ClockChangeListener) (Enum.nextElement())).clockChanged();
        }
    }

    /** Execute one step */
    void step() {
        this.processWork();
        this.processClockChange();
    }

    /** Reset */
    void reset() {
        this.stop();
        this.cycle = 0;

        this.clockChangeListenerList.removeAllElements();
        this.workListenerList.removeAllElements();

        this.disposeProcElements();
        this.initProcElements();
    }

    /** Start the thread */
    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    /** To safely stop the thread */
    public void stop() {
        this.thread = null;
    }

    /**
     * Run method for the thread
     *
     * <p>I put in a try catch to catch the memory from throwing an array index out of bounds exception
     */
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (this.thread == thisThread) {
            try {
                this.processWork();
                this.processClockChange();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Caught Memory out of bounds!");
                break;
            }
        }
        this.stop();
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
        nbInstrExecuted = this.stageEXE.nbInstrExecuted;
        System.out.println("Cycles:            " + this.cycle);
        System.out.println("Nb instr executed: " + nbInstrExecuted);

        if (this.cycle > 0) {
            double ipc = (float) nbInstrExecuted / (float) this.cycle;
            System.out.println("IPC: " + ipc);
        }
    }
}
