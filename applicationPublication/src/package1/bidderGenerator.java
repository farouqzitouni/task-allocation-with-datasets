package package1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Point3D;
import package3.writeXMLFile;

public class bidderGenerator {

	int nbr_sensors = 0;
	int nbr_bidders = 0;

	LinkedList<String> sensors = null;
	LinkedList<String> costs = null;
	LinkedList<String> working_currents = null;

	String ID = "";
	String velocity = "";
	String energy_gauge = "";
	String mass = "";
	String altitude = "";
	String battery_voltage = "";
	String battery_capacity = "";
	String peukert_exponent = "";
	String c_rate = "";

	LinkedList<LinkedList<String>> all_sensors = new LinkedList<>();
	LinkedList<LinkedList<String>> all_costs = new LinkedList<>();
	LinkedList<LinkedList<String>> all_working_currents = new LinkedList<>();
	LinkedList<LinkedList<String>> list_of_candidate_bidders_by_sensors = null;

	LinkedList<String> all_IDs = new LinkedList<>();
	LinkedList<String> all_velocities = new LinkedList<>();
	LinkedList<String> all_energy_gauges = new LinkedList<>();
	LinkedList<String> all_masses = new LinkedList<>();
	LinkedList<String> all_altitudes = new LinkedList<>();
	LinkedList<String> all_battery_voltages = new LinkedList<>();
	LinkedList<String> all_battery_capacities = new LinkedList<>();
	LinkedList<String> all_peukert_exponents = new LinkedList<>();
	LinkedList<String> all_c_rates = new LinkedList<>();

	DecimalFormat format1 = null;
	DecimalFormat format2 = null;
	DecimalFormat format3 = null;
	DecimalFormat format4 = null;
	DecimalFormat format5 = null;

	PrintWriter writer1 = null;

	double sigma = 0.0;

	public bidderGenerator(String filePath, int nbr_sensors, int nbr_bidders, LinkedList<Point3D> bidderLocations, int generations) {
		this.nbr_sensors = nbr_sensors;
		this.nbr_bidders = nbr_bidders;

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
		format3.setMinimumIntegerDigits(2);
		format3.setMaximumIntegerDigits(2);
		format3.setMinimumFractionDigits(2);
		format3.setMaximumFractionDigits(2);
		format3.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		format4 = new DecimalFormat();
		format4.setMinimumIntegerDigits(3);
		format4.setMaximumIntegerDigits(3);
		format4.setMinimumFractionDigits(2);
		format4.setMaximumFractionDigits(2);
		format4.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		format5 = new DecimalFormat();
		format5.setMinimumIntegerDigits(1);
		format5.setMaximumIntegerDigits(1);
		format5.setMinimumFractionDigits(5);
		format5.setMaximumFractionDigits(5);
		format5.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		double previousSigma = Double.MAX_VALUE;

		for (int p = 0; p < generations; p++) {
			all_IDs = new LinkedList<>();
			all_velocities = new LinkedList<>();
			all_energy_gauges = new LinkedList<>();
			all_masses = new LinkedList<>();
			all_altitudes = new LinkedList<>();
			all_battery_capacities = new LinkedList<>();
			all_peukert_exponents = new LinkedList<>();
			all_c_rates = new LinkedList<>();

			all_sensors = new LinkedList<>();
			all_costs = new LinkedList<>();
			all_working_currents = new LinkedList<>();

			for (int i = 1; i <= this.nbr_bidders; i++) {
				ID = "Bidder" + String.valueOf(format1.format(i));
				all_IDs.addLast(ID);

				LinkedList<Boolean> skillPositions = generateSkillPositions();

				sensors = new LinkedList<>();
				costs = new LinkedList<>();
				working_currents = new LinkedList<>();

				int[] vector1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
				int[] vector2 = { 4, 20, 2, 1, 10 };
				int[] vector3 = { 30, 40, 50, 60 };
				int[] vector4 = { 200, 300, 400, 500 };

				for (int j = 0; j < skillPositions.size(); j++) {
					if (skillPositions.get(j) == true) {
						sensors.addLast(format2.format(1));
						costs.addLast(format2.format(vector1[new Random().nextInt(vector1.length)]));
						working_currents.addLast(format3.format(vector2[new Random().nextInt(vector2.length)]));
					} else {
						sensors.addLast(format2.format(0));
						costs.addLast(format2.format(0));
						working_currents.addLast(format3.format(0));
					}
				}

				all_sensors.addLast(sensors);
				all_costs.addLast(costs);
				all_working_currents.addLast(working_currents);

				velocity = format4.format(vector3[new Random().nextInt(vector3.length)]);
				all_velocities.addLast(velocity);

				energy_gauge = format4.format(100.0);
				all_energy_gauges.addLast(energy_gauge);

				mass = format4.format(vector4[new Random().nextInt(vector4.length)]);
				all_masses.addLast(mass);

				altitude = format4.format(Math.sqrt(Math.pow(bidderLocations.get(i - 1).getZ(), 2.0)));
				all_altitudes.addLast(altitude);

				battery_voltage = format2.format(4.5);
				all_battery_voltages.addLast(battery_voltage);

				battery_capacity = format2.format(4);
				all_battery_capacities.addLast(battery_capacity);

				peukert_exponent = format2.format(1.3);
				all_peukert_exponents.addLast(peukert_exponent);

				c_rate = format3.format(20.0);
				all_c_rates.addLast(c_rate);
			}

			sigma = new displayMetrics(nbr_sensors, all_sensors).returnSigma();
			if (sigma < previousSigma) {
				previousSigma = sigma;
				
				try {
					writer1 = new PrintWriter(filePath + "/bidders.txt");

					writer1.println("**" + returnStars(32) + returnStars(String.valueOf(this.nbr_bidders).length()));
					writer1.println("*Bidder test instances (" + String.valueOf(this.nbr_bidders) + " bidders)*");
					writer1.println("**" + returnStars(32) + returnStars(String.valueOf(this.nbr_bidders).length()));
					writer1.println("");
					writer1.println("Each line of the file contains the following data:");
					writer1.println("");
					writer1.println("		ID|SE|CO|WC|LO|PI");
					writer1.println("");
					writer1.println("Where");
					writer1.println("");
					writer1.println("		ID = ID of the bidder.");
					writer1.println("		SE = SEnsors possessed by the bidder.");
					writer1.println("		CO = COsts of bidder sensors.");
					writer1.println("		WC = Working Current of bidder sensors.");
					writer1.println("		LO = LOcation of the bidder.");
					writer1.println("		PI = Physical Information about the bidder [Velocity, Gauge of energy, Mass, Altitude, Battery Voltage, Battery Capacity, Peukert's Exponent, C-rate].");
					writer1.println("");

					int n1 = 7;
					int n2 = 3 * nbr_sensors + (nbr_sensors - 1) * 2;
					int n3 = 3 * nbr_sensors + (nbr_sensors - 1) * 2;
					int n4 = 5 * nbr_sensors + (nbr_sensors - 1) * 2;
					int n5 = 22;
					int n6 = 52;
					int n7 = n1 + n2 + n3 + n4 + n5 + n6 + 18;

					writer1.println("|ID" + returnSpaces(n1) + "|SE" + returnSpaces(n2) + "|CO" + returnSpaces(n3) + "|WC" + returnSpaces(n4) + "|LO" + returnSpaces(n5) + "|PI" + returnSpaces(n6) + "|");
					writer1.println(returnDashes(n7));

					for (int i = 0; i < all_IDs.size(); i++) {
						writer1.println("|" + all_IDs.get(i) + "|" + all_sensors.get(i) + "|" + all_costs.get(i) + "|" + all_working_currents.get(i) + "|" + "(" + format4.format(bidderLocations.get(i).getX()) + ", " + format4.format(bidderLocations.get(i).getY()) + ", "
								+ format4.format(bidderLocations.get(i).getZ()) + ")" + "|" + "[" + all_velocities.get(i) + ", " + all_energy_gauges.get(i) + ", " + all_masses.get(i) + ", " + all_altitudes.get(i) + ", " + all_battery_voltages.get(i) + ", " + all_battery_capacities.get(i) + ", "
								+ all_peukert_exponents.get(i) + ", " + all_c_rates.get(i) + "]|");
					}

					writer1.println("SD = " + String.valueOf(sigma));

					writer1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				list_of_candidate_bidders_by_sensors = new LinkedList<>();
				for (int i = 0; i < nbr_sensors; i++) {
					LinkedList<String> liste1 = new LinkedList<>();
					liste1.addLast("Bidder000");
					
					list_of_candidate_bidders_by_sensors.addLast(liste1);
				}
				
				for (int i = 0; i < all_IDs.size(); i++) {
					String bidder_ID = all_IDs.get(i);
					LinkedList<String> bidder_sensors = all_sensors.get(i);
					
					for (int j = 0; j < bidder_sensors.size(); j++) {
						if (bidder_sensors.get(j).equals("1.0")) {
							list_of_candidate_bidders_by_sensors.get(j).addLast(bidder_ID);
						}
					}
				}
			}
		}
		
		long start = System.currentTimeMillis();
		
		List<Set<String>> sets = new ArrayList<Set<String>>();
		for (int i = 0; i < list_of_candidate_bidders_by_sensors.size(); i++) {
			sets.add(new HashSet<String>(list_of_candidate_bidders_by_sensors.get(i)));
		}
		Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
		
		Iterator<List<String>> iter1 = cartesianSet.iterator();
		LinkedList<String[]> all_allocations = new LinkedList<>();
		while (iter1.hasNext()) {
			LinkedList<String> allocation = new LinkedList<>();

			List<String> liste2 = iter1.next();
			for (int j = 0; j < liste2.size(); j++) {
				allocation.addLast(liste2.get(j));
			}
			
			String[] values1 = new String[nbr_sensors];
			for (int j = 0; j < values1.length; j++) {
				values1[j] = allocation.get(j);
			}
			
			all_allocations.addLast(values1);
		}
		new writeXMLFile(all_allocations, filePath);
		
		long end = System.currentTimeMillis();

		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.println(formatter.format((end - start) / 1000d));
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