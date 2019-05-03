package algorithms;

import io.OctaveWriter;
import models.Force;
import models.SpringAnalyticSolution;
import models.SpringForce;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Stack;

public class TimeStepDrivenMolecularDynamics {

	private static final String SCRIPTS_DIRECTORY = "./scripts";
	private static final String OCTAVE_FILE = SCRIPTS_DIRECTORY + "/positions.m";
	private static final String[] INTEGRATORS = {
			"Beeman",
			"VelocityVerlet",
			"Order5GearPredictorCorrector"
	};

	// Initial State
	private static float time = 0.0F;

	public static void run(
			float limitTime,
			float dt,
			float printDeltaT,
			float k,
			float gamma,
			float initialPosition,
			float mass,
			String positionsPlotFile) {

		// Initialize state
		float initialVelocity = -1 * (gamma / (2 * mass)); // m/s (initial velocity at time=0)

		Stack<Float> timeStepValues = new Stack<>();
		Stack<Float> analyticValues = new Stack<>();
		Stack<Float> beemanPositionValues = new Stack<>();
		Stack<Float> velocityVerletPositionValues = new Stack<>();
		Stack<Float> order5GearPredictorCorrectorPositionValues = new Stack<>();

		Force springForce = new SpringForce(k, gamma);
		float previousAcceleration = springForce.F(initialPosition, initialVelocity) / mass;

		SpringAnalyticSolution springAnalyticSolution = new SpringAnalyticSolution();
		Beeman beeman = new Beeman(mass, initialPosition, initialVelocity, previousAcceleration, springForce);
		VelocityVerlet velocityVerlet = new VelocityVerlet(mass, initialPosition, initialVelocity, springForce);
		Order5GearPredictorCorrector order5GearPredictorCorrector = new Order5GearPredictorCorrector(mass, initialPosition, initialVelocity, springForce);

		timeStepValues.push(time);
		analyticValues.push(springAnalyticSolution.getPosition(k, gamma, mass, time));
		beemanPositionValues.push(initialPosition);
		velocityVerletPositionValues.push(initialPosition);
		order5GearPredictorCorrectorPositionValues.push(initialPosition);

		// MSE for each integrator
		float[] mse = new float[3];

		// Print frame
		int currentFrame = 1;
		int printFrame = (int) Math.ceil(printDeltaT / dt);

		while (time < limitTime) {
			time += dt;

			float analyticValue = springAnalyticSolution.getPosition(k, gamma, mass, time);

			float beemanPosition = beeman.updatePosition(dt);
			mse[0] += Math.pow(analyticValue - beemanPosition, 2);

			float velocityPosition = velocityVerlet.updatePosition(dt);
			mse[1] += Math.pow(analyticValue - velocityPosition, 2);

			float order5GearPredictorCorrectorPosition = order5GearPredictorCorrector.updatePosition(dt);
			mse[2] += Math.pow(analyticValue - order5GearPredictorCorrectorPosition, 2);

			if ((currentFrame % printFrame) == 0) {
				timeStepValues.push(time);
				analyticValues.push(analyticValue);
				beemanPositionValues.push(beemanPosition);
				velocityVerletPositionValues.push(velocityPosition);
				order5GearPredictorCorrectorPositionValues.push(order5GearPredictorCorrectorPosition);
			}
			currentFrame++;
		}

		// MSEs normalized by total number of steps
		for (int i = 0; i < mse.length; i++) {
			mse[i] /= (limitTime / dt);
			System.out.print(INTEGRATORS[i] + ":\t" + mse[i] + " [m^2]\n");
		}

		OctaveWriter octaveWriter;
		try {
			octaveWriter = new OctaveWriter(Paths.get(OCTAVE_FILE));
			octaveWriter.writePositionsThroughTime(
					timeStepValues,
					analyticValues,
					beemanPositionValues,
					velocityVerletPositionValues,
					order5GearPredictorCorrectorPositionValues,
					positionsPlotFile);
			octaveWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
