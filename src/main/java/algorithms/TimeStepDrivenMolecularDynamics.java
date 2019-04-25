package algorithms;

import io.OctaveWriter;
import models.Particle;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

public class TimeStepDrivenMolecularDynamics {

	private static final String SCRIPTS_DIRECTORY = "./scripts";
	private static final String OCTAVE_FILE = SCRIPTS_DIRECTORY + "/positions.m";

	// Initial State
	private static double time = 0.0;

	public static void run(
			List<Particle> particles,
			StringBuffer buffer,
			PrintWriter eventWriter,
			double limitTime,
			double dt,
			double printDeltaT,
			double k,
			double gamma,
			double initialPosition,
			double mass,
			String positionsPlotFile) {

		// Initialize state
		double initialVelocity = -1 * (gamma / (2 * mass)); // m/s (initial velocity at time=0)

		// ++++ BEGIN MAIN LOOP THAT RUNS ALL INTEGRATORS
		final Stack<Double> timeStepValues = new Stack<>();
		final Stack<Double> analyticValues = new Stack<>();
		final Stack<Double> beemanPositionValues = new Stack<>();
		final Stack<Double> velocityVerletPositionValues = new Stack<>();

		Force springForce = new SpringForce(k, gamma);
		double previousAcceleration = springForce.F(initialPosition, initialVelocity) / mass;

		SpringAnalyticSolution springAnalyticSolution = new SpringAnalyticSolution();
		Beeman beeman = new Beeman(mass, initialPosition, initialVelocity, previousAcceleration, springForce);
		VelocityVerlet velocityVerlet = new VelocityVerlet(mass, initialPosition, initialVelocity, springForce);

		timeStepValues.push(time);
		analyticValues.push(springAnalyticSolution.getPosition(k, gamma, mass, time));
		beemanPositionValues.push(initialPosition);
		velocityVerletPositionValues.push(initialPosition);

		while (time < limitTime) {
			time += dt;
			timeStepValues.push(time);
			analyticValues.push(springAnalyticSolution.getPosition(k, gamma, mass, time));
			beemanPositionValues.push(beeman.updatePosition(dt));
			velocityVerletPositionValues.push(velocityVerlet.updatePosition(dt));
		}

		OctaveWriter octaveWriter;
		try {
			octaveWriter = new OctaveWriter(Paths.get(OCTAVE_FILE));
			octaveWriter.writePositionsThroughTime(
					timeStepValues,
					analyticValues,
					beemanPositionValues,
					velocityVerletPositionValues,
					positionsPlotFile);
			octaveWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ++++ END MAIN LOOP THAT RUNS ALL INTEGRATORS


//		eventWriter.close();

//		System.out.println("Generar en java todas las particulas. no hace falta hacerlo en python. Recibir por" +
//				"parametro la cantidad y las dimensiones del lugar");
//
//		System.out.println("hacer el while y que en cada frame se calcule la posicion y velocidad de todas respecto de otras");
//
//		System.out.println("Es decir por cada particula calcular la sumatoria de todas las fuerzas de todas las particulas alrededor que le generan a esta.");
//
//		System.out.println("Para eso ultimo mirar verlet basico y usar ese.");
//
//		System.out.println("recordar que esto es por tiempo. no por eventos");
//
//		System.out.println("no c como manejar el caso de cuando choca contra la esquina cuando cambia de box....");

	}
}
