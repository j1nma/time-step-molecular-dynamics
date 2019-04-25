package algorithms;

import io.OctaveWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Stack;

public class Beeman {

	private static final String SCRIPTS_DIRECTORY = "./scripts";
	private static final String BEEMAN_OCTAVE_FILE = SCRIPTS_DIRECTORY + "/beeman.m";

	public static void main(String[] args) {
		run();
	}

	private static Double Flast;

	private static Integer step = 0;

	// Parameters
	private static Double time = 0.0; // s
	private static Double dt = 0.001; // s
	private static Double mass = 70.0; // kg
	private static Double Kconstant = 10000.0; // N/m
	private static Double gamma = 100.0; // kg/s
	private static Double maxTime = 5.0; // s

	// Initial State
	private static Double position = 1.0; // m (initial position at time=0)
	private static Double A = 1.0; // ????
	private static Double velocity = -A * (gamma / (2 * mass)); // m/s (initial velocity at time=0)
	private static Double lastX = position;
	private static Double lastV = velocity;
	private static Double velocityPredicted;

	public static void run() {

		final Stack<Double> timeStepValues = new Stack<>();
		final Stack<Double> analyticValues = new Stack<>();
		final Stack<Double> positionsValues = new Stack<>();

		timeStepValues.push(time);
		analyticValues.push(realPosition(time));
		positionsValues.push(position);

		Flast = F(position, velocity);

		while (time < maxTime) {
			lastX = position;

			// Calculate current position
			position = X(dt);

			// Predict velocity
			velocityPredicted = Vpredictor(dt);

			// Correct velocity
			lastV = velocity;
			velocity = Vcorrector(dt, position, velocityPredicted);

			Flast = F(lastX, lastV);

//			System.out.printf("Step: %d Position: %f Velocity: %f  Error: %e  %s\n", step++, position, velocity, Math.pow(Math.abs(realPosition(time) - position), 2), velocity >= 0 ? "-->" : "    <--");
			time += dt;
			timeStepValues.push(time);
			analyticValues.push(realPosition(time));
			positionsValues.push(position);
		}

		OctaveWriter octaveWriter;
		try {
			octaveWriter = new OctaveWriter(Paths.get(BEEMAN_OCTAVE_FILE));
			octaveWriter.writePositionsThroughTime(timeStepValues, analyticValues, positionsValues);
			octaveWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Double X(Double delta) {
		return position + velocity * delta + ((2.0 / 3) * delta * delta) * (F(position, velocity) / mass) - ((1.0 / 6) * Flast / mass) * delta * delta;
	}

	private static Double Vpredictor(Double delta) {
		return velocity + (3.0 / 2) * (F(position, velocity) / mass) * delta - 0.5 * (Flast / mass) * delta;
	}

	private static Double Vcorrector(Double delta, Double Xnext, Double Vnext) {
		return velocity + (1.0 / 3) * (F(Xnext, Vnext) / mass) * delta + (5.0 / 6) * (F(lastX, lastV) / mass) * delta - (1.0 / 6) * (Flast / mass) * delta;
	}

	private static Double F(Double x, Double v) {
		return (-Kconstant * x - gamma * v);
	}

	private static double realPosition(Double time) {
		return A * Math.exp(-(gamma / (2 * mass)) * time) * Math.cos(Math.sqrt((Kconstant / mass) - (gamma * gamma / (4 * mass * mass))) * time);
	}
}
