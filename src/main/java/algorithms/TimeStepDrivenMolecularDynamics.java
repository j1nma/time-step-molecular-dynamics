package algorithms;

import models.Particle;

import java.io.PrintWriter;
import java.util.List;

public class TimeStepDrivenMolecularDynamics {

	private static double currentSimulationTime = 0.0;

	public static void run(
			List<Particle> particles,
			StringBuffer buffer,
			PrintWriter eventWriter,
			double limitTime,
			double deltaT,
			double printDeltaT,
			double k,
			double vdc,
			double initialPosition,
			double initialVelocity) {

		eventWriter.close();
	}
}
