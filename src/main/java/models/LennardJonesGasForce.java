package models;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Set;

public class LennardJonesGasForce {

	/**
	 * holeDepth
	 */
	private final double e;

	/**
	 * distanceAtMinimum
	 */
	private final double rm;

	public LennardJonesGasForce(final double holeDepth, final double distanceAtMinimum) {
		this.e = holeDepth;
		this.rm = distanceAtMinimum;
	}

	public double F(double distanceBetweenParticles) {
		double fraction = rm / distanceBetweenParticles;
		return ((12 * e) / rm) * (Math.pow(fraction, 13) - Math.pow(fraction, 7));
	}

	public Vector2D sumOfForces(final Particle particle, final Set<Particle> neighbours) {
		double totalForceX = 0.0;
		double totalForceY = 0.0;

		for (Particle neighbour : neighbours) {
			double magnitude = F(neighbour.getDistanceBetween(particle));
			Vector2D distanceVector = particle.getPosition().subtract(neighbour.getPosition());

			// Project normal force in x and y
			totalForceX += magnitude * (distanceVector.getX()) / distanceVector.getNorm();
			totalForceY += magnitude * (distanceVector.getY()) / distanceVector.getNorm();
		}

		return new Vector2D(totalForceX, totalForceY);
	}
}
