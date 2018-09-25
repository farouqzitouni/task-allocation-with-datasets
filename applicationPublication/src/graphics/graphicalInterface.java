package graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.geometry.Point3D;
import package1.bidderGenerator;
import package1.taskGenerator;

public class graphicalInterface {

	JFrame window1 = null;
	JTabbedPane tabbedpane1 = null;
	JPanel onglet1 = null;
	JPanel onglet2 = null;
	JPanel onglet3 = null;

	Font police1 = new Font("Garamond", Font.BOLD, 16);

	public graphicalInterface() {
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		window1 = new JFrame();
		window1.setTitle("Simulator");
		window1.setSize(new Dimension(550, 380));
		window1.setLocation(new Point((width - 550) / 2, (height - 380) / 2));
		window1.setResizable(false);
		window1.setLayout(new BorderLayout());
		window1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		initializeOnglet1();
		initializeOnglet2();
		initializeOnglet3();

		tabbedpane1 = new JTabbedPane();
		tabbedpane1.addTab("Datasets generator", onglet1);
		tabbedpane1.addTab("AG parameters", onglet2);
		tabbedpane1.addTab("Simulation", onglet3);
		window1.add(tabbedpane1, BorderLayout.CENTER);
		
		tabbedpane1.setSelectedIndex(2);

		window1.setVisible(true);
	}

	public void initializeOnglet1() {
		onglet1 = new JPanel();
		onglet1.setLayout(new BorderLayout());

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(10, 2, 5, 5));
		panel1.setBorder(new TitledBorder(""));
		onglet1.add(panel1, BorderLayout.CENTER);

		JLabel label1 = new JLabel("Number of sensors : ");
		label1.setFont(police1);
		panel1.add(label1, 0);

		JSpinner spinner1 = new JSpinner();
		spinner1.setFont(police1);
		JSpinner.NumberEditor numbereditor1 = new JSpinner.NumberEditor(spinner1);
		spinner1.setEditor(numbereditor1);
		numbereditor1.getModel().setMinimum(1);
		numbereditor1.getModel().setMaximum(20);
		numbereditor1.getModel().setStepSize(1);
		numbereditor1.getModel().setValue(3);
		panel1.add(spinner1, 1);

		JLabel label2 = new JLabel("Number of tasks : ");
		label2.setFont(police1);
		panel1.add(label2, 2);

		JSpinner spinner2 = new JSpinner();
		spinner2.setFont(police1);
		JSpinner.NumberEditor numbereditor2 = new JSpinner.NumberEditor(spinner2);
		spinner2.setEditor(numbereditor2);
		numbereditor2.getModel().setMinimum(1);
		numbereditor2.getModel().setMaximum(100);
		numbereditor2.getModel().setStepSize(1);
		numbereditor2.getModel().setValue(10);
		panel1.add(spinner2, 3);

		JLabel label3 = new JLabel("Number of bidders : ");
		label3.setFont(police1);
		panel1.add(label3, 4);

		JSpinner spinner3 = new JSpinner();
		spinner3.setFont(police1);
		JSpinner.NumberEditor numbereditor3 = new JSpinner.NumberEditor(spinner3);
		spinner3.setEditor(numbereditor3);
		numbereditor3.getModel().setMinimum(1);
		numbereditor3.getModel().setMaximum(200);
		numbereditor3.getModel().setStepSize(1);
		numbereditor3.getModel().setValue(10);
		panel1.add(spinner3, 5);

		JLabel label4 = new JLabel("Height of environment : ");
		label4.setFont(police1);
		panel1.add(label4, 6);

		JSpinner spinner4 = new JSpinner();
		spinner4.setFont(police1);
		JSpinner.NumberEditor numbereditor4 = new JSpinner.NumberEditor(spinner4);
		spinner4.setEditor(numbereditor4);
		numbereditor4.getModel().setMinimum(10);
		numbereditor4.getModel().setMaximum(500);
		numbereditor4.getModel().setStepSize(10);
		numbereditor4.getModel().setValue(20);
		panel1.add(spinner4, 7);

		JLabel label5 = new JLabel("Width of environment : ");
		label5.setFont(police1);
		panel1.add(label5, 8);

		JSpinner spinner5 = new JSpinner();
		spinner5.setFont(police1);
		JSpinner.NumberEditor numbereditor5 = new JSpinner.NumberEditor(spinner5);
		spinner5.setEditor(numbereditor5);
		numbereditor5.getModel().setMinimum(10);
		numbereditor5.getModel().setMaximum(500);
		numbereditor5.getModel().setStepSize(10);
		numbereditor5.getModel().setValue(20);
		panel1.add(spinner5, 9);

		JLabel label6 = new JLabel("Altitude of environment : ");
		label6.setFont(police1);
		panel1.add(label6, 10);

		JSpinner spinner6 = new JSpinner();
		spinner6.setFont(police1);
		JSpinner.NumberEditor numbereditor6 = new JSpinner.NumberEditor(spinner6);
		spinner6.setEditor(numbereditor6);
		numbereditor6.getModel().setMinimum(1);
		numbereditor6.getModel().setMaximum(500);
		numbereditor6.getModel().setStepSize(1);
		numbereditor6.getModel().setValue(1);
		panel1.add(spinner6, 11);

		JLabel label7 = new JLabel("Duration of simulation (Hours) : ");
		label7.setFont(police1);
		panel1.add(label7, 12);

		JSpinner spinner7 = new JSpinner();
		spinner7.setFont(police1);
		JSpinner.NumberEditor numbereditor7 = new JSpinner.NumberEditor(spinner7);
		spinner7.setEditor(numbereditor7);
		numbereditor7.getModel().setMinimum(1);
		numbereditor7.getModel().setMaximum(23);
		numbereditor7.getModel().setStepSize(1);
		numbereditor7.getModel().setValue(1);
		panel1.add(spinner7, 13);

		JLabel label8 = new JLabel("Duration of simulation (Minutes) : ");
		label8.setFont(police1);
		panel1.add(label8, 14);

		JSpinner spinner8 = new JSpinner();
		spinner8.setFont(police1);
		JSpinner.NumberEditor numbereditor8 = new JSpinner.NumberEditor(spinner8);
		spinner8.setEditor(numbereditor8);
		numbereditor8.getModel().setMinimum(1);
		numbereditor8.getModel().setMaximum(60);
		numbereditor8.getModel().setStepSize(1);
		numbereditor8.getModel().setValue(5);
		panel1.add(spinner8, 15);

		JLabel label9 = new JLabel("Number of generations : ");
		label9.setFont(police1);
		panel1.add(label9, 16);

		JSpinner spinner9 = new JSpinner();
		spinner9.setFont(police1);
		JSpinner.NumberEditor numbereditor9 = new JSpinner.NumberEditor(spinner9);
		spinner9.setEditor(numbereditor9);
		numbereditor9.getModel().setMinimum(1);
		numbereditor9.getModel().setMaximum(100000);
		numbereditor9.getModel().setStepSize(100);
		numbereditor9.getModel().setValue(1000);
		panel1.add(spinner9, 17);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.setBorder(new TitledBorder(""));
		onglet1.add(panel2, BorderLayout.SOUTH);

		JButton button1 = new JButton("Generate");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int nbr_sensors = (int) spinner1.getValue();
				int nbr_tasks = (int) spinner2.getValue();
				int nbr_bidders = (int) spinner3.getValue();

				int height = (int) spinner4.getValue();
				int width = (int) spinner5.getValue();
				int altitude = (int) spinner6.getValue();
				int nbr_hours = (int) spinner7.getValue();
				int nbr_minutes = (int) spinner8.getValue();
				int generations = (int) spinner9.getValue();

				DecimalFormat format1 = new DecimalFormat();
				format1.setMinimumIntegerDigits(3);
				format1.setMaximumIntegerDigits(3);
				format1.setMinimumFractionDigits(0);
				format1.setMaximumFractionDigits(0);
				format1.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

				String filePath = "datasets/dataset" + String.valueOf(format1.format(nbr_sensors)) + "-" + String.valueOf(format1.format(nbr_tasks)) + "-" + String.valueOf(format1.format(nbr_bidders));
				File directory = new File(filePath);
				directory.mkdir();

				boolean[][][] matrix1 = new boolean[height][width][altitude];
				for (int i = 0; i < matrix1.length; i++) {
					for (int j = 0; j < matrix1[0].length; j++) {
						matrix1[i][j][0] = false;
					}
				}

				LinkedList<Point3D> taskLocations = getTaskLocations(nbr_tasks, height, width, matrix1);
				LinkedList<Point3D> bidderLocations = getBidderLocations(nbr_bidders, height, width, matrix1);

				LinkedList<int[]> arrivalTimes = getArrivalTimes(nbr_hours, nbr_minutes, nbr_tasks);

				new taskGenerator(filePath, nbr_sensors, nbr_tasks, taskLocations, arrivalTimes, generations);
				new bidderGenerator(filePath, nbr_sensors, nbr_bidders, bidderLocations, generations);
				
				try {
					PrintWriter writer1 = new PrintWriter(filePath + "/information.txt");
					writer1.println(String.valueOf(nbr_sensors));
					writer1.println(String.valueOf(nbr_tasks));
					writer1.println(String.valueOf(nbr_bidders));
					writer1.println(String.valueOf(height));
					writer1.println(String.valueOf(width));
					writer1.println(String.valueOf(altitude));
					writer1.println(String.valueOf(nbr_hours));
					writer1.println(String.valueOf(nbr_minutes));
					writer1.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				JOptionPane.showMessageDialog(window1, "Generation completed", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panel2.add(button1, BorderLayout.CENTER);
	}

	public void initializeOnglet2() {
		onglet2 = new JPanel();
		onglet2.setLayout(new BorderLayout());

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(10, 2, 5, 5));
		panel1.setBorder(new TitledBorder(""));
		onglet2.add(panel1, BorderLayout.CENTER);

		JLabel label1 = new JLabel("Number of generations : ");
		label1.setFont(police1);
		panel1.add(label1, 0);

		JSpinner spinner1 = new JSpinner();
		spinner1.setFont(police1);
		JSpinner.NumberEditor numbereditor1 = new JSpinner.NumberEditor(spinner1);
		spinner1.setEditor(numbereditor1);
		numbereditor1.getModel().setMinimum(100);
		numbereditor1.getModel().setMaximum(1000);
		numbereditor1.getModel().setStepSize(50);
		numbereditor1.getModel().setValue(200);
		panel1.add(spinner1, 1);

		JLabel label2 = new JLabel("Number of individuals : ");
		label2.setFont(police1);
		panel1.add(label2, 2);

		JSpinner spinner2 = new JSpinner();
		spinner2.setFont(police1);
		JSpinner.NumberEditor numbereditor2 = new JSpinner.NumberEditor(spinner2);
		spinner2.setEditor(numbereditor2);
		numbereditor2.getModel().setMinimum(50);
		numbereditor2.getModel().setMaximum(200);
		numbereditor2.getModel().setStepSize(10);
		numbereditor2.getModel().setValue(100);
		panel1.add(spinner2, 3);

		JLabel label3 = new JLabel("Tournament size : ");
		label3.setFont(police1);
		panel1.add(label3, 4);

		JSpinner spinner3 = new JSpinner();
		spinner3.setFont(police1);
		JSpinner.NumberEditor numbereditor3 = new JSpinner.NumberEditor(spinner3);
		spinner3.setEditor(numbereditor3);
		numbereditor3.getModel().setMinimum(5);
		numbereditor3.getModel().setMaximum(10);
		numbereditor3.getModel().setStepSize(1);
		numbereditor3.getModel().setValue(10);
		panel1.add(spinner3, 5);

		JLabel label4 = new JLabel("Mating pool size : ");
		label4.setFont(police1);
		panel1.add(label4, 6);

		JSpinner spinner4 = new JSpinner();
		spinner4.setFont(police1);
		JSpinner.NumberEditor numbereditor4 = new JSpinner.NumberEditor(spinner4);
		spinner4.setEditor(numbereditor4);
		numbereditor4.getModel().setMinimum(20);
		numbereditor4.getModel().setMaximum(50);
		numbereditor4.getModel().setStepSize(1);
		numbereditor4.getModel().setValue(50);
		panel1.add(spinner4, 7);

		MaskFormatter mask1 = null;
		MaskFormatter mask2 = null;

		try {
			mask1 = new MaskFormatter("0.##");
			mask2 = new MaskFormatter("0.###");
		} catch (ParseException e2) {
			e2.printStackTrace();
		}

		JLabel label5 = new JLabel("Crossover probability : ");
		label5.setFont(police1);
		panel1.add(label5, 8);

		JFormattedTextField formattedtextfield1 = new JFormattedTextField(mask1);
		formattedtextfield1.setText("0.50");
		formattedtextfield1.setFont(police1);
		panel1.add(formattedtextfield1, 9);

		JLabel label6 = new JLabel("Mutation probability : ");
		label6.setFont(police1);
		panel1.add(label6, 10);

		JFormattedTextField formattedtextfield2 = new JFormattedTextField(mask2);
		formattedtextfield2.setText("0.010");
		formattedtextfield2.setFont(police1);
		panel1.add(formattedtextfield2, 11);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.setBorder(new TitledBorder(""));
		onglet2.add(panel2, BorderLayout.SOUTH);

		JButton button1 = new JButton("Save");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int number_of_generations = (int) spinner1.getValue();
				int number_of_individuals = (int) spinner2.getValue();
				int tournament_size = (int) spinner3.getValue();
				int size_of_mating_pool = (int) spinner4.getValue();

				double probability_of_crossover = Double.valueOf(formattedtextfield1.getText());
				double probabilty_of_mutation = Double.valueOf(formattedtextfield2.getText());

				try {
					PrintWriter writer1 = new PrintWriter("config/AG.txt");
					writer1.println(String.valueOf(number_of_generations));
					writer1.println(String.valueOf(number_of_individuals));
					writer1.println(String.valueOf(tournament_size));
					writer1.println(String.valueOf(size_of_mating_pool));
					writer1.println(String.valueOf(probability_of_crossover));
					writer1.println(String.valueOf(probabilty_of_mutation));
					writer1.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel2.add(button1, BorderLayout.CENTER);
	}

	public void initializeOnglet3() {
		onglet3 = new JPanel();
		onglet3.setLayout(new BorderLayout());

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(10, 2, 5, 5));
		panel1.setBorder(new TitledBorder(""));
		onglet3.add(panel1, BorderLayout.CENTER);

		JLabel label1 = new JLabel("Number of sensors : ");
		label1.setFont(police1);
		panel1.add(label1, 0);

		JTextField textfield1 = new JTextField();
		textfield1.setEditable(false);
		textfield1.setFont(police1);
		panel1.add(textfield1, 1);

		JLabel label2 = new JLabel("Number of tasks : ");
		label2.setFont(police1);
		panel1.add(label2, 2);

		JTextField textfield2 = new JTextField();
		textfield2.setEditable(false);
		textfield2.setFont(police1);
		panel1.add(textfield2, 3);

		JLabel label3 = new JLabel("Number of bidders : ");
		label3.setFont(police1);
		panel1.add(label3, 4);

		JTextField textfield3 = new JTextField();
		textfield3.setEditable(false);
		textfield3.setFont(police1);
		panel1.add(textfield3, 5);

		JLabel label4 = new JLabel("Height of environment : ");
		label4.setFont(police1);
		panel1.add(label4, 6);

		JTextField textfield4 = new JTextField();
		textfield4.setEditable(false);
		textfield4.setFont(police1);
		panel1.add(textfield4, 7);

		JLabel label5 = new JLabel("Width of environment : ");
		label5.setFont(police1);
		panel1.add(label5, 8);

		JTextField textfield5 = new JTextField();
		textfield5.setEditable(false);
		textfield5.setFont(police1);
		panel1.add(textfield5, 9);

		JLabel label6 = new JLabel("Altitude of environment : ");
		label6.setFont(police1);
		panel1.add(label6, 10);

		JTextField textfield6 = new JTextField();
		textfield6.setEditable(false);
		textfield6.setFont(police1);
		panel1.add(textfield6, 11);

		JLabel label7 = new JLabel("Duration of simulation : ");
		label7.setFont(police1);
		panel1.add(label7, 12);

		JTextField textfield7 = new JTextField();
		textfield7.setEditable(false);
		textfield7.setFont(police1);
		panel1.add(textfield7, 13);

		JLabel label8 = new JLabel("Select a dataset : ");
		label8.setFont(police1);
		panel1.add(label8, 14);

		JComboBox<String> combobox1 = new JComboBox<>();
		combobox1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				LinkedList<String> liste1 = open_file(String.valueOf(combobox1.getSelectedItem()));

				textfield1.setText(liste1.get(0));
				textfield2.setText(liste1.get(1));
				textfield3.setText(liste1.get(2));
				textfield4.setText(liste1.get(3));
				textfield5.setText(liste1.get(4));
				textfield6.setText(liste1.get(5));
				textfield7.setText("[" + getStandardFormat("0:0") + "-" + getStandardFormat(String.valueOf(Integer.valueOf(liste1.get(6)) - 1) + ":" + String.valueOf(Integer.valueOf(liste1.get(7)) - 1)) + "]");
			}
		});
		panel1.add(combobox1, 15);

		JLabel label9 = new JLabel("Select an allocation strategy : ");
		label9.setFont(police1);
		panel1.add(label9, 16);

		JComboBox<String> combobox2 = new JComboBox<>();
		panel1.add(combobox2, 17);

		JLabel label10 = new JLabel("Select a fitness function : ");
		label10.setFont(police1);
		panel1.add(label10, 18);

		JComboBox<String> combobox3 = new JComboBox<>();
		panel1.add(combobox3, 19);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1, 2, 5, 5));
		panel2.setBorder(new TitledBorder(""));
		onglet3.add(panel2, BorderLayout.SOUTH);

		JButton button1 = new JButton("Run");
		button1.setEnabled(false);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = "datasets/" + String.valueOf(combobox1.getSelectedItem());

				try {
					PrintWriter writer1 = new PrintWriter("config/criteria.txt");
					writer1.println(String.valueOf(combobox2.getSelectedItem()));
					writer1.println(String.valueOf(combobox3.getSelectedItem()));
					writer1.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				int nbr_sensors = Integer.valueOf(textfield1.getText());
				int nbr_tasks = Integer.valueOf(textfield2.getText());
				int nbr_bidders = Integer.valueOf(textfield3.getText());

				Runtime Instance = Runtime.instance();
				ProfileImpl Profile = new ProfileImpl(false);
				AgentContainer Conteneur = Instance.createAgentContainer(Profile);

				Object[] Parametres1 = { nbr_sensors, nbr_tasks, nbr_bidders, filePath };

				try {
					AgentController Agent = Conteneur.createNewAgent("agentAuctioneer", "package2.agentAuctioneer", Parametres1);
					Agent.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panel2.add(button1, 0);

		JButton button2 = new JButton("Refresh");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1.setEnabled(true);

				combobox1.removeAllItems();
				File[] directories = new File("datasets").listFiles(File::isDirectory);
				for (int i = 0; i < directories.length; i++) {
					combobox1.addItem(String.valueOf(directories[i].getName()));
				}

				combobox2.removeAllItems();
				combobox2.addItem("Exact solution");
				combobox2.addItem("Approximate solution (GA)");

				combobox3.removeAllItems();
				combobox3.addItem("Minimize costs");
				combobox3.addItem("Maximize rewards");
				combobox3.addItem("Maximize utilities");
				combobox3.addItem("Minimize traveled distances");
				combobox3.addItem("Minimize traveling times");
				combobox3.addItem("Minimize consumed energies");
				
				combobox1.setSelectedIndex(0);
				combobox2.setSelectedIndex(1);
				combobox3.setSelectedIndex(4);
			}
		});
		panel2.add(button2, 1);
	}

	public LinkedList<Point3D> getTaskLocations(int nbr_tasks, int height, int width, boolean[][][] matrix1) {
		LinkedList<Point3D> liste1 = new LinkedList<>();

		for (int i = 0; i < nbr_tasks; i++) {
			int x = new Random().nextInt(height);
			int y = new Random().nextInt(width);
			int z = 0;

			while (matrix1[x][y][0] == true) {
				x = new Random().nextInt(height);
				y = new Random().nextInt(width);
			}

			liste1.addLast(new Point3D(x, y, z));
		}

		return liste1;
	}

	public LinkedList<Point3D> getBidderLocations(int nbr_bidders, int height, int width, boolean[][][] matrix1) {
		LinkedList<Point3D> liste1 = new LinkedList<>();

		for (int i = 0; i < nbr_bidders; i++) {
			int x = new Random().nextInt(height);
			int y = new Random().nextInt(width);
			int z = 0;

			while (matrix1[x][y][0] == true) {
				x = new Random().nextInt(height);
				y = new Random().nextInt(width);
			}

			liste1.addLast(new Point3D(x, y, z));
		}

		return liste1;
	}

	public LinkedList<int[]> getArrivalTimes(int nbr_hours, int nbr_minutes, int nbr_tasks) {
		LinkedList<Integer> heures = new LinkedList<>();
		for (int i = 0; i < nbr_hours; i++) {
			heures.addLast(i);
		}

		LinkedList<Integer> minutes = new LinkedList<>();
		for (int i = 0; i < nbr_minutes; i++) {
			minutes.addLast(i);
		}

		LinkedList<int[]> arrivalTimes = new LinkedList<>();
		for (int i = 0; i < nbr_tasks; i++) {
			int[] vector1 = new int[2];

			vector1[0] = heures.get(new Random().nextInt(heures.size()));
			vector1[1] = minutes.get(/*new Random().nextInt(minutes.size())*/i);

			arrivalTimes.addLast(vector1);
		}

		for (int i = 0; i < arrivalTimes.size(); i++) {
			int[] vector1 = arrivalTimes.get(i);

			for (int j = (i + 1); j < arrivalTimes.size(); j++) {
				int[] vector2 = arrivalTimes.get(j);

				if (vector1[0] > vector2[0]) {
					arrivalTimes.set(i, vector2);
					arrivalTimes.set(j, vector1);

					vector1 = arrivalTimes.get(i);
					vector2 = arrivalTimes.get(j);
				} else if (vector1[0] == vector2[0]) {
					if (vector1[1] > vector2[1]) {
						arrivalTimes.set(i, vector2);
						arrivalTimes.set(j, vector1);

						vector1 = arrivalTimes.get(i);
						vector2 = arrivalTimes.get(j);
					}
				}
			}
		}

		return arrivalTimes;
	}

	public LinkedList<String> open_file(String filePath) {
		LinkedList<String> records = new LinkedList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader("datasets/" + filePath + "/information.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				records.addLast(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return records;
	}

	public String getStandardFormat(String chaine1) {
		String chaine2 = "";

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date d = df.parse(chaine1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			chaine2 = df.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return chaine2;
	}
}