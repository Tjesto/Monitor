package com.monitor.panels;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.monitor.model.GeneralTableModel;
import com.monitor.model.MonthlyTableModel;

public final class GeneralDataPanel extends JPanel {	
	
	protected JTable mainView;
	protected JComboBox<String> termChooser;

	public GeneralDataPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JLabel lblKlasa = new JLabel("Klasa");
		panel_1.add(lblKlasa);
		
		JComboBox classChooser = new JComboBox();
		panel_1.add(classChooser);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JLabel lblSemestr = new JLabel("Semestr");
		panel_2.add(lblSemestr);
		
		termChooser = new JComboBox<>(new String[] {"Pierwszy", "Drugi"});		
		panel_2.add(termChooser);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JLabel lblRokSzkolny = new JLabel("Rok szkolny");
		panel_3.add(lblRokSzkolny);
		
		JComboBox comboBox = new JComboBox();
		panel_3.add(comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		mainView = new JTable();
		scrollPane.setViewportView(mainView);
				
		setVisible(true);
		mainView.setModel(new GeneralTableModel());
		termChooser.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {				
				if (e.getStateChange() == ItemEvent.SELECTED) {			
					mainView.removeAll();
					//mainView.setModel(new MonthlyTableModel(new String[]{"Matematyk", "Historia"}, termChooser.getSelectedItem().equals("Drugi")));
					mainView.repaint();
				}
			}
		});
	}

}
