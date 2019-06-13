package models;

import java.util.List;

/**
 * Let the system evolve until the number of particles at both sides of the box is stabilized (fraction of particles).
 */
public class StabilizedBoxCriteria {

	private final double fraction;
	private final double halfBoxWidth;

	public StabilizedBoxCriteria(double fraction, double halfBoxWidth) {
		this.fraction = fraction;
		this.halfBoxWidth = halfBoxWidth;
	}

	public boolean isDone(List<Particle> particles) {
		double size = particles.size();
		double lefts = particles.stream().filter(p -> p.getPosition().getX() < halfBoxWidth).count();
		System.out.println("size: " + particles.size());
		System.out.println("lefts: " + lefts);
		System.out.println("frac: " + lefts / size);
		return fraction > (lefts / size);
	}
}