package algorithms;

public class Verlet {

	private static Double mass = 70.0; //kg
	private static Double Kconstant = 100000.0; // N/m
	private static Double gamma = 100.0; // kg/s
	private static Integer step = 0;

	// Parameters
	private static Double time = 0.0; // s
	private static Double dt = 1e-4; // s
	private static Double maxTime = 5.0; // s
	private static Double acceleration = 1.0; // ???

	// Initial State
	private static Double position = 1.0; // m (initial position on time=0)
	private static Double A = 1.0; // ????
	private static Double velocity = - A *(gamma/(2*mass)); // m/s (initial velocity on time=0)

	private static Double lastPosition = position;


	public static void main(String[] args) {
		run();
	}

	public static void run() {
		Double aux;
		Double realX;
		while( time<maxTime ) {
			time+=dt;
			aux = position;
			position = X(time, dt);
			velocity = V(time, dt);
			lastPosition = aux;

			realX = realPosition(time);
			System.out.printf("Step: %d Position: %f Velocity: %f Real Position: %f Error: %e   %s\n", step++, position, velocity, realX, Math.pow(Math.abs(realX-position),2), velocity>=0?"=>":"<=");
		}
	}

	private static Double X(Double time, Double delta) {
		return 2*position-lastPosition+((delta*delta)*(F(time)/mass)+O(time*time*time*time));
	}

	/**
	 * Aqui position es en t+deltaT
	 * @param time
	 * @param delta
	 * @return
	 */
	private static Double V(Double time, Double delta) {
		return ((position-lastPosition)/(2*delta))+O(time*time*time);
	}

	private static Double F(Double time) {
		return -Kconstant*position-gamma*velocity;
	}

	private static Double O(Double time) {
		return 0.0;
	}

	private static double realPosition(Double time) {
		return A * Math.exp(-(gamma/(2*mass)) * time) * Math.cos(Math.sqrt((Kconstant/mass) - (gamma*gamma / (4 * mass*mass))) * time);
	}
}
