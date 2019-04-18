package algorithms;

import java.util.HashMap;
import java.util.Map;

/**
 * @see "teorica4 diapo 21 y 22"
 */
public class Order5GearPredictorCorrector {

	public static void main(String[] args) {
		run();
	}

	public static void run() {
		Integer step = 0;

		// Parameters
		Double time = 0.0; // s
		Double dt = 0.1; // s
		Double mass = 70.0; //kg
		Double Kconstant = 100000.0; // N/m
		Double lambda = 100.0; // kg/s
		Double maxTime = 5.0; // s
		Double acceleration = 1.0; // ???

		// Initial State
		Double position = 1.0; // m (initial position on time=0)
		Double A = 1.0; // ????
		Double velocity = - A *(lambda/(2*mass)); // m/s (initial velocity on time=0)

		// inicializo sistema variables y preparo situacion inicial
		// me genero el paso r(0) y v(0)
		xValues.put(time, position);
		vValues.put(time, velocity);

		while (time < maxTime) {
			System.out.printf("Step: %d  Position: %f  Velocity: %f  Acceleration: %f\n",step++, position, velocity, acceleration);
			// a partir del ultimo paso en el tiempo t q fue la iteracion anterior

			// STEP-1  predigo la proximo velocidad  y posicion en t+deltaT
			// vip(t+Δt) = vi(t) + ai(t)Δt
			velocity = Vpredictivo(time, dt);
			// rip(t+Δt) = ri(t) + vi(t)Δt
			position = Xpredictivo(time, dt);

			// STEP-2 teniendo las predicciones calculo las aceleraciones en t+deltaT
			acceleration = 1.0; // ????

			// STEP-3 sabiendo la probable aceleracion en t+deltaT,
			// usamos esta para calcular de nuevo el paso uno pero con mas exactitud segun
			// vi(t+Δt) = vi(t) + ai(t+Δt)Δt
			velocity = Vcorrection(time, dt);
			vValues.put(time+dt, velocity);
			// ri(t+Δt) = ri(t) + vi(t+Δt)Δt
			position = Xcorrection(time, dt);
			xValues.put(time+dt, position);
			// veamos que igual seguimos prediciondo t+deltaT


			time+=dt;
		}
	}

	static Map<Double, Double> vValues = new HashMap<>();
	static Map<Double, Double> xValues = new HashMap<>();


	/**
	 * Used for step one. Predict next velocity
	 * @param t
	 * @param delta
	 * @return
	 */
	private static Double Vpredictivo(Double t, Double delta) {
		return (vValues.get(t) + (A(t)*delta));
	}

	/**
	 * Used for step one. Predict next position
	 * @param t
	 * @param delta
	 * @return
	 */
	private static Double Xpredictivo(Double t, Double delta) {
		return (xValues.get(t) + (vValues.get(t) *delta));
	}

	/**
	 * Used for step two. Correct next velocity
	 * @param t
	 * @param delta
	 * @return
	 */
	private static Double Vcorrection(Double t, Double delta) {
		return (vValues.get(t) + (A(t+delta)*delta));
	}

	/**
	 * Used for step two. Correct next position
	 * @param t
	 * @param delta
	 * @return
	 */
	private static Double Xcorrection(Double t, Double delta) {
		return (xValues.get(t) + (vValues.get(t+delta) *delta));
	}

	private static Double A(Double time) {
		return 1.0;
	}
}
