package algorithms;

import models.Force;

public class VelocityVerlet implements IntegrationMethod {

	private final double mass;
	private double currentPosition;
	private double currentVelocity;
	private final Force force;

	public VelocityVerlet(double mass,
	                      double initialPosition,
	                      double initialVelocity,
	                      Force force) {
		this.mass = mass;
		this.currentPosition = initialPosition;
		this.currentVelocity = initialVelocity;
		this.force = force;
	}

	public double updatePosition(double dt) {
		double currentAcceleration = force.F(currentPosition, currentVelocity) / mass;

		double halfStepVelocity = currentVelocity + currentAcceleration * (dt / 2.0);
		double nextPosition = X(dt, currentAcceleration);

		double nextAcceleration = force.F(nextPosition, halfStepVelocity) / mass;
		double nextVelocity = halfStepVelocity + nextAcceleration * (dt / 2.0);

		currentPosition = nextPosition;
		currentVelocity = nextVelocity;
		return nextPosition;
	}

	private double X(double dt, double currentAcceleration) {
		return this.currentPosition + this.currentVelocity * dt + 0.5 * currentAcceleration * Math.pow(dt, 2);
	}
}
