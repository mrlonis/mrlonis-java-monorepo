package com.mrlonis.project2.procsimu.comp;

import com.mrlonis.project2.procsimu.Control;
import com.mrlonis.project2.procsimu.Stage;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Represents a file (default size is 32 registers) of 32-bit registers.
 *
 * @author Modified by Vincent Oberle.
 */
public class Register extends Stage {

    // Number of registers
    private static final int NB_REGISTER = 32;

    // The arrays to store the register values
    private long[] values = new long[NB_REGISTER];

    private String[] names;
    private Object[][] data;

    // The data model for the table
    private AbstractTableModel dataModel = new AbstractTableModel() {
        public int getColumnCount() {
            return names.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public String getColumnName(int column) {
            return names[column];
        }

        public Class getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }

        public void setValueAt(Object aValue, int row, int column) {
            data[row][column] = aValue;
        }
    };

    /**
     * Constructor for Register.
     *
     * @param control The control object.
     */
    public Register(Control control) {
        super(control);
        initWindow();
    }

    /** Initialize the window for the register. */
    private void initWindow() {
        setTitle("Register");

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(true);
        scrollPane.setBounds(36, 48, 150, 300);
        scrollPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        scrollPane.setForeground(new Color(0));
        scrollPane.setBackground(new Color(-3_355_444));
        getContentPane().add(scrollPane);

        JTable table = new JTable();
        table.setBounds(12, 12, 150, 300);
        table.setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setForeground(new Color(0));
        table.setBackground(new Color(16_777_215));
        scrollPane.getViewport().add(table);

        setBounds(820, 20, 180, 600);

        names = new String[2];
        names[0] = "Address";
        names[1] = "Value";
        data = new String[NB_REGISTER][2];
        for (int i = 0; i < NB_REGISTER; i++) {
            data[i][0] = Long.toString(i);
            data[i][1] = Long.toString(0L);
        }
        table.setModel(dataModel);

        this.setVisible(true);
    }

    /**
     * Reads a register.
     *
     * @param regNb Number of the register to read.
     * @return Value of the register.
     */
    public long read(long regNb) {
        // Test if the register is valid
        if ((regNb < 1L) || (regNb >= NB_REGISTER)) {
            return 0; // covers the R0 = 0
        } else {
            return values[(int) regNb];
        }
    }

    /**
     * Wrotes a value in the register.
     *
     * @param regNb Number of the register to write
     * @param value Value to write
     */
    public void write(long regNb, long value) {
        // Test if the register and the value are valid
        if (!((regNb < 1L) || (regNb >= NB_REGISTER)) && !((value < 0L) || (value > 0xffffffffL))) {
            values[(int) regNb] = value;
        }
        dataModel.setValueAt(Long.toString(value), (int) regNb, 1);
        dataModel.fireTableCellUpdated((int) regNb, 1);
    }
}
