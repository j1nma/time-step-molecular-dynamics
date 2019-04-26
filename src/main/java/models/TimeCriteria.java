package models;

import java.util.List;

public class TimeCriteria implements Criteria {

	private final double limit;

	public TimeCriteria(double limit) {
		this.limit = limit;
	}

	@Override
	public boolean test(List<Particle> particles, double time) {
		return time > limit;
	}
}