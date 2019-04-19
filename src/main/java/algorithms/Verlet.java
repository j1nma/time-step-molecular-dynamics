package algorithms;

import java.io.IOException;

// TODO refactor name to chosen Verlet variant
public class Verlet {

	private static Double mass = 70.0; //kg
	private static Double Kconstant = 100000.0; // N/m
	private static Double gamma = 100.0; // kg/s
	private static Integer step = 0;

	// Parameters
	private static Double time = 0.0; // s
	private static Double dt = 0.001; // s
	private static Double maxTime = 5.0; // s
	private static Double acceleration = 1.0; // ???

	// Initial State
	private static Double position = 1.0; // m (initial position on time=0)
	private static Double A = 1.0; // ????
	private static Double velocity = - A *(gamma/(2*mass)); // m/s (initial velocity on time=0)

	private static Double lastPosition = 0.0;


	public static void main(String[] args) {
		run();
	}

	public static void run() {
		Double aux;
		while( time<maxTime ) {
			System.out.printf("Step: %d Position: %f Velocity: %f    %s\n", step++, position, velocity, velocity>=0?"=>":"<=");
			aux = position;
			position = X(time, dt);
			velocity = V(time, dt);
			lastPosition = aux;

			time+=dt;
		}
	}

	private static Double X(Double time, Double delta) {
		return 2*position-lastPosition+((delta*delta/mass)*F(time)+O(time*time*time*time));
	}

	private static Double V(Double time, Double delta) {
		return ((position-lastPosition)/(2*delta))+O(time*time*time);
	}

	private static Double F(Double time) {
		return -Kconstant*position-gamma*velocity;
	}

	private static Double O(Double time) {
		return 0.0;
	}

}
