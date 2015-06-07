package com.monitor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.monitor.dataBaseManager.ClassSubject;
import com.monitor.dataBaseManager.DataBaseManager;

public class MonthlyTableModel implements TableModel {

	private static final String[] columns = new String[] { "Rodzaj zaj��",
			"Wrzesie�", "Pa�dziernik", "Listopad", "Grudzie�", "Stycze�",
			"Luty", "Marzec", "Kwiecie�", "Maj", "Czerwiec", "��cznie" };

	DataBaseManager dataBaseManager = DataBaseManager.getInstance();
	private static final int lessonKind = 0;
	private static final int totalLessons = columns.length - 1;
	private static final int secondTermOffset = 5;

	private final boolean isSecondTerm;

	private ArrayList<String> subjects;
	private ArrayList<ArrayList<Integer>> values;
	protected HashMap<Integer, String> classList;

	public MonthlyTableModel(String classId, int year, boolean isSecondTerm) {

		classList = dataBaseManager.getClassesNames();
		int id = 0;
		for (int key : classList.keySet()) {
			if (classList.get(key).equals(classId)) {
				id = key;
			}
		}
		ArrayList<ClassSubject> csl = dataBaseManager.getSubjectsForClass(id,
				year);
		HashSet<String> set = new HashSet<String>();
		for (ClassSubject cs : csl) {
			set.add(cs.getSubjectName());
		}
		values = new ArrayList<ArrayList<Integer>>();
		subjects = new ArrayList<String>();
		for (String s : set) {
			subjects.add(s);
		}
		/* uzupelnianie values zerami */
		for (int j = 0; j < subjects.size(); j++) {
			values.add(new ArrayList<Integer>());
			for (int i = 0; i < 10; i++) {
				values.get(j).add(0);
			}
		}

		for (int i = 0; i < subjects.size(); i++) {
			for (ClassSubject cs : csl) {
				if (cs.getSubjectName().equals(subjects.get(i))) {
					values.get(i).set(cs.getMonth(), cs.getRealized());
				}
			}
		}

		/* dataBaseManager.get */

		/* this.subjects = subjects; */
		this.isSecondTerm = isSecondTerm;
		/* values = new int[subjects.length][getColumnCount() - 2]; */
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
		return subjects.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return subjects.get(row);
		case 6:
			return sum(row);
		}
		if (isSecondTerm == false) {
			return values.get(row).get(col - 1);
		} else {
			return values.get(row).get(col - 1 + 5);
		}
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
		return col > 0 && col < getColumnCount() - 1;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueAt(Object val, int row, int col) {
		if (isCellEditable(row, col)) {
			if (val instanceof Integer) {
				values.get(row).set(col - 1, (int) val);
			}
		}

	}

}
