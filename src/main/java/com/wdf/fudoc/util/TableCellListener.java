package com.wdf.fudoc.util;

import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

/*
 * this class listens for changes made to the data in the table via the
 * tablecelleditor. when editing is started, the value of the cell is saved
 * when editing is stopped the new value is saved. when the oold and new
 * values are different, then the provided action is invoked.
 *
 * the source of the action is a tablecelllistener instance.
 */
public class TableCellListener implements PropertyChangeListener, Runnable {
    private JTable table;
    private Action action;
    private int row;
    private int column;
    private Object oldvalue;
    private Object newvalue;

    /**
     * create a tablecelllistener.
     *
     * @param table  the table to be monitored for data changes
     * @param action the action to invoke when cell data is changed
     */
    public TableCellListener(JTable table, Action action) {
        this.table = table;
        this.action = action;
        this.table.addPropertyChangeListener(this);
    }

    /**
     * create a tablecelllistener with a copy of all the data relevant to
     * the change of data for a given cell.
     *
     * @param row      the row of the changed cell
     * @param column   the column of the changed cell
     * @param oldvalue the old data of the changed cell
     * @param newvalue the new data of the changed cell
     */
    private TableCellListener(JTable table, int row, int column, Object oldvalue, Object newvalue) {
        this.table = table;
        this.row = row;
        this.column = column;
        this.oldvalue = oldvalue;
        this.newvalue = newvalue;
    }

    /**
     * get the column that was last edited
     *
     * @return the column that was edited
     */
    public int getcolumn() {
        return column;
    }

    /**
     * get the new value in the cell
     *
     * @return the new value in the cell
     */
    public Object getnewvalue() {
        return newvalue;
    }

    /**
     * get the old value of the cell
     *
     * @return the old value of the cell
     */
    public Object getoldvalue() {
        return oldvalue;
    }

    /**
     * get the row that was last edited
     *
     * @return the row that was edited
     */
    public int getrow() {
        return row;
    }

    /**
     * get the table of the cell that was changed
     *
     * @return the table of the cell that was changed
     */
    public JTable gettable() {
        return table;
    }

    //
// implement the propertychangelistener interface
//
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // a cell has started/stopped editing
        if ("tableCellEditor".equals(e.getPropertyName())) {
            if (table.isEditing()) {
                //system.out.printf("tablecelleditor is editing..%n");
                processeditingstarted();
            } else {
                //system.out.printf("tablecelleditor editing stopped..%n");
                processeditingstopped();
            }
        }
    }

    /*
     * save information of the cell about to be edited
     */
    private void processeditingstarted() {
        // the invokelater is necessary because the editing row and editing
        // column of the table have not been set when the "tablecelleditor"
        // propertychangeevent is fired.
        // this results in the "run" method being invoked
        SwingUtilities.invokeLater(this);
    }

    /*
     * see above.
     */
    @Override
    public void run() {
        row = table.convertRowIndexToModel(table.getEditingRow());
        column = table.convertColumnIndexToModel(table.getEditingColumn());
        oldvalue = table.getModel().getValueAt(row, column);
        //这里应对oldvalue为null的情况做处理，否则将导致原值与新值均为空时仍被视为值改变
        if (oldvalue == null) oldvalue = "";
        newvalue = null;
    }

    /*
     *  update the cell history when necessary
     */
    private void processeditingstopped() {
        newvalue = table.getModel().getValueAt(row, column);
        //这里应对newvalue为null的情况做处理，否则后面会抛出异常
        if (newvalue == null) newvalue = "";
        // the data has changed, invoke the supplied action
        if (!newvalue.equals(oldvalue)) {
            // make a copy of the data in case another cell starts editing
            // while processing this change
            TableCellListener tcl = new TableCellListener(gettable(), getrow(), getcolumn(), getoldvalue(), getnewvalue());
            ActionEvent event = new ActionEvent(tcl, ActionEvent.ACTION_PERFORMED, "");
            action.actionPerformed(event);
        }
    }

}