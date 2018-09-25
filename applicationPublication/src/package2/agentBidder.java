package package2;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import package3.stnNode;

@SuppressWarnings("serial")
public class agentBidder extends Agent {

	DFAgentDescription agentDescription = null;
	ACLMessage message1 = null;
	ACLMessage message2 = null;

	Object[] parametres1 = null;
	Object[] message_content1 = null;
	Object[] message_content2 = null;

	int nbr_sensors = 0;
	int counter1 = 0;

	String bidder_ID = "";

	LinkedList<Double> bidder_sensors = null;
	LinkedList<Double> bidder_costs = null;
	LinkedList<Double> bidder_working_currents = null;

	double bidder_X = 0;
	double bidder_Y = 0;
	double bidder_Z = 0;
	double bidder_velocity = 0;
	double bidder_energy_gauge = 0;
	double bidder_mass = 0;
	double bidder_altitude = 0;
	double bidder_battery_voltage = 0;
	double bidder_battery_capacity = 0;
	double bidder_peukert_exponent = 0;
	double bidder_c_rate = 0;
	double min_energy_gauge = 0.0;

	LinkedList<stnNode> stnQueue = new LinkedList<>();

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

		parallelbehaviour.addSubBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				nbr_sensors = (int) parametres1[0];

				bidder_ID = (String) parametres1[1];

				bidder_sensors = spliter((String) parametres1[2], "[], ");
				bidder_costs = spliter((String) parametres1[3], "[], ");
				bidder_working_currents = spliter((String) parametres1[4], "[], ");

				bidder_X = spliter((String) parametres1[5], "(), ").get(0);
				bidder_Y = spliter((String) parametres1[5], "(), ").get(1);
				bidder_Z = spliter((String) parametres1[5], "(), ").get(2);

				bidder_velocity = spliter((String) parametres1[6], "[], ").get(0);
				bidder_energy_gauge = spliter((String) parametres1[6], "[], ").get(1);
				bidder_mass = spliter((String) parametres1[6], "[], ").get(2);
				bidder_altitude = spliter((String) parametres1[6], "[], ").get(3);
				bidder_battery_voltage = spliter((String) parametres1[6], "[], ").get(4);
				bidder_battery_capacity = spliter((String) parametres1[6], "[], ").get(5);
				bidder_peukert_exponent = spliter((String) parametres1[6], "[], ").get(6);
				bidder_c_rate = spliter((String) parametres1[6], "[], ").get(7);

				stnNode node = new stnNode();
				node.set_task_ID("Task000");
				node.set_task_real_starting_date("00:00");
				node.set_task_real_finishing_date("00:00");
				node.set_task_priority(Integer.MAX_VALUE);
				node.set_task_X(bidder_X);
				node.set_task_Y(bidder_Y);
				node.set_task_Z(bidder_Z);
				node.set_task_duration(0);
				stnQueue.addLast(node);
			}
		});

		addBehaviour(new CyclicBehaviour(this) {
			@SuppressWarnings({ "unchecked" })
			@Override
			public void action() {
				message1 = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));

				if (message1 != null) {
					try {
						message_content1 = (Object[]) message1.getContentObject();

						if (message_content1[0].equals("REQUEST-CHOOSE-TASKS")) {
							LinkedList<String> list_of_task_ID = (LinkedList<String>) message_content1[1];
							LinkedList<LinkedList<Double>> list_of_task_sensors = (LinkedList<LinkedList<Double>>) message_content1[2];
							LinkedList<LinkedList<Double>> list_of_task_rewards = (LinkedList<LinkedList<Double>>) message_content1[3];
							LinkedList<LinkedList<Double>> list_of_task_working_rates = (LinkedList<LinkedList<Double>>) message_content1[4];
							LinkedList<Double> list_of_task_X = (LinkedList<Double>) message_content1[5];
							LinkedList<Double> list_of_task_Y = (LinkedList<Double>) message_content1[6];
							LinkedList<Double> list_of_task_Z = (LinkedList<Double>) message_content1[7];
							LinkedList<Double> list_of_task_priorities = (LinkedList<Double>) message_content1[8];
							LinkedList<Double> list_of_task_durations = (LinkedList<Double>) message_content1[9];
							LinkedList<String> list_of_task_starting_dates = (LinkedList<String>) message_content1[10];

							for (int i = 0; i < list_of_task_ID.size(); i++) {
								LinkedList<Double> commun_sensors = new LinkedList<>();
								LinkedList<Double> commun_costs = new LinkedList<>();
								LinkedList<Double> commun_rewards = new LinkedList<>();
								LinkedList<Double> estimated_consumed_energy_by_sensors = new LinkedList<>();

								double estimated_distance = 0.0;
								double estimated_traveling_time = 0.0;
								double estimated_consumed_energy_by_displacement = 0.0;
								double estimated_gain = 0.0;

								if (getTheTaskCanBeHandled(list_of_task_priorities.get(i)) == true) {
									commun_sensors = getCommunSensors(bidder_sensors, list_of_task_sensors.get(i));

									if (getTheTaskIsFaisable(commun_sensors) == true) {
										commun_costs = getCommunCosts(commun_sensors, bidder_costs);
										commun_rewards = getCommunRewards(commun_sensors, list_of_task_rewards.get(i));

										estimated_distance = getEstimatedDistance(bidder_X, bidder_Y, bidder_Z, list_of_task_X.get(i), list_of_task_Y.get(i), list_of_task_Z.get(i));
										estimated_traveling_time = (estimated_distance / bidder_velocity);

										if (getTheTaskCanBeReached(estimated_traveling_time, list_of_task_starting_dates.get(i)) == true) {
											estimated_consumed_energy_by_sensors = getEstimatedConsumedEnergyBySensors(bidder_sensors, bidder_working_currents, list_of_task_working_rates.get(i), list_of_task_durations.get(i));
											estimated_consumed_energy_by_displacement = getEstimatedConsumedEnergyByDisplacement(estimated_traveling_time);

											if (getTheTaskCanBeDone(estimated_consumed_energy_by_sensors, estimated_consumed_energy_by_displacement) == true) {
												double sum1 = 0.0;
												for (int j = 0; j < commun_rewards.size(); j++) {
													sum1 = sum1 + commun_rewards.get(j);
												}
												estimated_gain = sum1 * Math.pow(Math.E, -0.5 * Math.pow(estimated_traveling_time, 2.0));

												try {
													message_content2 = new Object[12];
													message_content2[0] = "ANSWER-CHOOSE-TASKS";
													message_content2[1] = list_of_task_ID.get(i);
													message_content2[2] = bidder_ID;
													message_content2[3] = true;
													message_content2[4] = commun_sensors;
													message_content2[5] = commun_costs;
													message_content2[6] = commun_rewards;
													message_content2[7] = estimated_consumed_energy_by_sensors;
													message_content2[8] = estimated_distance;
													message_content2[9] = estimated_traveling_time;
													message_content2[10] = estimated_consumed_energy_by_displacement;
													message_content2[11] = estimated_gain;

													message2 = new ACLMessage(ACLMessage.INFORM);
													message2.setContentObject(message_content2);
													message2.addReceiver(new AID("agentAuctioneer", AID.ISLOCALNAME));
													send(message2);
												} catch (IOException e) {
													e.printStackTrace();
												}
											} else {
												try {
													message_content2 = new Object[12];
													message_content2[0] = "ANSWER-CHOOSE-TASKS";
													message_content2[1] = list_of_task_ID.get(i);
													message_content2[2] = bidder_ID;
													message_content2[3] = false;
													message_content2[4] = commun_sensors;
													message_content2[5] = commun_costs;
													message_content2[6] = commun_rewards;
													message_content2[7] = estimated_consumed_energy_by_sensors;
													message_content2[8] = estimated_distance;
													message_content2[9] = estimated_traveling_time;
													message_content2[10] = estimated_consumed_energy_by_displacement;
													message_content2[11] = estimated_gain;

													message2 = new ACLMessage(ACLMessage.INFORM);
													message2.setContentObject(message_content2);
													message2.addReceiver(new AID("agentAuctioneer", AID.ISLOCALNAME));
													send(message2);
												} catch (IOException e) {
													e.printStackTrace();
												}
											}
										} else {
											try {
												message_content2 = new Object[12];
												message_content2[0] = "ANSWER-CHOOSE-TASKS";
												message_content2[1] = list_of_task_ID.get(i);
												message_content2[2] = bidder_ID;
												message_content2[3] = false;
												message_content2[4] = commun_sensors;
												message_content2[5] = commun_costs;
												message_content2[6] = commun_rewards;
												message_content2[7] = estimated_consumed_energy_by_sensors;
												message_content2[8] = estimated_distance;
												message_content2[9] = estimated_traveling_time;
												message_content2[10] = estimated_consumed_energy_by_displacement;
												message_content2[11] = estimated_gain;

												message2 = new ACLMessage(ACLMessage.INFORM);
												message2.setContentObject(message_content2);
												message2.addReceiver(new AID("agentAuctioneer", AID.ISLOCALNAME));
												send(message2);
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									} else {
										try {
											message_content2 = new Object[12];
											message_content2[0] = "ANSWER-CHOOSE-TASKS";
											message_content2[1] = list_of_task_ID.get(i);
											message_content2[2] = bidder_ID;
											message_content2[3] = false;
											message_content2[4] = commun_sensors;
											message_content2[5] = commun_costs;
											message_content2[6] = commun_rewards;
											message_content2[7] = estimated_consumed_energy_by_sensors;
											message_content2[8] = estimated_distance;
											message_content2[9] = estimated_traveling_time;
											message_content2[10] = estimated_consumed_energy_by_displacement;
											message_content2[11] = estimated_gain;

											message2 = new ACLMessage(ACLMessage.INFORM);
											message2.setContentObject(message_content2);
											message2.addReceiver(new AID("agentAuctioneer", AID.ISLOCALNAME));
											send(message2);
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								} else {
									try {
										message_content2 = new Object[12];
										message_content2[0] = "ANSWER-CHOOSE-TASKS";
										message_content2[1] = list_of_task_ID.get(i);
										message_content2[2] = bidder_ID;
										message_content2[3] = false;
										message_content2[4] = commun_sensors;
										message_content2[5] = commun_costs;
										message_content2[6] = commun_rewards;
										message_content2[7] = estimated_consumed_energy_by_sensors;
										message_content2[8] = estimated_distance;
										message_content2[9] = estimated_traveling_time;
										message_content2[10] = estimated_consumed_energy_by_displacement;
										message_content2[11] = estimated_gain;

										message2 = new ACLMessage(ACLMessage.INFORM);
										message2.setContentObject(message_content2);
										message2.addReceiver(new AID("agentAuctioneer", AID.ISLOCALNAME));
										send(message2);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						} else if (message_content1[0].equals("REQUEST-REACH-TASK")) {
							bidder_X = (double) message_content1[2];
							bidder_Y = (double) message_content1[3];
							bidder_Z = (double) message_content1[4];

							stnNode node = new stnNode();

							node.set_task_ID((String) message_content1[1]);
							node.set_task_real_starting_date((String) message_content1[7]);
							node.set_task_real_finishing_date((String) message_content1[8]);
							node.set_task_priority((double) message_content1[5]);
							node.set_task_X(bidder_X);
							node.set_task_Y(bidder_Y);
							node.set_task_Y(bidder_Z);
							node.set_task_duration((double) message_content1[6]);

							stnQueue.addLast(node);

							bidder_energy_gauge = bidder_energy_gauge - (double) message_content1[9];
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

	public LinkedList<Double> spliter(String string1, String separators) {
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

	public boolean getTheTaskCanBeHandled(double priority) {
		boolean theTaskCanBeHandled = false;

		if (stnQueue.getLast().get_task_priority() >= priority) {
			theTaskCanBeHandled = true;
		} else {
			theTaskCanBeHandled = false;
		}

		return theTaskCanBeHandled;
	}

	public LinkedList<Double> getCommunSensors(LinkedList<Double> bidder_sensors, LinkedList<Double> task_sensors) {
		LinkedList<Double> commun_sensors = new LinkedList<>();

		for (int i = 0; i < bidder_sensors.size(); i++) {
			if ((bidder_sensors.get(i) == 1.0) && (task_sensors.get(i) == 1.0)) {
				commun_sensors.addLast(1.0);
			} else {
				commun_sensors.addLast(0.0);
			}
		}

		return commun_sensors;
	}

	public boolean getTheTaskIsFaisable(LinkedList<Double> commun_sensors) {
		boolean theTaskIsAllocable = false;

		int i = 0;
		while ((i < commun_sensors.size()) && (theTaskIsAllocable == false)) {
			if (commun_sensors.get(i) == 1.0) {
				theTaskIsAllocable = true;
			} else {
				i = i + 1;
			}
		}

		return theTaskIsAllocable;
	}

	public LinkedList<Double> getCommunCosts(LinkedList<Double> commun_sensors, LinkedList<Double> robot_costs) {
		LinkedList<Double> commun_costs = new LinkedList<>();

		for (int i = 0; i < commun_sensors.size(); i++) {
			commun_costs.addLast(commun_sensors.get(i) * robot_costs.get(i));
		}

		return commun_costs;
	}

	public LinkedList<Double> getCommunRewards(LinkedList<Double> commun_sensors, LinkedList<Double> task_rewards) {
		LinkedList<Double> commun_rewards = new LinkedList<>();

		for (int i = 0; i < commun_sensors.size(); i++) {
			commun_rewards.addLast(commun_sensors.get(i) * task_rewards.get(i));
		}

		return commun_rewards;
	}

	public double getEstimatedDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) + Math.pow(z1 - z2, 2.0));
	}

	public boolean getTheTaskCanBeReached(double travel_time, String task_starting_date) throws ParseException {
		boolean theTaskCanBeReached = true;

		DateFormat df = new SimpleDateFormat("HH:mm");
		Date dateobj = new Date();

		String currentDate = df.format(dateobj);
		String liberationDate = stnQueue.getLast().get_task_real_finishing_date();
		Date date1 = df.parse(currentDate);
		Date date2 = df.parse(liberationDate);

		String estimated_arrival_date = "";
		if (date1.compareTo(date2) >= 0) {
			estimated_arrival_date = addMinutes(currentDate, (int) Math.ceil(travel_time));
		} else {
			estimated_arrival_date = addMinutes(liberationDate, (int) Math.ceil(travel_time));
		}

		Date date3 = df.parse(estimated_arrival_date);
		Date date4 = df.parse(task_starting_date);

		if (date3.compareTo(date4) <= 0) {
			theTaskCanBeReached = true;
		} else {
			theTaskCanBeReached = false;
		}

		return theTaskCanBeReached;
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

	public LinkedList<Double> getEstimatedConsumedEnergyBySensors(LinkedList<Double> commun_sensors, LinkedList<Double> bidder_working_currents, LinkedList<Double> task_working_rates, double task_duration) {
		LinkedList<Double> estimated_consumed_energy_by_sensors = new LinkedList<>();

		for (int i = 0; i < commun_sensors.size(); i++) {
			if (commun_sensors.get(i) == 1.0) {
				double Peukert_capacity = bidder_c_rate * Math.pow((bidder_battery_capacity / bidder_c_rate), bidder_peukert_exponent);
				double activation_period = ((task_duration / 60.0) * task_working_rates.get(i)) / 100.0;
				double consumed_percent = (activation_period / (Peukert_capacity / Math.pow((bidder_working_currents.get(i) / 1000.0), bidder_peukert_exponent))) * 100;

				estimated_consumed_energy_by_sensors.addLast(consumed_percent);
			} else {
				estimated_consumed_energy_by_sensors.addLast(0.0);
			}
		}

		return estimated_consumed_energy_by_sensors;
	}

	public double getEstimatedConsumedEnergyByDisplacement(double traveling_time) {
		double estimated_consumed_energy_by_displacement = 0.0;

		double kinetic_energy = 0.5 * (bidder_mass / 1000.0) * Math.pow(bidder_velocity / 60.0, 2.0);
		double potential_energy = (bidder_mass / 1000.0) * 9.81 * bidder_altitude;
		double energy = kinetic_energy + potential_energy;
		double intensity = energy / bidder_battery_voltage;
		double Peukert_capacity = bidder_c_rate * Math.pow((bidder_battery_capacity / bidder_c_rate), bidder_peukert_exponent);

		estimated_consumed_energy_by_displacement = ((traveling_time / 60.0) / (Peukert_capacity / Math.pow(intensity, bidder_peukert_exponent))) * 100;

		return estimated_consumed_energy_by_displacement;
	}

	public boolean getTheTaskCanBeDone(LinkedList<Double> estimated_consumed_energy_by_sensors, double estimated_consumed_energy_by_displacement) {
		boolean theTaskCanBeDone = false;

		double sum1 = estimated_consumed_energy_by_displacement;
		for (int i = 0; i < estimated_consumed_energy_by_sensors.size(); i++) {
			sum1 = sum1 + estimated_consumed_energy_by_sensors.get(i);
		}

		if ((bidder_energy_gauge - sum1) >= min_energy_gauge) {
			theTaskCanBeDone = true;
		} else {
			theTaskCanBeDone = false;
		}

		return theTaskCanBeDone;
	}
}