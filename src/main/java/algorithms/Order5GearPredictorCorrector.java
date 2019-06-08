package algorithms;

import models.Force;

/**
 * @see "teorica4 diapo 21 y 22"
 * @see "https://slideplayer.com/slide/11345253/ diapo 26"
 * @see "https://www.slideshare.net/keerthanpg/integration-schemes-in-molecular-dynamics CLAVEEEEEE"
 */
public class Order5GearPredictorCorrector implements IntegrationMethod {

	private final double mass;
	private double r, r1, r2, r3, r4, r5;
	private Force force;

	public Order5GearPredictorCorrector(double mass,
	                                    double initialPosition,
	                                    double initialVelocity,
	                                    Force force) {
		this.mass = mass;
		this.r = initialPosition;
		this.r1 = initialVelocity;
		this.force = force;
		this.r2 = force.F(r, r1) / mass;
		this.r3 = force.F(r1, r2) / mass;
		this.r4 = force.F(r2, r3) / mass;
		this.r5 = force.F(r3, r4) / mass;
	}

	public double updatePosition(double dt) {

		// Predict
		double rp, rp1, rp2, rp3, rp4, rp5;
		rp = r + (r1 * dt) + ((r2 * Math.pow(dt, 2)) / 2.0) + ((r3 * Math.pow(dt, 3.0)) / 6.0) + ((r4 * Math.pow(dt, 4.0)) / 24.0) + ((r5 * Math.pow(dt, 5.0)) / 120.0);
		rp1 = r1 + (r2 * dt) + (r3 * Math.pow(dt, 2) / 2.0) + (r4 * Math.pow(dt, 3.0)) / 6.0 + (r5 * Math.pow(dt, 4.0)) / 24.0;
		rp2 = r2 + (r3 * dt) + ((r4 * Math.pow(dt, 2)) / 2.0) + (r5 * Math.pow(dt, 3.0)) / 6.0;
		rp3 = r3 + (r4 * dt) + ((r5 * Math.pow(dt, 2)) / 2.0);
		rp4 = r4 + (r5 * dt);
		rp5 = r5;

		// Evaluate
		double newForce = force.F(rp, rp1);
		double deltaAcceleration = (newForce / mass) - rp2;
		double deltaR2 = (deltaAcceleration * Math.pow(dt, 2.0)) / 2.0;

		// Correct
		r = rp + (3.0 / 16.0) * deltaR2;
		r1 = rp1 + ((251.0 / 360.0) * deltaR2 * 1.0) / dt;
		r2 = rp2 + (1.0 * deltaR2 * 2.0) / Math.pow(dt, 2);
		r3 = rp3 + ((11.0 / 18.0) * deltaR2 * 6.0) / Math.pow(dt, 3);
		r4 = rp4 + ((1.0 / 6.0) * deltaR2 * 24.0) / Math.pow(dt, 4);
		r5 = rp5 + ((1.0 / 60.0) * deltaR2 * 120.0) / Math.pow(dt, 5);

		return r;
	}
}
