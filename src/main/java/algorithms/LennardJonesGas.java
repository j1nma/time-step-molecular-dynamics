package algorithms;

import models.Criteria;
import models.Particle;
import models.TimeCriteria;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.PrintWriter;
import java.util.List;

/**
 * Consider a Lennard-Jones gas formed by particles whose dimensionless parameters are
 * rm (distance where V is minimum) = 1,
 * ε = 2,
 * m = 0.1,
 * and initial velocity v = 10.
 * <p>
 * The cutoff distance of the potential is r = 5.
 * <p>
 * The box containing the gas measures 200 units high x 400 wide with a partition that divides the box
 * into two halves of 200 x 200 and has a central hole of 10 units (qualitatively similar to Fig.1 of TP Nro .3).
 * <p>
 * Initially all the particles are on the left side of the box and as the system evolves they will spread
 * to the other half. The particles are confined in the box so the boundary condition is rigid walls.
 */
public class LennardJonesGas {

	private static double particleRadius = 1;
	private static double particleMass = 0.1;
	private static double initialParticleVelocity = 10;
	private static double holeDepth = 2;
	private static double interactionRadius = 5;
	private static double boxHeight = 200;
	private static double boxWidth = 400;
	private static double centralHoleUnits = 10;
	private static double currentSimulationTime = 0.0;

	// IO
	private static final String SCRIPTS_DIRECTORY = "./scripts";
	private static final String OCTAVE_FILE = SCRIPTS_DIRECTORY + "/gas.m";
	private static final String[] INTEGRATORS = {
			"Beeman",
			"VelocityVerlet",
			"Order5GearPredictorCorrector"
	};

	// Initial State
	private static double time = 0.0;

	public static void run(
			List<Particle> initialParticles,
			StringBuffer buffer,
			PrintWriter eventWriter,
			double limitTime,
			double dt,
			double printDeltaT,
			double k,
			double vdc,
			double initialPosition,
			double initialVelocity) {

		List<Particle> particles = initialParticles;

//		// Print to buffer and set dummy particles for Ovito grid
//		setDummyParticles(buffer, particles);
//
//		// Print particles
//		for (Particle p : particles)
//			buffer.append(particleToString(p)).append("\n");

		Criteria timeCriteria = new TimeCriteria(limitTime);

		// Print frame
		int currentFrame = 1;
		int printFrame = (int) Math.ceil(printDeltaT / dt);

		while (!timeCriteria.isDone(particles, time)) {

			// Calculate neighbours
			CellIndexMethod.run(particles,
					boxHeight > boxWidth ? boxHeight : boxWidth,
					(int) interactionRadius + 1, //TODO watch out, may delete "+1"
					interactionRadius);

			// si está cerca de una pared le agrego una falsa, no como vecina pero como parte de la colección para la sumatoria de fuerzas
			// Hacer una f(x) que reciba la partícula y un collection de partícula
			System.out.println("Es decir por cada particula calcular la sumatoria de todas las fuerzas de todas las particulas alrededor que le generan a esta.");

			System.out.println("Para eso ultimo usar algún integrador (el que menos memoria consuma).");

			System.out.println("manejar el caso de cuando interactua contra la esquina cuando cambia de box: como entre particulas hay fuerza de Potencial que repela");
			// la esquina estará entre la interacción, representada por una partícula

//			if ((currentFrame % printFrame) == 0) {
////				try {
////					writer.write(time, neighbours);
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
////			}

			time += dt;
			currentFrame++;
		}


//		eventWriter.close();
	}


	public static double calculatePotential(double distanceAtMinimum,
	                                        double distanceBetweenParticles,
	                                        double holeDepth) {
		double fraction = distanceAtMinimum / distanceBetweenParticles;
		return holeDepth * (Math.pow(fraction, 12) - 2.0 * Math.pow(fraction, 6));
	}

	private static void setDummyParticles(StringBuffer buff, List<Particle> particles) {
		// Particles for fixing Ovito grid
		Particle dummy1 = new Particle(-1, 0);
		Particle dummy2 = new Particle(0, 0);
		dummy1.setPosition(new Vector2D(0, 0));
		dummy1.setVelocity(new Vector2D(0, 0));
		dummy2.setPosition(new Vector2D(boxWidth, 200));
		dummy2.setVelocity(new Vector2D(0, 0));

		// Print dummy particles to simulation output file
		buff.append(particles.size() + 2).append("\n")
				.append(0 + "\n")
				.append(particleToString(dummy1)).append("\n")
				.append(particleToString(dummy2)).append("\n");
	}


	private static String particleToString(Particle p) {
		return p.getId() + " " +
				p.getPosition().getX() + " " +
				p.getPosition().getY() + " " +
				p.getVelocity().getX() + " " +
				p.getVelocity().getY() + " "
				;
	}

	public static void run(List<Particle> particles, StringBuffer buffer, PrintWriter eventWriter, double limitTime, double deltaT, double printDeltaT, double k, double vdc, double initialPosition, double mass, String leftParticlesPlotFile) {

	}
}
