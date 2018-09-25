package package2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import package3.ES;
import package3.GA;
import package3.bids;
import package3.readXMLFile;
import package3.tasks;

@SuppressWarnings("serial")
public class agentAuctioneer extends Agent {

	DFAgentDescription agentDescription = null;
	ACLMessage message1 = null;

	Object[] parametres1 = null;
	Object[] message_content1 = null;

	String filePath = "";

	int nbr_sensors = 0;
	int nbr_tasks = 0;
	int nbr_bidders = 0;
	int counter1 = 0;

	double number_of_done_tasks = 0.0;
	double number_of_undone_tasks = 0.0;
	double total_execution_time = 0.0;
	double total_fitness = 0.0;

	boolean logic1 = false;

	bids[][] matrix_of_bids = null;

	LinkedList<tasks> list_of_all_tasks = new LinkedList<>();
	LinkedList<tasks> list_of_dicovered_tasks = new LinkedList<>();

	LinkedList<String> list_of_all_bidders_ID = new LinkedList<>();
	LinkedList<String> list_of_dicovered_tasks_ID = new LinkedList<>();
	
	LinkedList<LinkedList<String>> list_of_all_allocations = new LinkedList<>();

	Runtime Instance1 = null;
	ProfileImpl Profile1 = null;
	AgentContainer Conteneur1 = null;

	protected void setup() {
		try {
			agentDescription = new DFAgentDescription();
			agentDescription.setName(getAID());
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		parametres1 = this.getArguments();

		ParallelBehaviour parallelbehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);

		parallelbehaviour.addSubBehaviour(new OneShotBehaviour(this) {
			@Override
			public void action() {
				nbr_sensors = (int) parametres1[0];
				nbr_tasks = (int) parametres1[1];
				nbr_bidders = (int) parametres1[2];

				filePath = (String) parametres1[3];
				
				//long start = System.currentTimeMillis();
				list_of_all_allocations = new readXMLFile(filePath, nbr_sensors).return_list_of_allocations();
				//long end = System.currentTimeMillis();

				//NumberFormat formatter = new DecimalFormat("#0.00000");
				//System.out.println(formatter.format((end - start) / 1000d));

				// System.out.println(nbr_sensors);
				// System.out.println(nbr_tasks);
				// System.out.println(nbr_bidders);
				// System.out.println(filePath);
			}
		});

		parallelbehaviour.addSubBehaviour(new OneShotBehaviour(this) {
			@Override
			public void action() {
				LinkedList<String> liste1 = open_the_file(filePath, "tasks.txt");
				for (int i = 19; i < (liste1.size() - 1); i++) {
					LinkedList<String> liste2 = spliter1(liste1.get(i), "|");

					String ID = liste2.get(0);
					String SE = liste2.get(1);
					String RE = liste2.get(2);
					String WR = liste2.get(3);
					String LO = liste2.get(4);
					String TI = liste2.get(5);

					tasks task = new tasks();

					task.set_task_ID(ID);
					task.set_task_sensors(spliter2(SE, "[], "));
					task.set_task_rewards(spliter2(RE, "[], "));
					task.set_task_working_rates(spliter2(WR, "[], "));
					task.set_task_X(spliter2(LO, "(), ").get(0));
					task.set_task_Y(spliter2(LO, "(), ").get(1));
					task.set_task_Z(spliter2(LO, "(), ").get(2));
					task.set_task_priority(Double.valueOf(spliter1(TI, "[], ").get(0)));
					task.set_task_duration(Double.valueOf(spliter1(TI, "[], ").get(1)));
					task.set_task_discovering_date(spliter1(TI, "[], ").get(2));
					task.set_task_starting_date(spliter1(TI, "[], ").get(3));
					task.set_task_finishing_date(spliter1(TI, "[], ").get(4));
					task.set_task_state("UNDISCOVERED");

					list_of_all_tasks.addLast(task);

					// task.display_my_information();
				}
			}
		});

		parallelbehaviour.addSubBehaviour(new OneShotBehaviour(this) {
			@Override
			public void action() {
				Instance1 = Runtime.instance();
				Profile1 = new ProfileImpl(false);
				Conteneur1 = Instance1.createAgentContainer(Profile1);

				LinkedList<String> liste1 = open_the_file(filePath, "bidders.txt");
				for (int i = 19; i < (liste1.size() - 1); i++) {
					LinkedList<String> liste2 = spliter1(liste1.get(i), "|");

					String ID = liste2.get(0);
					String SE = liste2.get(1);
					String CO = liste2.get(2);
					String WC = liste2.get(3);
					String LO = liste2.get(4);
					String PI = liste2.get(5);

					list_of_all_bidders_ID.addLast(ID);

					try {
						Object[] Parametres1 = { nbr_sensors, ID, SE, CO, WC, LO, PI };
						AgentController Agent = Conteneur1.createNewAgent(ID, "package2.agentBidder", Parametres1);
						Agent.start();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		parallelbehaviour.addSubBehaviour(new TickerBehaviour(this, 60000) {
			@Override
			protected void onTick() {
				DateFormat df = new SimpleDateFormat("HH:mm");
				Date dateobj = new Date();
				String currentDate = df.format(dateobj).toString();
				getCurrentTasks(currentDate);

				if (list_of_dicovered_tasks.isEmpty() == false) {
					for (int i = 0; i < list_of_dicovered_tasks.size(); i++) {
						for (int j = (i + 1); j < list_of_dicovered_tasks.size(); j++) {
							double value1 = list_of_dicovered_tasks.get(i).get_task_priority();
							double value2 = list_of_dicovered_tasks.get(j).get_task_priority();

							tasks task1 = list_of_dicovered_tasks.get(i);
							tasks task2 = list_of_dicovered_tasks.get(j);

							String ID1 = list_of_dicovered_tasks_ID.get(i);
							String ID2 = list_of_dicovered_tasks_ID.get(j);

							if (value1 < value2) {
								list_of_dicovered_tasks.set(i, task2);
								list_of_dicovered_tasks.set(j, task1);

								list_of_dicovered_tasks_ID.set(i, ID2);
								list_of_dicovered_tasks_ID.set(j, ID1);
							}
						}
					}

					LinkedList<String> list_of_task_ID = new LinkedList<>();
					LinkedList<LinkedList<Double>> list_of_task_sensors = new LinkedList<>();
					LinkedList<LinkedList<Double>> list_of_task_rewards = new LinkedList<>();
					LinkedList<LinkedList<Double>> list_of_task_working_rates = new LinkedList<>();
					LinkedList<Double> list_of_task_X = new LinkedList<>();
					LinkedList<Double> list_of_task_Y = new LinkedList<>();
					LinkedList<Double> list_of_task_Z = new LinkedList<>();
					LinkedList<Double> list_of_task_priorities = new LinkedList<>();
					LinkedList<Double> list_of_task_durations = new LinkedList<>();
					LinkedList<String> list_of_task_starting_dates = new LinkedList<>();

					for (int i = 0; i < list_of_dicovered_tasks.size(); i++) {
						list_of_task_ID.addLast(list_of_dicovered_tasks.get(i).get_task_ID());
						list_of_task_sensors.addLast(list_of_dicovered_tasks.get(i).get_task_sensors());
						list_of_task_rewards.addLast(list_of_dicovered_tasks.get(i).get_task_rewards());
						list_of_task_working_rates.addLast(list_of_dicovered_tasks.get(i).get_task_working_rates());
						list_of_task_X.addLast(list_of_dicovered_tasks.get(i).get_task_X());
						list_of_task_Y.addLast(list_of_dicovered_tasks.get(i).get_task_Y());
						list_of_task_Z.addLast(list_of_dicovered_tasks.get(i).get_task_Z());
						list_of_task_priorities.addLast(list_of_dicovered_tasks.get(i).get_task_priority());
						list_of_task_durations.addLast(list_of_dicovered_tasks.get(i).get_task_duration());
						list_of_task_starting_dates.addLast(list_of_dicovered_tasks.get(i).get_task_starting_date());
					}

					matrix_of_bids = new bids[list_of_dicovered_tasks.size()][list_of_all_bidders_ID.size()];
					LinkedList<Double> liste1 = new LinkedList<>();
					for (int i = 0; i < matrix_of_bids.length; i++) {
						for (int j = 0; j < matrix_of_bids[0].length; j++) {
							bids bid = new bids();

							bid.set_bidder_ID("");
							bid.set_task_ID("");
							bid.set_bidder_interested_in_task(false);
							bid.set_commun_sensors(liste1);
							bid.set_commun_costs(liste1);
							bid.set_commun_rewards(liste1);
							bid.set_estimated_consumed_energy_by_sensors(liste1);
							bid.set_estimated_traveled_distance(0.0);
							bid.set_estimated_traveling_time(0.0);
							bid.set_estimated_consumed_energy_by_displacement(0.0);
							bid.set_estimated_gain(0.0);

							matrix_of_bids[i][j] = bid;
						}
					}

					for (int i = 0; i < list_of_all_bidders_ID.size(); i++) {
						try {
							message_content1 = new Object[11];
							message_content1[0] = "REQUEST-CHOOSE-TASKS";
							message_content1[1] = list_of_task_ID;
							message_content1[2] = list_of_task_sensors;
							message_content1[3] = list_of_task_rewards;
							message_content1[4] = list_of_task_working_rates;
							message_content1[5] = list_of_task_X;
							message_content1[6] = list_of_task_Y;
							message_content1[7] = list_of_task_Z;
							message_content1[8] = list_of_task_priorities;
							message_content1[9] = list_of_task_durations;
							message_content1[10] = list_of_task_starting_dates;

							message1 = new ACLMessage(ACLMessage.INFORM);
							message1.setContentObject(message_content1);
							message1.addReceiver(new AID(list_of_all_bidders_ID.get(i), AID.ISLOCALNAME));
							send(message1);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		parallelbehaviour.addSubBehaviour(new TickerBehaviour(this, 100) {
			@Override
			protected void onTick() {
				if (logic1 == true) {
					logic1 = false;
					
					for (int i = 0; i < matrix_of_bids.length; i++) {
						bids[] vector_of_bids = matrix_of_bids[i];

						LinkedList<bids> list_of_candidate_bidders = new LinkedList<>();
						for (int j = 0; j < vector_of_bids.length; j++) {
							if (vector_of_bids[j].get_bidder_interested_in_task() == true) {
								list_of_candidate_bidders.addLast(vector_of_bids[j]);
							}
						}

						if (list_of_candidate_bidders.isEmpty() == false) {
							if (getTheTaskCanBeAllocated(list_of_dicovered_tasks.get(i).get_task_sensors(), list_of_candidate_bidders) == true) {
								LinkedList<String> criteria = open_the_file("config", "criteria.txt");

								LinkedList<String> allocation = null;
								LinkedList<String> coalition = null;

								double fitness_value = 0.0;
								double execution_time = 0.0;

								if (criteria.get(0).equals("Exact solution")) {
									ES es = new ES(filePath, list_of_dicovered_tasks.get(i).get_task_sensors(), list_of_candidate_bidders, list_of_all_allocations);

									allocation = es.getTheOptimalAllocation();
									coalition = es.getTheOptimalCoalition();

									fitness_value = es.getTheOptimalFitnessValue();
									execution_time = es.getTheEstimatedExecutionTime();
								} else if (criteria.get(0).equals("Approximate solution (GA)")) {
									GA ga = new GA(list_of_dicovered_tasks.get(i).get_task_sensors(), list_of_candidate_bidders);

									allocation = ga.getTheBestAllocation();
									coalition = ga.getTheBestCoalition();

									fitness_value = ga.getTheBestFitnessValue();
									execution_time = ga.getTheEstimatedExecutionTime();
								}

								LinkedList<bids> list_of_kept_bidders = new LinkedList<>();
								for (int j = 0; j < list_of_candidate_bidders.size(); j++) {
									if (coalition.contains(list_of_candidate_bidders.get(j).get_bidder_ID())) {
										list_of_kept_bidders.addLast(list_of_candidate_bidders.get(j));
									}
								}

								double max_estimated_traveling_time = Double.MIN_EXPONENT;
								for (int j = 0; j < list_of_kept_bidders.size(); j++) {
									double estimated_traveling_time = list_of_kept_bidders.get(j).get_estimated_traveling_time();

									if (estimated_traveling_time > max_estimated_traveling_time) {
										max_estimated_traveling_time = estimated_traveling_time;
									}
								}

								String real_starting_date = addMinutes(list_of_dicovered_tasks.get(i).get_task_discovering_date(), (int) Math.ceil(max_estimated_traveling_time));
								String real_finishing_date = addMinutes(real_starting_date, (int) list_of_dicovered_tasks.get(i).get_task_duration());

								LinkedList<Double> consumed_energies = new LinkedList<>();
								for (int j = 0; j < list_of_kept_bidders.size(); j++) {
									double consumed_energy = list_of_kept_bidders.get(j).get_estimated_consumed_energy_by_displacement();

									LinkedList<Integer> indices = new LinkedList<>();
									for (int k = 0; k < allocation.size(); k++) {
										if (allocation.get(k).equals(list_of_kept_bidders.get(j).get_bidder_ID())) {
											indices.addLast(k);
										}
									}

									LinkedList<Double> commun_energies = list_of_kept_bidders.get(j).get_estimated_consumed_energy_by_sensors();
									for (int k = 0; k < indices.size(); k++) {
										consumed_energy = consumed_energy + commun_energies.get(indices.get(k));
									}

									consumed_energies.addLast(consumed_energy);
								}

								for (int p = (i + 1); p < matrix_of_bids.length; p++) {
									for (int q = 0; q < matrix_of_bids[p].length; q++) {
										String bidder_ID = matrix_of_bids[p][q].get_bidder_ID();

										if (coalition.contains(bidder_ID)) {
											matrix_of_bids[p][q].set_bidder_interested_in_task(false);
										}
									}
								}

								for (int j = 0; j < coalition.size(); j++) {
									try {
										message_content1 = new Object[10];
										message_content1[0] = "REQUEST-REACH-TASK";
										message_content1[1] = list_of_dicovered_tasks.get(i).get_task_ID();
										message_content1[2] = list_of_dicovered_tasks.get(i).get_task_X();
										message_content1[3] = list_of_dicovered_tasks.get(i).get_task_Y();
										message_content1[4] = list_of_dicovered_tasks.get(i).get_task_Z();
										message_content1[5] = list_of_dicovered_tasks.get(i).get_task_priority();
										message_content1[6] = list_of_dicovered_tasks.get(i).get_task_duration();
										message_content1[7] = real_starting_date;
										message_content1[8] = real_finishing_date;
										message_content1[9] = consumed_energies.get(j);

										message1 = new ACLMessage(ACLMessage.INFORM);
										message1.setContentObject(message_content1);
										message1.addReceiver(new AID(coalition.get(j), AID.ISLOCALNAME));
										send(message1);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

								NumberFormat formatter = new DecimalFormat("#0.00000");
								
								System.out.println("The task " + list_of_dicovered_tasks.get(i).get_task_ID() + " can be allocated : { the allocation is : " + allocation + " = [" + String.valueOf(formatter.format(fitness_value)) + "]" + ", the coalition is : " + coalition + ", and the estimated computing time is : "
										+ String.valueOf(formatter.format(execution_time)) + " ms.}");
								number_of_done_tasks = number_of_done_tasks + 1.0;
								total_execution_time = total_execution_time + execution_time;
								total_fitness = total_fitness + fitness_value;
								
								double rate1 = number_of_done_tasks;
								double rate2 = number_of_undone_tasks;
								System.out.println("The rate of allocated tasks is : " + String.valueOf(formatter.format(rate1)));
								System.out.println("The rate of unallocated tasks is : " + String.valueOf(formatter.format(rate2)));
								System.out.println("The total fitness value is : " + String.valueOf(formatter.format(total_fitness)));
								System.out.println("The total execution time is : " + String.valueOf(formatter.format(total_execution_time)));
							} else {
								NumberFormat formatter = new DecimalFormat("#0.00000");
								
								System.out.println("The task " + list_of_dicovered_tasks.get(i).get_task_ID() + " cannot be allocated (there are not enough sensors).");
								number_of_undone_tasks = number_of_undone_tasks + 1.0;
								total_execution_time = total_execution_time + 0.0;
								total_fitness = total_fitness + 0.0;
								
								double rate1 = number_of_done_tasks;
								double rate2 = number_of_undone_tasks;
								System.out.println("The rate of allocated tasks is : " + String.valueOf(formatter.format(rate1)));
								System.out.println("The rate of unallocated tasks is : " + String.valueOf(formatter.format(rate2)));
								System.out.println("The total fitness value is : " + String.valueOf(formatter.format(total_fitness)));
								System.out.println("The total execution time is : " + String.valueOf(formatter.format(total_execution_time)));
							}
						} else {
							NumberFormat formatter = new DecimalFormat("#0.00000");
							
							System.out.println("The task " + list_of_dicovered_tasks.get(i).get_task_ID() + " cannot be allocated (there is no candidate bidders).");
							number_of_undone_tasks = number_of_undone_tasks + 1.0;
							total_execution_time = total_execution_time + 0.0;
							total_fitness = total_fitness + 0.0;
							
							double rate1 = number_of_done_tasks;
							double rate2 = number_of_undone_tasks;
							System.out.println("The rate of allocated tasks is : " + String.valueOf(formatter.format(rate1)));
							System.out.println("The rate of unallocated tasks is : " + String.valueOf(formatter.format(rate2)));
							System.out.println("The total fitness value is : " + String.valueOf(formatter.format(total_fitness)));
							System.out.println("The total execution time is : " + String.valueOf(formatter.format(total_execution_time)));
						}
					}
				}
			}
		});

		parallelbehaviour.addSubBehaviour(new CyclicBehaviour() {
			@SuppressWarnings("unchecked")
			@Override
			public void action() {
				message1 = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));

				if (message1 != null) {
					try {
						message_content1 = (Object[]) message1.getContentObject();

						if (message_content1[0].equals("ANSWER-CHOOSE-TASKS")) {
							int line = list_of_dicovered_tasks_ID.indexOf(String.valueOf(message_content1[1]));
							int column = list_of_all_bidders_ID.indexOf(String.valueOf(message_content1[2]));

							bids bid = new bids();

							bid.set_bidder_ID((String) message_content1[2]);
							bid.set_task_ID((String) message_content1[1]);
							bid.set_bidder_interested_in_task((Boolean) message_content1[3]);
							bid.set_commun_sensors((LinkedList<Double>) message_content1[4]);
							bid.set_commun_costs((LinkedList<Double>) message_content1[5]);
							bid.set_commun_rewards((LinkedList<Double>) message_content1[6]);
							bid.set_estimated_consumed_energy_by_sensors((LinkedList<Double>) message_content1[7]);
							bid.set_estimated_traveled_distance((double) message_content1[8]);
							bid.set_estimated_traveling_time((double) message_content1[9]);
							bid.set_estimated_consumed_energy_by_displacement((double) message_content1[10]);
							bid.set_estimated_gain((double) message_content1[11]);

							matrix_of_bids[line][column] = bid;

							counter1 = counter1 + 1;
							if (counter1 == (nbr_bidders * list_of_dicovered_tasks.size())) {
								counter1 = 0;
								logic1 = true;
							}
						}
					} catch (UnreadableException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				message1 = null;
			}
		});

		addBehaviour(parallelbehaviour);
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

	public LinkedList<String> spliter1(String string1, String separators) {
		LinkedList<String> liste1 = new LinkedList<>();

		StringTokenizer jeton1 = new StringTokenizer(string1, separators);
		while (jeton1.hasMoreTokens()) {
			liste1.addLast(jeton1.nextToken());
		}

		return liste1;
	}

	public LinkedList<Double> spliter2(String string1, String separators) {
		LinkedList<String> liste1 = new LinkedList<>();

		StringTokenizer jeton1 = new StringTokenizer(string1, separators);
		while (jeton1.hasMoreTokens()) {
			liste1.addLast(jeton1.nextToken());
		}

		LinkedList<Double> liste2 = new LinkedList<>();
		for (int i = 0; i < liste1.size(); i++) {
			liste2.addLast(Double.valueOf(liste1.get(i)));
		}

		return liste2;
	}

	public void getCurrentTasks(String currentDate) {
		list_of_dicovered_tasks = new LinkedList<>();
		list_of_dicovered_tasks_ID = new LinkedList<>();

		for (int i = 0; i < list_of_all_tasks.size(); i++) {
			if ((list_of_all_tasks.get(i).get_task_discovering_date().equals(currentDate)) && (list_of_all_tasks.get(i).get_task_state().equals("UNDISCOVERED"))) {
				list_of_all_tasks.get(i).set_task_state("DISCOVERED");

				list_of_dicovered_tasks_ID.addLast(list_of_all_tasks.get(i).get_task_ID());
				list_of_dicovered_tasks.addLast(list_of_all_tasks.get(i));
			}
		}
	}

	public boolean getTheTaskCanBeAllocated(LinkedList<Double> task_sensors, LinkedList<bids> list_of_candidate_bidders) {
		boolean theTaskCanBeAllocated = true;

		LinkedList<Double> bidder_sensors = new LinkedList<>();
		for (int i = 0; i < task_sensors.size(); i++) {
			bidder_sensors.addLast(0.0);
		}

		for (int i = 0; i < list_of_candidate_bidders.size(); i++) {
			LinkedList<Double> commun_sensors = list_of_candidate_bidders.get(i).get_commun_sensors();

			for (int j = 0; j < commun_sensors.size(); j++) {
				bidder_sensors.set(j, bidder_sensors.get(j) + commun_sensors.get(j));
			}
		}

		int i = 0;
		while ((i < task_sensors.size()) && (theTaskCanBeAllocated == true)) {
			if ((task_sensors.get(i) == 1.0) && (bidder_sensors.get(i) == 0.0)) {
				theTaskCanBeAllocated = false;
			} else {
				i = i + 1;
			}
		}

		return theTaskCanBeAllocated;
	}

	public String addMinutes(String string1, int minutes) {
		String string2 = "";

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date d = df.parse(string1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, minutes);
			string2 = df.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return string2;
	}
}