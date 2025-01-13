package com.mrlonis.project1.procsimu.comp;

import com.mrlonis.project1.procsimu.Configuration;
import com.mrlonis.project1.procsimu.Control;
import com.mrlonis.project1.procsimu.Stage;
import java.awt.*;
import java.io.*;
import java.nio.file.FileSystems;
import javax.swing.*;
import javax.swing.table.*;

/** The RAM. */
public class Memory extends Stage {

    /** Current instruction */
    public Latch latInstr;

    /** Instruction following the current one in latInstr. */
    public Latch latNextInstr;

    /** PC of the current instruction in latInstr */
    public Latch latPC; // added for superscalar branch prediction

    /** Current data */
    public Latch latData;

    private short[] values = new short[Configuration.MEMORY_SIZE];
    private long instrAddress;
    private long dataAddress;

    private JTable table;
    private AbstractTableModel dataModel;
    private String[] names;
    private Object[][] intData;

    /** Constructor */
    public Memory(Control control) {
        super(control);
        initWindow("Memory");

        latInstr = new Latch(control);
        latNextInstr = new Latch(control);
        latPC = new Latch(control);
        latData = new Latch(control);
    }

    /** Initializes the memory window */
    private void initWindow(String title) {
        setTitle(title);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(true);
        scrollPane.setBounds(36, 48, 150, 300);
        scrollPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        scrollPane.setForeground(new Color(0));
        scrollPane.setBackground(new Color(-3_355_444));
        getContentPane().add(scrollPane);

        table = new JTable();
        table.setBounds(12, 12, 150, 300);
        table.setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setForeground(new Color(0));
        table.setBackground(new Color(16_777_215));
        scrollPane.getViewport().add(table);

        setBounds(10, 20, 180, 600);

        names = new String[2];
        names[0] = "Address";
        names[1] = "Value";
        intData = new Object[Configuration.MEMORY_SIZE][2];

        /**
         * This is depreciated and we should instead use setVisible(true);
         *
         * <p>this.show();
         */
        this.setVisible(true);
    }

    /** Put the address for the instr read. */
    public void readInstruction(long address) {
        instrAddress = address;
    }

    /** Read a word from memory (the actual data becomes available after work() is called */
    public void readData(long address) {
        dataAddress = address;
    }

    /** Write the value at address in the RAM */
    public void write(long address, long value) {
        if (!(address < 0 || address > values.length) && !(value < 0 || value > 0xffffffffL)) {
            values[(int) address + 3] = (byte) (value);
            values[(int) address + 2] = (byte) (value >> 8);
            values[(int) address + 1] = (byte) (value >> 16);
            values[(int) address] = (byte) (value >> 24);
        }

        /**
         * Do not need new Long() as values returns a long
         *
         * <p>dataModel.setValueAt(new Long(values[(int) address]), (int) address, 1); dataModel.setValueAt(new
         * Long(values[(int) address + 1]), (int) address + 1, 1); dataModel.setValueAt(new Long(values[(int) address +
         * 2]), (int) address + 2, 1); dataModel.setValueAt(new Long(values[(int) address + 3]), (int) address + 3, 1);
         */
        dataModel.setValueAt(values[(int) address], (int) address, 1);
        dataModel.setValueAt(values[(int) address + 1], (int) address + 1, 1);
        dataModel.setValueAt(values[(int) address + 2], (int) address + 2, 1);
        dataModel.setValueAt(values[(int) address + 3], (int) address + 3, 1);
        dataModel.fireTableCellUpdated((int) address, 1);
        dataModel.fireTableCellUpdated((int) address + 1, 1);
        dataModel.fireTableCellUpdated((int) address + 2, 1);
        dataModel.fireTableCellUpdated((int) address + 3, 1);
    }

    /** Asks for the file name to load to the user */
    public String getFileName() {
        FileDialog dlgLoad = new FileDialog(this);
        dlgLoad.setMode(FileDialog.LOAD);
        dlgLoad.setTitle("Load");
        dlgLoad.setDirectory(Configuration.DEFAULT_LOAD_DIRECTORY);
        dlgLoad.setVisible(true);

        String defDirectory = dlgLoad.getDirectory();
        String defFile = dlgLoad.getFile();
        this.setTitle(dlgLoad.getFile());

        return defDirectory + FileSystems.getDefault().getSeparator() + defFile;
    }

    /** Loads the file in the RAM. */
    public void load(String filename) {
        short srtC;
        int count;
        InputStream ipsStream;
        int defMode;

        control.setTitle("Control: " + filename);
        try {
            count = 0;
            ipsStream = new FileInputStream(filename);
            while ((srtC = (short) ipsStream.read()) != -1) {
                values[count] = srtC;
                count++;
            }
            ipsStream.close();
        } catch (IOException e) {
            System.out.println("!! Error while reading: " + filename);
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("!! Data too big: " + filename);
            return;
        }

        for (int i = 0; i < values.length; i++) {
            /**
             * Depreciated. Need to Use Integer.valueOf
             *
             * <p>intData[i][0] = new Integer(i); intData[i][1] = new Integer(values[i]);
             */
            intData[i][0] = i;
            intData[i][1] = (int) values[i];
        }

        dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return names.length;
            }

            public int getRowCount() {
                return intData.length;
            }

            public Object getValueAt(int row, int col) {
                return intData[row][col];
            }

            public String getColumnName(int column) {
                return names[column];
            }

            public Class getColumnClass(int col) {
                return getValueAt(0, col).getClass();
            }

            public void setValueAt(Object aValue, int row, int column) {
                intData[row][column] = aValue;
            }
        };
        table.setModel(dataModel);
    }

    /** Read the instructions / the data and updates the latches. */
    public void work() {
        latInstr.write(((long) ((values[(int) instrAddress] & 0xff) << 24)
                        | ((values[(int) instrAddress + 1] & 0xff) << 16)
                        | ((values[(int) instrAddress + 2] & 0xff) << 8)
                        | ((values[(int) instrAddress + 3] & 0xff)))
                & 0xffffffffL);

        latNextInstr.write(((long) ((values[(int) instrAddress + 4] & 0xff) << 24)
                        | ((values[(int) instrAddress + 5] & 0xff) << 16)
                        | ((values[(int) instrAddress + 6] & 0xff) << 8)
                        | ((values[(int) instrAddress + 7] & 0xff)))
                & 0xffffffffL);

        latPC.write(instrAddress);

        if (dataAddress != Const.ADDR_NULL) {
            long lngTmp = ((long) ((values[(int) dataAddress] & 0xff) << 24)
                            | ((values[(int) dataAddress + 1] & 0xff) << 16)
                            | ((values[(int) dataAddress + 2] & 0xff) << 8)
                            | ((values[(int) dataAddress + 3] & 0xff)))
                    & 0xffffffffL;
            latData.write(lngTmp);
        }

        dataAddress = Const.ADDR_NULL;
    }
}
