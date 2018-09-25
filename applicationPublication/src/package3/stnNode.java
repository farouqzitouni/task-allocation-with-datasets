package package3;

public class stnNode {

	String task_ID = "";
	String task_real_starting_date = "";
	String task_real_finishing_date = "";

	double task_priority = 0;
	double task_X = 0;
	double task_Y = 0;
	double task_Z = 0;
	double task_duration = 0;

	public stnNode() {
	}

	public void set_task_ID(String task_ID) {
		this.task_ID = task_ID;
	}

	public void set_task_real_starting_date(String task_real_starting_date) {
		this.task_real_starting_date = task_real_starting_date;
	}

	public void set_task_real_finishing_date(String task_real_finishing_date) {
		this.task_real_finishing_date = task_real_finishing_date;
	}

	public void set_task_priority(double task_priority) {
		this.task_priority = task_priority;
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

	public void set_task_duration(double task_duration) {
		this.task_duration = task_duration;
	}

	public String get_task_ID() {
		return this.task_ID;
	}

	public String get_task_real_starting_date() {
		return this.task_real_starting_date;
	}

	public String get_task_real_finishing_date() {
		return this.task_real_finishing_date;
	}

	public double get_task_priority() {
		return this.task_priority;
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

	public double get_task_duration() {
		return this.task_duration;
	}
}