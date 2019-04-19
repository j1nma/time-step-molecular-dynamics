package algorithms;

import sun.java2d.cmm.kcms.KcmsServiceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @see "teorica4 diapo 21 y 22"
 * @see "https://slideplayer.com/slide/11345253/ diapo 26"
 * @see "https://www.slideshare.net/keerthanpg/integration-schemes-in-molecular-dynamics CLAVEEEEEE"
 */
public class Order5GearPredictorCorrector {

	public static void main(String[] args) {
		run();
	}

	private static Integer step = 0;

	// Parameters
	private static Double time = 0.0; // s
	private static Double dt = 1e-4; // s
	private static Double mass = 70.0; //kg
	private static Double Kconstant = 100000.0; // N/m
	private static Double gamma = 100.0; // kg/s
	private static Double maxTime = 5.0; // s
	private static Double acceleration = 1.0; // ???

	// Initial State
	private static Double position = 1.0; // m (initial position on time=0)
	private static Double A = 1.0; // ????
	private static Double velocity = 0.0; //- A *(gamma/(2*mass)); // m/s (initial velocity on time=0)

	public static void run() {

		System.out.println();
		System.out.println();
		System.out.println();

		while (time < maxTime) {
			System.out.printf("Step: %d  Position: %e  Velocity: %f  Error2: %e Real: %e\n",step++, position, velocity, Math.pow(Math.abs(realPosition(time)-position),2), realPosition(time));


			Double Xp2 = F(position,velocity)/mass;
			Double Xp3 = F(velocity,Xp2)/mass;
			Double Xp4 = F(Xp2,Xp3)/mass;
			Double Xp5 = F(Xp3,Xp4)/mass;


			// Predict
			Double r0p = position + velocity * dt + Xp2 * Math.pow(dt,2) / 2 + Xp3 * Math.pow(dt,3) / 6 + Xp4 * Math.pow(dt,4) / 24 + Xp5 * Math.pow(dt,5) / 120;
			Double r1p = velocity + Xp2 * dt + Xp3 * Math.pow(dt,2) / 2 + Xp4 * Math.pow(dt,3) / 6 + Xp5 * Math.pow(dt,4) / 24;
			Double r2p = Xp2 + Xp3 * dt + Xp4 * Math.pow(dt,2) / 2 + Xp5 * Math.pow(dt,3) / 6;

			// Estimate
			Double dr2 = ((F(r0p, r1p)/mass) - r2p) * Math.pow(dt,2) / 2;

			// Correct
			position = r0p+(3.0/16)*dr2;
			velocity = r1p+(251.0/360)*dr2;

			time+=dt;
		}
	}


	private static Double F(Double x, Double y) {
		return -Kconstant*x-y*gamma;
	}

	private static double realPosition(Double t) {
		return A * Math.exp(-(gamma/(2*mass)) * t) * Math.cos(Math.sqrt((Kconstant/mass) - (Math.pow(gamma,2)/ (4 * Math.pow(mass,2)))) * t);
	}
}
