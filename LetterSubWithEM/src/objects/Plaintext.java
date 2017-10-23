package objects;

import java.util.Comparator;
import java.util.Map;

public class Plaintext implements Comparable<Plaintext> {

	private String plaintext;
	private double subTableProabability;
	private double lmProbability;

	public Plaintext(String plaintext, Map<String, Map<String, Double>> lm) {
		this.plaintext = plaintext;
		// TODO calculate lm probability
		lmProbability = 1.;
		String last = "start";
		String current = "start";
		for (int i = 0; i < plaintext.length(); i++) {
			last = current;
			current = plaintext.charAt(i) + "";
			lmProbability *= lm.get(current).get(last);
		}
		lmProbability *= lm.get("end").get(current);
	}

	public double getLMProbability() {
		return lmProbability;
	}

	public double updateSubTableProbability(SubstitutionTable st) {
		subTableProabability = 1.;
		for (int i = 0; i < plaintext.length(); i++) {
			subTableProabability *= st.getProbability(plaintext.charAt(i) + "",
					i);
		}
		return subTableProabability;
	}

	public double getSubTableProbability() {
		return this.subTableProabability;
	}

	public String getPlaintext() {
		return this.plaintext;
	}

	@Override
	public int compareTo(Plaintext o) {
		if (this.subTableProabability * this.lmProbability > o
				.getLMProbability() * o.getSubTableProbability()) {
			return -1;
		} else if (this.subTableProabability * this.lmProbability < o
				.getLMProbability() * o.getSubTableProbability()) {
			return 1;
		}
		return 0;
	}

}
