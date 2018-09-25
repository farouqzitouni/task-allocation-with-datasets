package package1;

import java.util.LinkedList;

public class displayMetrics {

	double sigma = 0.0;

	public displayMetrics(int nbr_sensors, LinkedList<LinkedList<String>> liste1) {
		LinkedList<Double> liste2 = new LinkedList<>();
		for (int i = 0; i < nbr_sensors; i++) {
			liste2.addLast(0.0);
		}

		for (int i = 0; i < liste1.size(); i++) {
			LinkedList<String> liste3 = liste1.get(i);
			LinkedList<Double> liste4 = new LinkedList<>();
			
			for (int j = 0; j < liste3.size(); j++) {
				liste4.addLast(Double.valueOf(liste3.get(j)));
			}

			for (int j = 0; j < liste4.size(); j++) {
				liste2.set(j, liste2.get(j) + liste4.get(j));
			}
		}

		sigma = std_dev2(liste2);
	}

	public double std_dev2(LinkedList<Double> liste1) {
		double sum = 0;
		for (int i = 0; i < liste1.size(); ++i) {
			sum = sum + liste1.get(i);
		}
		double mean = sum / (double) liste1.size();

		double sq_sum = 0;
		for (int i = 0; i < liste1.size(); ++i) {
			sq_sum = sq_sum + ((liste1.get(i) - mean) * (liste1.get(i) - mean));
		}
		double variance = sq_sum / (double) liste1.size();

		return Math.sqrt(variance);
	}

	public double returnSigma() {
		return sigma;
	}
}