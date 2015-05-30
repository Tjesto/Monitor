package com.monitor.model;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class MonthlyTableModel implements TableModel {

	private static final String[] columns = new String[] { "Rodzaj zajêæ",
			"Wrzesieñ", "PaŸdziernik", "Listopad", "Grudzieñ", "Styczeñ",
			"Luty", "Marzec", "Kwiecieñ", "Maj", "Czerwiec", "£¹cznie" }; 
	
	private static final int lessonKind = 0;
	private static final int totalLessons = columns.length -1;
	private static final int secondTermOffset = 5;

	private final boolean isSecondTerm;
	
	private final String[] subjects;
	private final int[][] values;
	
	public MonthlyTableModel(String[] subjects, boolean isSecondTerm) {
		this.subjects = subjects;
		this.isSecondTerm = isSecondTerm;
		values = new int[subjects.length][getColumnCount() - 2];
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public Class<?> getColumnClass(int index) {
		if (index == 0) {
			return String.class;
		}
		return Integer.class;
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public String getColumnName(int arg0) {		
		if (arg0 == 0) {
			return columns[lessonKind];
		}
		if (arg0 == getColumnCount() - 1) {
			return columns[totalLessons];
		}
		
		return isSecondTerm ? columns[arg0 + secondTermOffset] : columns[arg0];
	}

	@Override
	public int getRowCount() {
		return subjects.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: 
			return subjects[row];
		case 6:
			return sum(row);
		}		
		return values[row][col-1];
	}

	private Integer sum(int row) {
		int sum = 0;
		for (int i = 1; i <= getColumnCount() - 2; i++) {
			Object o = getValueAt(row, i);
			if (o instanceof Integer) {
				sum += (int) o;
			}
		}
		return sum;
	}

	@Override
	public boolean isCellEditable(int row, int col) {		
		return col > 0 && col < getColumnCount() -1;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueAt(Object val, int row, int col) {
		if (isCellEditable(row, col)) {
			if (val instanceof Integer) {
				values[row][col-1] = (int) val;
			}
		}

	}

}
