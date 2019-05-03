package algorithms;

import models.Force;

public class VelocityVerlet implements IntegrationMethod {

	private float mass;
	private float currentPosition;
	private float currentVelocity;
	private Force force;

	public VelocityVerlet(float mass,
	                      float initialPosition,
	                      float initialVelocity,
	                      Force force) {
		this.mass = mass;
		this.currentPosition = initialPosition;
		this.currentVelocity = initialVelocity;
		this.force = force;
	}

	public float updatePosition(float dt) {
		float currentAcceleration = force.F(currentPosition, currentVelocity) / mass;

		float halfStepVelocity = (currentVelocity + currentAcceleration * (dt / 2.0F));
		float nextPosition = X(dt, currentAcceleration);

		float nextAcceleration = force.F(nextPosition, halfStepVelocity) / mass;
		float nextVelocity = halfStepVelocity + nextAcceleration * (dt / 2.0F);

		currentPosition = nextPosition;
		currentVelocity = nextVelocity;
		return nextPosition;
	}

	private float X(float dt, float currentAcceleration) {
		return (float) (this.currentPosition + this.currentVelocity * dt + 0.5F * currentAcceleration * Math.pow(dt, 2.0));
	}
}
