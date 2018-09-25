package package3;

import java.util.LinkedList;

public class tasks {

	String task_ID = "";
	String task_discovering_date = "";
	String task_starting_date = "";
	String task_finishing_date = "";
	String task_state = "";

	LinkedList<Double> task_sensors = null;
	LinkedList<Double> task_rewards = null;
	LinkedList<Double> task_working_rates = null;

	double task_X = 0;
	double task_Y = 0;
	double task_Z = 0;
	double task_priority = 0;
	double task_duration = 0;

	public tasks() {
	}

	public void set_task_ID(String task_ID) {
		this.task_ID = task_ID;
	}

	public void set_task_sensors(LinkedList<Double> task_sensors) {
		this.task_sensors = new LinkedList<>();
		this.task_sensors.addAll(task_sensors);
	}

	public void set_task_rewards(LinkedList<Double> task_rewards) {
		this.task_rewards = new LinkedList<>();
		this.task_rewards.addAll(task_rewards);
	}

	public void set_task_working_rates(LinkedList<Double> task_working_rates) {
		this.task_working_rates = new LinkedList<>();
		this.task_working_rates.addAll(task_working_rates);
	}

	public void set_task_X(double task_X) {
		this.task_X = task_X;
	}

	public void set_task_Y(double task_Y) {
		this.task_Y = task_Y;
	}

	public void set_task_Z(double task_Z) {
		this.task_Z = task_Z;
	}

	public void set_task_priority(double task_priority) {
		this.task_priority = task_priority;
	}

	public void set_task_duration(double task_duration) {
		this.task_duration = task_duration;
	}

	public void set_task_discovering_date(String task_discovering_date) {
		this.task_discovering_date = task_discovering_date;
	}

	public void set_task_starting_date(String task_starting_date) {
		this.task_starting_date = task_starting_date;
	}

	public void set_task_finishing_date(String task_finishing_date) {
		this.task_finishing_date = task_finishing_date;
	}

	public void set_task_state(String task_state) {
		this.task_state = task_state;
	}

	public String get_task_ID() {
		return this.task_ID;
	}

	public LinkedList<Double> get_task_sensors() {
		return this.task_sensors;
	}

	public LinkedList<Double> get_task_rewards() {
		return this.task_rewards;
	}

	public LinkedList<Double> get_task_working_rates() {
		return this.task_working_rates;
	}

	public double get_task_X() {
		return this.task_X;
	}

	public double get_task_Y() {
		return this.task_Y;
	}

	public double get_task_Z() {
		return this.task_Z;
	}

	public double get_task_priority() {
		return this.task_priority;
	}

	public double get_task_duration() {
		return this.task_duration;
	}

	public String get_task_discovering_date() {
		return this.task_discovering_date;
	}

	public String get_task_starting_date() {
		return this.task_starting_date;
	}

	public String get_task_finishing_date() {
		return this.task_finishing_date;
	}

	public String get_task_state() {
		return this.task_state;
	}

	public void display_my_information() {
		System.out.println(task_ID + " " + task_sensors + " " + task_rewards + " " + task_working_rates + " " + task_X + " " + task_Y + " " + task_Z + " " + task_priority + " " + task_duration + " " + task_discovering_date + " " + task_starting_date + " " + task_finishing_date + " " + task_state);
	}
}