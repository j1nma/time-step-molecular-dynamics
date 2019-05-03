package algorithms;

import models.Force;

/**
 * @see "teorica4 diapo 21 y 22"
 * @see "https://slideplayer.com/slide/11345253/ diapo 26"
 * @see "https://www.slideshare.net/keerthanpg/integration-schemes-in-molecular-dynamics CLAVEEEEEE"
 */
public class Order5GearPredictorCorrector implements IntegrationMethod {

	private float mass;
	private float r, r1, r2, r3, r4, r5;
	private Force force;

	public Order5GearPredictorCorrector(float mass,
	                                    float initialPosition,
	                                    float initialVelocity,
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

	public float updatePosition(float dt) {

		// Predict
		float rp, rp1, rp2, rp3, rp4, rp5;
		rp = (float) (r + (r1 * dt) + ((r2 * Math.pow(dt, 2)) / 2F) + ((r3 * Math.pow(dt, 3F)) / 6F) + ((r4 * Math.pow(dt, 4F)) / 24F) + ((r5 * Math.pow(dt, 5F)) / 120F));
		rp1 = (float) (r1 + (r2 * dt) + (r3 * Math.pow(dt, 2) / 2F) + (r4 * Math.pow(dt, 3F)) / 6F + (r5 * Math.pow(dt, 4F)) / 24F);
		rp2 = (float) (r2 + (r3 * dt) + ((r4 * Math.pow(dt, 2)) / 2F) + (r5 * Math.pow(dt, 3F)) / 6F);
		rp3 = (float) (r3 + (r4 * dt) + ((r5 * Math.pow(dt, 2)) / 2F));
		rp4 = r4 + (r5 * dt);
		rp5 = r5;

		// Evaluate
		float newForce = force.F(rp, rp1);
		float deltaAcceleration = (newForce / mass) - rp2;
		float deltaR2 = (float) ((deltaAcceleration * Math.pow(dt, 2F)) / 2F);

		// Correct
		r = rp + (3 / 16F) * deltaR2;
		r1 = rp1 + ((251 / 360F) * deltaR2 * 1F) / dt;
		r2 = (float) (rp2 + (1 * deltaR2 * 2F) / Math.pow(dt, 2));
		r3 = (float) (rp3 + ((11 / 18F) * deltaR2 * 6F) / Math.pow(dt, 3));
		r4 = (float) (rp4 + ((1 / 6F) * deltaR2 * 24F) / Math.pow(dt, 4));
		r5 = (float) (rp5 + ((1 / 60F) * deltaR2 * 120F) / Math.pow(dt, 5));

		return r;
	}
}
