package algorithms.neighbours;

import models.Particle;
import models.neighbours.SumOfForces;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Set;

public class VerletWithNeighbours implements IntegrationMethodWithNeighbours {

	private Vector2D currentPosition;
	private SumOfForces force;

	public VerletWithNeighbours(Vector2D initialPosition,
	                            SumOfForces force) {
		this.currentPosition = initialPosition;
		this.force = force;
	}

	public Particle updatePosition(Particle particle, Set<Particle> neighbours, double dt) {

		final Vector2D currentForce = force.sumOfForces(particle, neighbours);

		final Vector2D predictedPosition = particle.getPosition()
				.scalarMultiply(2)
				.subtract(currentPosition)
				.add(currentForce.scalarMultiply(dt * dt / particle.getMass()));

		final Vector2D predictedVelocity = predictedPosition
				.subtract(currentPosition)
				.scalarMultiply(1.0 / (2.0 * dt));

		currentPosition = particle.getPosition();

		particle.setPosition(predictedPosition);
		particle.setVelocity(predictedVelocity);
		return particle;
	}
}
