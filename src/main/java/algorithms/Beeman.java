package algorithms;

public class Beeman {

	public static void main(String[] args) {
		run();
	}

	private static Double Flast;

	private static Integer step = 0;

	// Parameters
	private static Double time = 0.0; // s
	private static Double dt = 0.001; // s
	private static Double mass = 70.0; //kg
	private static Double Kconstant = 100000.0; // N/m
	private static Double gamma = 100.0; // kg/s
	private static Double maxTime = 5.0; // s

	// Initial State
	private static Double position = 1.0; // m (initial position on time=0)
	private static Double A = 1.0; // ????
	private static Double velocity = - A *(gamma/(2*mass)); // m/s (initial velocity on time=0)
	private static Double lastX = position;
	private static Double lastV = velocity;
	private static Double velocityPredicted;

	public static void run() {


		Flast =F(position, velocity);

		while (time < maxTime) {
			System.out.printf("Step: %d Position: %f Velocity: %f    %s\n",step++,position,velocity,velocity>=0?"-->":"    <--");
			lastX = position;
			// saco X
			position = X(time, dt);
			// predigo V
			velocityPredicted = Vpredictor(time, dt);
			// correcion V
			lastV = velocity;
			velocity = Vcorrector(time, dt, position, velocityPredicted);
			Flast = F(lastX, lastV);

			time+=dt;
		}

	}

	private static Double X(Double time, Double delta) {
		return position + velocity*delta+ ((2.0/3)*delta*delta)*(F(position, velocity)/mass)-((1.0/6)*Flast/mass)*delta*delta;
	}

	private static Double Vpredictor(Double time, Double delta) {
		return velocity+ (3.0/2)*(F(position, velocity)/mass)*delta-0.5*(Flast/mass)*delta;
	}

	private static Double Vcorrector(Double time, Double delta, Double Xnext, Double Vnext) {
		return velocity+ (1.0/3)*(F(Xnext, Vnext)/mass)*delta+(5.0/6)*(F(lastX,lastV)/mass)*delta-(1.0/6)*(Flast/mass)*delta;
	}

	private static Double F(Double x, Double v) { return (-Kconstant*x-gamma*v); }

}
