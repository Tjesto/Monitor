package com.monitor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.monitor.panels.GeneralDataPanel;
import com.monitor.panels.InDataPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppWindow {

	private JFrame frame;
	private JPanel panel;
	private InDataPanel inputPanel;
	private GeneralDataPanel overallPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppWindow window = new AppWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnPlik = new JMenu("Plik");
		menuBar.add(mnPlik);
		
		JMenu mnEdycja = new JMenu("Edycja");
		menuBar.add(mnEdycja);
		
		JMenu mnDane = new JMenu("Dane");
		menuBar.add(mnDane);
		
		JMenuItem addTeacher = new JMenuItem("Dodaj nauczyciela");
		mnDane.add(addTeacher);
		
		JMenuItem addClass = new JMenuItem("Dodaj klas\u0119");
		mnDane.add(addClass);
		
		JMenu mnView = new JMenu("Widok");
		menuBar.add(mnView);
		
		final JRadioButtonMenuItem rdbtnmntmSemestr = new JRadioButtonMenuItem("Semestr");
		mnView.add(rdbtnmntmSemestr);			
		
		final JRadioButtonMenuItem rdbtnmntmPodsumowanieOglne = new JRadioButtonMenuItem("Podsumowanie og\u00F3lne");
		mnView.add(rdbtnmntmPodsumowanieOglne);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		frame.getContentPane().add(panel);
		
		inputPanel = new InDataPanel();
		overallPanel = new GeneralDataPanel();		
		frame.getContentPane().add(overallPanel);
		frame.getContentPane().add(inputPanel);
		inputPanel.setVisible(false);
		overallPanel.setVisible(false);
		rdbtnmntmSemestr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rdbtnmntmPodsumowanieOglne.setSelected(false);
				panel.setVisible(false);				
				overallPanel.setVisible(false);
				frame.getContentPane().add(inputPanel);
				inputPanel.setVisible(true);
				rdbtnmntmSemestr.setSelected(true);
			}
		});
		rdbtnmntmPodsumowanieOglne.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rdbtnmntmPodsumowanieOglne.setSelected(true);
				panel.setVisible(false);
				inputPanel.setVisible(false);
				frame.getContentPane().add(overallPanel);
				overallPanel.setVisible(true);
				rdbtnmntmSemestr.setSelected(false);
			}
		});
	}
}
