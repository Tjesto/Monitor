package com.monitor.panels;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import com.monitor.database.Class;
import com.monitor.database.DataBaseManager;
import com.monitor.model.MonthlyTableModel;

import javax.swing.JScrollPane;

public class InDataPanel extends JPanel {
	protected JTable mainView = new JTable();
	protected JComboBox<String> classChooser = new JComboBox<String>();
	protected JComboBox<String> yearChooser = new JComboBox<String>();
	protected JComboBox<String> termChooser = new JComboBox<String>();
	
	protected HashMap<Integer, String> classList;
	
	DataBaseManager dataBaseManager = DataBaseManager.getInstance();
	protected ItemListener listenerTermChooser = new ItemListener() {
		// TODO
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mainView.removeAll();
				mainView.setModel(
						new MonthlyTableModel((String)classChooser.getSelectedItem(), 
								Integer.parseInt((String)yearChooser.getSelectedItem()),
								termChooser.getSelectedItem().equals("Drugi")));
				mainView.repaint();
			}
		}
	};

	protected ItemListener listenerClassChooser = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// TODO
				String cmboitem = (String) classChooser.getSelectedItem();
				int id = -1;
				for (int key : classList.keySet()) {
					if (classList.get(key).equals(cmboitem)) {
						id = key;
						System.out
								.println("listener listenerClassChooser clicked = "
										+ cmboitem + " " + key);
					}
				}
				/* pobranie danych by ustawic combobox roku dla danej klasy */
				ArrayList<Class> cl = dataBaseManager.getClasses();
				for (Class c : cl) {
					if (c.getId() == id) {
							int begin = Integer.parseInt(c.getBegin());
							int end = Integer.parseInt(c.getEnd());
							yearChooser.removeAllItems();
							while (begin <= end) {
								yearChooser.addItem(Integer.toString(begin));
								begin++;
							}
					}
				}
				/* odswiezenie widoku tabeli */
				mainView.setModel(
						new MonthlyTableModel((String)classChooser.getSelectedItem(), 
								Integer.parseInt((String)yearChooser.getSelectedItem()),
								termChooser.getSelectedItem().equals("Drugi")));

			}
		}
	};

	/**
	 * Create the panel.
	 */
	public InDataPanel() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JLabel lblKlasa = new JLabel("Klasa");
		panel_1.add(lblKlasa);
		
		termChooser = new JComboBox<>(new String[] { "Pierwszy", "Drugi" });
		termChooser.addItemListener(listenerTermChooser);

		classChooser.addItemListener(listenerClassChooser);
		classList = dataBaseManager.getClassesNames();
		for (int key : classList.keySet()) {
			classChooser.addItem(classList.get(key));
		}
		panel_1.add(classChooser);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JLabel lblSemestr = new JLabel("Semestr");
		panel_2.add(lblSemestr);

		panel_2.add(termChooser);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		JLabel lblRokSzkolny = new JLabel("Rok szkolny");
		panel_3.add(lblRokSzkolny);
		panel_3.add(yearChooser);
		yearChooser.addItemListener(listenerTermChooser);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		/*classChooser.setSelectedIndex(1);
		yearChooser.setSelectedIndex(1);
		mainView.setModel(
				new MonthlyTableModel((String)classChooser.getSelectedItem(), 
						Integer.parseInt((String)yearChooser.getSelectedItem()),
						termChooser.getSelectedItem().equals("Drugi")));*/
		scrollPane.setViewportView(mainView);
		setVisible(true);
	}

}
