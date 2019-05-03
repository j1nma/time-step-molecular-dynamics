package algorithms;

import models.Force;

public class Beeman implements IntegrationMethod {

	private float mass;
	private float currentPosition;
	private float currentVelocity;
	private float previousAcceleration;
	private Force force;

	public Beeman(float mass,
	              float initialPosition,
	              float initialVelocity,
	              float previousAcceleration,
	              Force force) {
		this.mass = mass;
		this.currentPosition = initialPosition;
		this.currentVelocity = initialVelocity;
		this.previousAcceleration = previousAcceleration;
		this.force = force;
	}

	public float updatePosition(float dt) {
		float currentAcceleration;
		float nextPosition;
		float nextVelocity;
		float nextAcceleration;

		// Calculate current acceleration
		currentAcceleration = force.F(currentPosition, currentVelocity) / mass;

		// Calculate next position
		nextPosition = X(dt, currentAcceleration);

		// Calculate next acceleration
		nextAcceleration = force.F(nextPosition, currentVelocity) / mass;

		// Calculate next velocity
		nextVelocity = Vcorrector(dt, nextAcceleration, currentAcceleration);

		this.currentPosition = nextPosition;
		this.currentVelocity = nextVelocity;
		this.previousAcceleration = currentAcceleration;
		return nextPosition;
	}

	private float X(float dt, float currentAcceleration) {
		return (float) (this.currentPosition + this.currentVelocity * dt + (2 / 3F) * currentAcceleration * Math.pow(dt, 2.0) - (1 / 6F) * this.previousAcceleration * Math.pow(dt, 2.0));
	}

	private float Vcorrector(float dt, float nextAcceleration, float currentAcceleration) {
		return this.currentVelocity + (1 / 3F) * nextAcceleration * dt + (5 / 6F) * currentAcceleration * dt - (1 / 6F) * this.previousAcceleration * dt;
	}

}
