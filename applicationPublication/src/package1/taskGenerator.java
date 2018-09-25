package package1;

import java.io.IOException;
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

import javafx.geometry.Point3D;

public class taskGenerator {

	int nbr_sensors = 0;
	int nbr_tasks = 0;

	LinkedList<String> sensors = null;
	LinkedList<String> rewards = null;
	LinkedList<String> working_rates = null;

	String ID = "";
	String priority = "";
	String duration = "";
	String discovering_date = "";
	String starting_date = "";
	String finishing_date = "";

	LinkedList<LinkedList<String>> all_sensors = new LinkedList<>();
	LinkedList<LinkedList<String>> all_rewards = new LinkedList<>();
	LinkedList<LinkedList<String>> all_working_rates = new LinkedList<>();

	LinkedList<String> all_IDs = new LinkedList<>();
	LinkedList<String> all_priorities = new LinkedList<>();
	LinkedList<String> all_durations = new LinkedList<>();
	LinkedList<String> all_discovering_dates = new LinkedList<>();
	LinkedList<String> all_starting_dates = new LinkedList<>();
	LinkedList<String> all_finishing_dates = new LinkedList<>();

	DecimalFormat format1 = null;
	DecimalFormat format2 = null;
	DecimalFormat format3 = null;
	DecimalFormat format4 = null;
	DecimalFormat format5 = null;

	PrintWriter writer1 = null;

	double sigma = 0.0;

	public taskGenerator(String filePath, int nbr_sensors, int nbr_tasks, LinkedList<Point3D> taskLocations, LinkedList<int[]> arrivalTimes, int generations) {
		this.nbr_sensors = nbr_sensors;
		this.nbr_tasks = nbr_tasks;

		format1 = new DecimalFormat();
		format1.setMinimumIntegerDigits(3);
		format1.setMaximumIntegerDigits(3);
		format1.setMinimumFractionDigits(0);
		format1.setMaximumFractionDigits(0);
		format1.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		format2 = new DecimalFormat();
		format2.setMinimumIntegerDigits(1);
		format2.setMaximumIntegerDigits(1);
		format2.setMinimumFractionDigits(1);
		format2.setMaximumFractionDigits(1);
		format2.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		format3 = new DecimalFormat();
		format3.setMinimumIntegerDigits(3);
		format3.setMaximumIntegerDigits(3);
		format3.setMinimumFractionDigits(2);
		format3.setMaximumFractionDigits(2);
		format3.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		format4 = new DecimalFormat();
		format4.setMinimumIntegerDigits(1);
		format4.setMaximumIntegerDigits(1);
		format4.setMinimumFractionDigits(2);
		format4.setMaximumFractionDigits(2);
		format4.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		format5 = new DecimalFormat();
		format5.setMinimumIntegerDigits(2);
		format5.setMaximumIntegerDigits(2);
		format5.setMinimumFractionDigits(0);
		format5.setMaximumFractionDigits(0);
		format5.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		double previousSigma = Double.MAX_VALUE;

		for (int p = 0; p < generations; p++) {

			all_sensors = new LinkedList<>();
			all_rewards = new LinkedList<>();
			all_working_rates = new LinkedList<>();

			all_IDs = new LinkedList<>();
			all_priorities = new LinkedList<>();
			all_durations = new LinkedList<>();
			all_discovering_dates = new LinkedList<>();
			all_starting_dates = new LinkedList<>();
			all_finishing_dates = new LinkedList<>();

			for (int i = 1; i <= this.nbr_tasks; i++) {
				ID = "Task" + String.valueOf(format1.format(i));
				all_IDs.addLast(ID);

				LinkedList<Boolean> skillPositions = generateSkillPositions();

				sensors = new LinkedList<>();
				rewards = new LinkedList<>();
				working_rates = new LinkedList<>();

				int[] vector1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
				double[] vector2 = { 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 1.00 };

				for (int j = 0; j < skillPositions.size(); j++) {
					if (skillPositions.get(j) == true) {
						sensors.addLast(format2.format(1));
						rewards.addLast(format2.format(vector1[new Random().nextInt(vector1.length)]));
						working_rates.addLast(format4.format(vector2[new Random().nextInt(vector2.length)]));
					} else {
						sensors.addLast(format2.format(0));
						rewards.addLast(format2.format(0));
						working_rates.addLast(format4.format(0));
					}
				}

				all_sensors.addLast(sensors);
				all_rewards.addLast(rewards);
				all_working_rates.addLast(working_rates);

				priority = format5.format(new Random().nextInt(10));
				all_priorities.addLast(priority);

				int randomValue1 = new Random().nextInt(5) + 1;
				duration = format5.format(randomValue1);
				all_durations.addLast(duration);

				discovering_date = getStandardFormat(arrivalTimes.get(i - 1)[0] + ":" + arrivalTimes.get(i - 1)[1]);
				all_discovering_dates.addLast(discovering_date);

				int randomValue2 = new Random().nextInt(10);
				while (randomValue2 < 5) {
					randomValue2 = new Random().nextInt(10);
				}
				starting_date = addMinutes(discovering_date, randomValue2);
				all_starting_dates.addLast(starting_date);

				int randomValue3 = new Random().nextInt(10);
				while (randomValue3 < 5) {
					randomValue3 = new Random().nextInt(10);
				}
				finishing_date = addMinutes(starting_date, randomValue1 + randomValue3);
				all_finishing_dates.addLast(finishing_date);
			}

			sigma = new displayMetrics(nbr_sensors, all_sensors).returnSigma();
			
			if (sigma < previousSigma) {
				previousSigma = sigma;
				
				try {
					writer1 = new PrintWriter(filePath + "/tasks.txt");

					writer1.println("**" + returnStars(28) + returnStars(String.valueOf(this.nbr_tasks).length()));
					writer1.println("*Task test instances (" + String.valueOf(this.nbr_tasks) + " tasks)*");
					writer1.println("**" + returnStars(28) + returnStars(String.valueOf(this.nbr_tasks).length()));
					writer1.println("");
					writer1.println("Each line of the file contains the following data:");
					writer1.println("");
					writer1.println("		ID|SE|RE|WR|LO|TI");
					writer1.println("");
					writer1.println("Where");
					writer1.println("");
					writer1.println("		ID = ID of the task.");
					writer1.println("		SE = SEnsors required by the task.");
					writer1.println("		RE = REwards of task sensors.");
					writer1.println("		WR = Working Rates of task sensors.");
					writer1.println("		LO = LOcation of the task.");
					writer1.println("		TI = Temporal Information about the task [Priority, Duration, Discovering date, Starting date, Finishing date].");
					writer1.println("");

					int n1 = 5;
					int n2 = 3 * nbr_sensors + (nbr_sensors - 1) * 2;
					int n3 = 3 * nbr_sensors + (nbr_sensors - 1) * 2;
					int n4 = 4 * nbr_sensors + (nbr_sensors - 1) * 2;
					int n5 = 22;
					int n6 = 27;
					int n7 = n1 + n2 + n3 + n4 + n5 + n6 + 18;

					writer1.println("|ID" + returnSpaces(n1) + "|SE" + returnSpaces(n2) + "|RE" + returnSpaces(n3) + "|WR" + returnSpaces(n4) + "|LO" + returnSpaces(n5) + "|TI" + returnSpaces(n6) + "|");
					writer1.println(returnDashes(n7));

					for (int i = 0; i < all_IDs.size(); i++) {
						writer1.println("|" + all_IDs.get(i) + "|" + all_sensors.get(i) + "|" + all_rewards.get(i) + "|" + all_working_rates.get(i) + "|" + "(" + format3.format(taskLocations.get(i).getX()) + ", " + format3.format(taskLocations.get(i).getY()) + ", "
								+ format3.format(taskLocations.get(i).getZ()) + ")" + "|" + "[" + all_priorities.get(i) + ", " + all_durations.get(i) + ", " + all_discovering_dates.get(i) + ", " + all_starting_dates.get(i) + ", " + all_finishing_dates.get(i) + "]|");
					}

					writer1.println("SD = " + String.valueOf(sigma));

					writer1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public LinkedList<Boolean> generateSkillPositions() {
		LinkedList<Boolean> liste1 = new LinkedList<>();
		
		for (int i = 0; i < nbr_sensors; i++) {
			if (new Random().nextDouble() >= 0.1) {
				liste1.addLast(true);
			} else {
				liste1.addLast(true);
			}
		}

		return liste1;
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

	public String addMinutes(String chaine1, int minutes) {
		String chaine2 = "";

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date d = df.parse(chaine1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, minutes);
			chaine2 = df.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return chaine2;
	}

	public String returnSpaces(int nbrSpaces) {
		String spaces = "";

		for (int i = 0; i < nbrSpaces; i++) {
			spaces = spaces + " ";
		}

		return spaces;
	}

	public String returnDashes(int nbrDashes) {
		String dashes = "-";

		for (int i = 0; i < nbrDashes; i++) {
			dashes = dashes + "-";
		}

		return dashes;
	}

	public String returnStars(int nbrDashes) {
		String dashes = "";

		for (int i = 0; i < nbrDashes; i++) {
			dashes = dashes + "*";
		}

		return dashes;
	}
}