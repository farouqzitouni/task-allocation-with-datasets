package package3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class ES {

	double optimal_fitness_value = 0.0;
	double estimated_execution_time = 0.0;

	String function = "";

	LinkedList<bids> list_of_candidate_bidders = null;

	LinkedList<LinkedList<String>> list_of_candidate_bidders_by_sensors = null;
	LinkedList<LinkedList<String>> list_of_valide_allocations = new LinkedList<>();

	LinkedList<Double> list_of_fitness_values = new LinkedList<>();

	LinkedList<String> optimal_allocation = new LinkedList<>();
	LinkedList<String> optimal_coalition = new LinkedList<>();

	public ES(String filePath, LinkedList<Double> task_sensors, LinkedList<bids> list_of_candidate_bidders, LinkedList<LinkedList<String>> list_of_all_allocations) {
		LinkedList<String> criteria = open_the_file("config", "criteria.txt");
		this.function = criteria.get(1);

		list_of_candidate_bidders_by_sensors = new LinkedList<>();
		for (int i = 0; i < task_sensors.size(); i++) {
			LinkedList<String> liste1 = new LinkedList<>();
			list_of_candidate_bidders_by_sensors.addLast(liste1);
		}

		this.list_of_candidate_bidders = new LinkedList<>();
		this.list_of_candidate_bidders.addAll(list_of_candidate_bidders);

		for (int i = 0; i < this.list_of_candidate_bidders.size(); i++) {
			String bidder_ID = this.list_of_candidate_bidders.get(i).get_bidder_ID();
			LinkedList<Double> commun_sensors = this.list_of_candidate_bidders.get(i).get_commun_sensors();

			for (int j = 0; j < commun_sensors.size(); j++) {
				if (commun_sensors.get(j) == 1.0) {
					list_of_candidate_bidders_by_sensors.get(j).addLast(bidder_ID);
				}
			}
		}
		
		for (int i = 0; i < list_of_candidate_bidders_by_sensors.size(); i++) {
			if (list_of_candidate_bidders_by_sensors.get(i).isEmpty() == true) {
				list_of_candidate_bidders_by_sensors.get(i).addLast("Bidder000");
			}
		}

		long start = System.currentTimeMillis();
		
		list_of_valide_allocations = new LinkedList<>();
		for (int i = 0; i < list_of_all_allocations.size(); i++) {
			LinkedList<String> allocation = list_of_all_allocations.get(i);
			
			if (getTheAllocationIsValide(allocation) == true) {
				list_of_valide_allocations.addLast(allocation);
			}
		}
		list_of_fitness_values = fitness_computing(list_of_valide_allocations);
		
		long end = System.currentTimeMillis();

		int indexOfOptimalAllocation = -1;
		if (function.startsWith("Minimize")) {
			indexOfOptimalAllocation = getTheIndexOfMinValue(list_of_fitness_values);
		} else if (function.startsWith("Maximize")) {
			indexOfOptimalAllocation = getTheIndexOfMaxValue(list_of_fitness_values);
		}

		optimal_allocation = list_of_valide_allocations.get(indexOfOptimalAllocation);
		optimal_coalition = getTheCoalition(optimal_allocation);

		optimal_fitness_value = list_of_fitness_values.get(indexOfOptimalAllocation);
		estimated_execution_time = ((end - start) / 1000d);
	}

	public LinkedList<String> open_the_file(String filePath, String fileName) {
		LinkedList<String> records = new LinkedList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath + "/" + fileName));
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
	
	public boolean getTheAllocationIsValide(LinkedList<String> allocation) {
		boolean theAllocationIsValide = true;
		
		int i = 0;
		while ((i < allocation.size())&&(theAllocationIsValide == true)) {
			if (list_of_candidate_bidders_by_sensors.get(i).contains(allocation.get(i)) == true) {
				i = i + 1;
			} else {
				theAllocationIsValide = false;
			}
		}

		return theAllocationIsValide;
	}

	public LinkedList<Double> fitness_computing(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		if (function.equals("Minimize costs")) {
			fitness_values = costs(allocations);
		} else if (function.equals("Maximize rewards")) {
			fitness_values = rewards(allocations);
		} else if (function.equals("Maximize utilities")) {
			fitness_values = utilities(allocations);
		} else if (function.equals("Minimize traveled distances")) {
			fitness_values = traveled_distances(allocations);
		} else if (function.equals("Minimize traveling times")) {
			fitness_values = traveling_times(allocations);
		} else if (function.equals("Minimize consumed energies")) {
			fitness_values = energies(allocations);
		}

		return fitness_values;
	}

	public LinkedList<String> getTheCoalition(LinkedList<String> allocation) {
		LinkedList<String> coalition = new LinkedList<>();

		for (int i = 0; i < allocation.size(); i++) {
			if (!allocation.get(i).equals("Bidder000")) {
				if (!coalition.contains(allocation.get(i))) {
					coalition.addLast(allocation.get(i));
				}
			}
		}

		return coalition;
	}

	public int getTheBidderIndex(String bidder_ID) {
		int bidder_Index = -1;

		int i = 0;
		boolean found = false;
		while ((i < list_of_candidate_bidders.size()) && (found == false)) {
			if (list_of_candidate_bidders.get(i).get_bidder_ID().equals(bidder_ID)) {
				found = true;
				bidder_Index = i;
			} else {
				i = i + 1;
			}
		}

		return bidder_Index;
	}

	LinkedList<Double> getTheRatesOfUsedSensors(LinkedList<String> coalition, LinkedList<String> allocation) {
		LinkedList<Double> theRatesOfUsedSensors = new LinkedList<>();

		for (int i = 0; i < coalition.size(); i++) {
			String bidder_ID = coalition.get(i);
			int theBidderIndex = getTheBidderIndex(bidder_ID);

			if (theBidderIndex != -1) {
				LinkedList<Double> bidder_sensors = list_of_candidate_bidders.get(theBidderIndex).get_commun_sensors();

				double counter1 = 0.0;
				for (int j = 0; j < allocation.size(); j++) {
					if (allocation.get(j).equals(bidder_ID)) {
						counter1 = counter1 + 1.0;
					}
				}

				double counter2 = 0.0;
				for (int j = 0; j < bidder_sensors.size(); j++) {
					if (bidder_sensors.get(j) == 1.0) {
						counter2 = counter2 + 1.0;
					}
				}

				double rate = (counter1 / counter2);

				theRatesOfUsedSensors.addLast(rate);
			}
		}

		return theRatesOfUsedSensors;
	}

	public LinkedList<Double> costs(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		double total_fitness = 0.0;

		for (int i = 0; i < allocations.size(); i++) {
			LinkedList<String> allocation = allocations.get(i);

			LinkedList<String> coalition = getTheCoalition(allocation);
			LinkedList<Double> rates_of_used_sensors = getTheRatesOfUsedSensors(coalition, allocation);

			double fitness = 0.0;

			for (int p = 0; p < coalition.size(); p++) {
				int bidder_Index = getTheBidderIndex(coalition.get(p));
				LinkedList<Double> commun_costs = list_of_candidate_bidders.get(bidder_Index).get_commun_costs();
				double estimated_gain = list_of_candidate_bidders.get(bidder_Index).get_estimated_gain();

				double sum1 = 0.0;

				for (int q = 0; q < allocation.size(); q++) {
					if (allocation.get(q).equals(coalition.get(p))) {
						sum1 = sum1 + commun_costs.get(q);
					}
				}

				fitness = fitness + (sum1 / (estimated_gain * rates_of_used_sensors.get(p)));
			}

			fitness = fitness * (double) coalition.size();
			fitness_values.addLast(fitness);

			total_fitness = total_fitness + fitness;
		}

		// fitness_values = scaling(fitness_values);

		// for (int i = 0; i < fitness_values.size(); i++) {
		// fitness_values.set(i, fitness_values.get(i) / total_fitness);
		// }

		return fitness_values;
	}

	public LinkedList<Double> rewards(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		double total_fitness = 0.0;

		for (int i = 0; i < allocations.size(); i++) {
			LinkedList<String> allocation = allocations.get(i);

			LinkedList<String> coalition = getTheCoalition(allocation);
			LinkedList<Double> rates_of_used_sensors = getTheRatesOfUsedSensors(coalition, allocation);

			double fitness = 0.0;

			for (int p = 0; p < coalition.size(); p++) {
				int bidder_Index = getTheBidderIndex(coalition.get(p));
				LinkedList<Double> commun_rewards = list_of_candidate_bidders.get(bidder_Index).get_commun_rewards();
				double estimated_gain = list_of_candidate_bidders.get(bidder_Index).get_estimated_gain();

				double sum1 = 0.0;

				for (int q = 0; q < allocation.size(); q++) {
					if (allocation.get(q).equals(coalition.get(p))) {
						sum1 = sum1 + commun_rewards.get(q);
					}
				}

				fitness = fitness + (sum1 * estimated_gain * rates_of_used_sensors.get(p));
			}

			fitness = fitness / (double) coalition.size();
			fitness_values.addLast(fitness);

			total_fitness = total_fitness + fitness;
		}

		// fitness_values = scaling(fitness_values);

		// for (int i = 0; i < fitness_values.size(); i++) {
		// fitness_values.set(i, fitness_values.get(i) / total_fitness);
		// }

		return fitness_values;
	}

	public LinkedList<Double> utilities(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		LinkedList<Double> costs = costs(allocations);
		LinkedList<Double> rewards = rewards(allocations);

		for (int i = 0; i < costs.size(); i++) {
			fitness_values.addLast(Math.max(rewards.get(i) - costs.get(i), 0.0));
		}

		// fitness_values = scaling(fitness_values);

		// for (int i = 0; i < fitness_values.size(); i++) {
		// fitness_values.set(i, fitness_values.get(i) / total_fitness);
		// }

		return fitness_values;
	}

	public LinkedList<Double> traveled_distances(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		double total_fitness = 0.0;

		for (int i = 0; i < allocations.size(); i++) {
			LinkedList<String> allocation = allocations.get(i);

			LinkedList<String> coalition = getTheCoalition(allocation);
			LinkedList<Double> rates_of_used_sensors = getTheRatesOfUsedSensors(coalition, allocation);

			double fitness = 0.0;

			for (int p = 0; p < coalition.size(); p++) {
				int bidder_Index = getTheBidderIndex(coalition.get(p));
				double traveled_distance = list_of_candidate_bidders.get(bidder_Index).get_estimated_traveled_distance();
				double estimated_gain = list_of_candidate_bidders.get(bidder_Index).get_estimated_gain();

				fitness = fitness + (traveled_distance / (estimated_gain * rates_of_used_sensors.get(p)));
			}

			fitness = fitness * (double) coalition.size();
			fitness_values.addLast(fitness);

			total_fitness = total_fitness + fitness;
		}

		// fitness_values = scaling(fitness_values);

		// for (int i = 0; i < fitness_values.size(); i++) {
		// fitness_values.set(i, fitness_values.get(i) / total_fitness);
		// }

		return fitness_values;
	}

	public LinkedList<Double> traveling_times(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		double total_fitness = 0.0;

		for (int i = 0; i < allocations.size(); i++) {
			LinkedList<String> allocation = allocations.get(i);

			LinkedList<String> coalition = getTheCoalition(allocation);
			LinkedList<Double> rates_of_used_sensors = getTheRatesOfUsedSensors(coalition, allocation);

			double fitness = 0.0;

			for (int p = 0; p < coalition.size(); p++) {
				int bidder_Index = getTheBidderIndex(coalition.get(p));
				double traveling_time = list_of_candidate_bidders.get(bidder_Index).get_estimated_traveling_time();
				double estimated_gain = list_of_candidate_bidders.get(bidder_Index).get_estimated_gain();

				fitness = fitness + (traveling_time / (estimated_gain * rates_of_used_sensors.get(p)));
			}

			fitness = fitness * (double) coalition.size();
			fitness_values.addLast(fitness);

			total_fitness = total_fitness + fitness;
		}

		// fitness_values = scaling(fitness_values);

		// for (int i = 0; i < fitness_values.size(); i++) {
		// fitness_values.set(i, fitness_values.get(i) / total_fitness);
		// }

		return fitness_values;
	}

	public LinkedList<Double> energies(LinkedList<LinkedList<String>> allocations) {
		LinkedList<Double> fitness_values = new LinkedList<>();

		double total_fitness = 0.0;
		for (int i = 0; i < allocations.size(); i++) {
			LinkedList<String> allocation = allocations.get(i);

			LinkedList<String> coalition = getTheCoalition(allocation);
			LinkedList<Double> rates_of_used_sensors = getTheRatesOfUsedSensors(coalition, allocation);

			double fitness1 = 0.0;
			double fitness2 = 0.0;

			for (int p = 0; p < coalition.size(); p++) {
				int bidder_Index = getTheBidderIndex(coalition.get(p));
				double stimated_consumed_energy_by_displacement = list_of_candidate_bidders.get(bidder_Index).get_estimated_consumed_energy_by_displacement();
				LinkedList<Double> estimated_consumed_energy_by_sensors = list_of_candidate_bidders.get(bidder_Index).get_estimated_consumed_energy_by_sensors();
				double estimated_gain = list_of_candidate_bidders.get(bidder_Index).get_estimated_gain();

				fitness1 = fitness1 + (stimated_consumed_energy_by_displacement / (estimated_gain * rates_of_used_sensors.get(p)));

				double sum1 = 0.0;

				for (int q = 0; q < allocation.size(); q++) {
					if (allocation.get(q).equals(coalition.get(p))) {
						sum1 = sum1 + estimated_consumed_energy_by_sensors.get(q);
					}
				}

				fitness2 = fitness2 + (sum1 / (estimated_gain * rates_of_used_sensors.get(p)));
			}

			fitness1 = fitness1 * (double) coalition.size();
			fitness2 = fitness2 * (double) coalition.size();

			double fitness = fitness1 + fitness2;
			fitness_values.addLast(fitness);

			total_fitness = total_fitness + fitness;
		}

		// fitness_values = scaling(fitness_values);

		// for (int i = 0; i < fitness_values.size(); i++) {
		// fitness_values.set(i, fitness_values.get(i) / total_fitness);
		// }

		return fitness_values;
	}

	public LinkedList<Double> scaling(LinkedList<Double> liste1) {
		LinkedList<Double> liste2 = new LinkedList<>();

		double sum1 = 0.0;
		for (int i = 0; i < liste1.size(); i++) {
			sum1 = sum1 + liste1.get(i);
		}
		double average = sum1 / (double) liste1.size();

		double sum2 = 0.0;
		for (int i = 0; i < liste1.size(); i++) {
			sum2 = sum2 + (liste1.get(i) - average) * (liste1.get(i) - average);
		}
		double variance = sum2 / (double) liste1.size();
		double standard_deviation = Math.sqrt(variance);

		for (int i = 0; i < liste1.size(); i++) {
			liste2.addLast((liste1.get(i) - average - standard_deviation) / (standard_deviation));
		}

		return liste2;
	}

	public int getTheIndexOfMaxValue(LinkedList<Double> liste1) {
		int theIndexOfMaxValue = -1;

		double theMaxValue = liste1.get(0);
		theIndexOfMaxValue = 0;

		for (int i = 1; i < liste1.size(); i++) {
			if (liste1.get(i) > theMaxValue) {
				theMaxValue = liste1.get(i);
				theIndexOfMaxValue = i;
			}
		}

		return theIndexOfMaxValue;
	}

	public int getTheIndexOfMinValue(LinkedList<Double> liste1) {
		int theIndexOfMinValue = -1;

		double theMinValue = liste1.get(0);
		theIndexOfMinValue = 0;

		for (int i = 1; i < liste1.size(); i++) {
			if (liste1.get(i) < theMinValue) {
				theMinValue = liste1.get(i);
				theIndexOfMinValue = i;
			}
		}

		return theIndexOfMinValue;
	}

	public LinkedList<String> getTheOptimalAllocation() {
		return optimal_allocation;
	}

	public LinkedList<String> getTheOptimalCoalition() {
		return optimal_coalition;
	}

	public double getTheOptimalFitnessValue() {
		return optimal_fitness_value;
	}

	public double getTheEstimatedExecutionTime() {
		return estimated_execution_time;
	}
}