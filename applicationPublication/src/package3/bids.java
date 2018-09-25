package package3;

import java.util.LinkedList;

public class bids {

	boolean bidder_interested_in_task = false;

	String bidder_ID = "";
	String task_ID = "";

	LinkedList<Double> commun_sensors = null;
	LinkedList<Double> commun_costs = null;
	LinkedList<Double> commun_rewards = null;
	LinkedList<Double> estimated_consumed_energy_by_sensors = null;

	double estimated_traveled_distance = 0.0;
	double estimated_traveling_time = 0.0;
	double estimated_consumed_energy_by_displacement = 0.0;
	double estimated_gain = 0.0;

	public bids() {
	}

	public void set_bidder_ID(String bidder_ID) {
		this.bidder_ID = bidder_ID;
	}

	public void set_task_ID(String task_ID) {
		this.task_ID = task_ID;
	}

	public void set_bidder_interested_in_task(boolean bidder_interested_in_task) {
		this.bidder_interested_in_task = bidder_interested_in_task;
	}

	public void set_commun_sensors(LinkedList<Double> commun_sensors) {
		this.commun_sensors = new LinkedList<>();
		this.commun_sensors.addAll(commun_sensors);
	}

	public void set_commun_costs(LinkedList<Double> commun_costs) {
		this.commun_costs = new LinkedList<>();
		this.commun_costs.addAll(commun_costs);
	}

	public void set_commun_rewards(LinkedList<Double> commun_rewards) {
		this.commun_rewards = new LinkedList<>();
		this.commun_rewards.addAll(commun_rewards);
	}

	public void set_estimated_consumed_energy_by_sensors(LinkedList<Double> estimated_consumed_energy_by_sensors) {
		this.estimated_consumed_energy_by_sensors = new LinkedList<>();
		this.estimated_consumed_energy_by_sensors.addAll(estimated_consumed_energy_by_sensors);
	}

	public void set_estimated_traveled_distance(double estimated_traveled_distance) {
		this.estimated_traveled_distance = estimated_traveled_distance;
	}

	public void set_estimated_traveling_time(double estimated_traveling_time) {
		this.estimated_traveling_time = estimated_traveling_time;
	}

	public void set_estimated_consumed_energy_by_displacement(double estimated_consumed_energy_by_displacement) {
		this.estimated_consumed_energy_by_displacement = estimated_consumed_energy_by_displacement;
	}

	public void set_estimated_gain(double estimated_gain) {
		this.estimated_gain = estimated_gain;
	}

	public String get_bidder_ID() {
		return this.bidder_ID;
	}

	public String get_task_ID() {
		return this.task_ID;
	}

	public boolean get_bidder_interested_in_task() {
		return this.bidder_interested_in_task;
	}

	public LinkedList<Double> get_commun_sensors() {
		return this.commun_sensors;
	}

	public LinkedList<Double> get_commun_costs() {
		return this.commun_costs;
	}

	public LinkedList<Double> get_commun_rewards() {
		return this.commun_rewards;
	}

	public LinkedList<Double> get_estimated_consumed_energy_by_sensors() {
		return this.estimated_consumed_energy_by_sensors;
	}

	public double get_estimated_traveled_distance() {
		return this.estimated_traveled_distance;
	}

	public double get_estimated_traveling_time() {
		return this.estimated_traveling_time;
	}

	public double get_estimated_consumed_energy_by_displacement() {
		return this.estimated_consumed_energy_by_displacement;
	}

	public double get_estimated_gain() {
		return this.estimated_gain;
	}

	public void display_my_information() {
		System.out.println(
				bidder_ID + " " + task_ID + " " + commun_sensors + " " + commun_costs + " " + commun_rewards + " " + estimated_consumed_energy_by_sensors + " " + estimated_traveled_distance + " " + estimated_traveling_time + " " + estimated_consumed_energy_by_displacement + " " + estimated_gain);
	}
}