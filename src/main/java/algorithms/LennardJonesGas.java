package algorithms;

import algorithms.neighbours.IntegrationMethodWithNeighbours;
import models.Criteria;
import models.Particle;
import models.TimeCriteria;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
	private static double RM = 1.0;
	private static double e = 0.1;

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

	// Each particle's integration method
	private final Map<Particle, IntegrationMethodWithNeighbours> particleIntegrationMethods;

	public LennardJonesGas(Map<Particle, IntegrationMethodWithNeighbours> particleIntegrationMethods) {
		this.particleIntegrationMethods = particleIntegrationMethods;
	}

	public static void run(
			List<Particle> particles,
			StringBuffer buffer,
			PrintWriter eventWriter,
			double limitTime,
			double dt,
			double printDeltaT,
			double k,
			double vdc,
			double initialPosition,
			double initialVelocity,
			String LEFT_PARTICLES_PLOT_FILE) {


//		// Print to buffer and set dummy particles for Ovito grid
		printFirstFrame(buffer, particles);

		Criteria timeCriteria = new TimeCriteria(limitTime);

		// Print frame
		int currentFrame = 1;
		int printFrame = (int) Math.ceil(printDeltaT / dt);

		while (!timeCriteria.isDone(particles, time)) {
			// TODO ese 2 no es verdad, ya que las dummy particles de paredes aparecen y desaparecen...
			//  son las dos dummy de esquina mas esas...como hacer para saber si van a haber o no de antemano?
			//  o sino registarr el maximo posible y que esten en las esquinas hasta necesitarse
			buffer.append(particles.size() + 2).append("\n").append(currentFrame + "\n");

			// Calculate neighbours
			CellIndexMethod.run(particles,
					boxHeight > boxWidth ? boxHeight : boxWidth,
					(int) interactionRadius + 1, //TODO watch out, may delete "+1"
					interactionRadius);

			// calcular la sumatoria de fuerzas de cada particula con falsas para las paredes
			particles.stream().parallel().forEach(p -> {
				Set<Particle> neighboursCustom = new HashSet<>(p.getNeighbours());
//				addFakeWallParticles(p, neighboursCustom);
				calculateForce(p, neighboursCustom);
			});

			// calculo nueva posicion e imprimo
			particles.stream().parallel().forEach(p -> {
				// get new X position
				double Ax = p.getForce().getX()/p.getMass(); // acceleration in X axis
				double X = p.getPosition().getX() + Ax*dt; // new X position
				// get new Y position
				double Ay = p.getForce().getY()/p.getMass(); // acceleration in Y axis
				double Y = p.getPosition().getY() + Ay*dt; // new Y position

				p.setPosition(new Vector2D(X,Y));

				// la imprimo TODO agregar aca para que solo cada printDeltaT
				buffer.append(particleToString(p));
			});

			// agrego las dummy
			printGridDummyParticles(buffer);

			time += dt;
			currentFrame++;
		}

	}


	/**
	 * Calcula la sumatoria de fuerzas sobre la particula
	 *
	 * @param particle
	 * @param neighbours
	 */
	private static void calculateForce(Particle particle, Set<Particle> neighbours) {
		Vector2D F = new Vector2D(0,0);
		F = neighbours.stream().map(p2 -> {
			// sacar angulo entre particulas  atam2
			double angle = particle.getAngleWith(p2);

			// calculo modulo de la fuerza
//			double r = ;
			double fraction = RM / particle.getDistanceBetween(p2);
			double force = (12*e/RM)*(Math.pow(fraction,13)-Math.pow(fraction,7));

			// descompongo force con angle para sacar f.x y f.y
			return new Vector2D(force*Math.cos(angle),force*Math.sin(angle));
		}).reduce(F, (F1, F2) -> F1.add(F2));

		particle.setForce(F);
	}


	/**
	 * Calcula el potencial entre dos particulas
	 *
	 * @param distanceAtMinimum
	 * @param distanceBetweenParticles
	 * @param holeDepth
	 * @return
	 */
	private static double calculatePotential(double distanceAtMinimum,
	                                        double distanceBetweenParticles,
	                                        double holeDepth) {
		double fraction = RM / distanceBetweenParticles;
		return holeDepth * (Math.pow(fraction, 12) - 2.0 * Math.pow(fraction, 6));
	}

	/**
	 *
	 * @param particle
	 * @param neighbours
	 * @return
	 */
	private Particle moveParticle(Particle particle, Set<Particle> neighbours) {
		neighbours = neighbours
				.stream()
				.filter(n -> !centralHoleInBetween(particle, n))
				.collect(Collectors.toSet());

		addFakeWallParticles(particle, neighbours);

		IntegrationMethodWithNeighbours integrationMethod = particleIntegrationMethods.get(particle);
		return integrationMethod.updatePosition(particle, neighbours, time);
	}

	/**
	 * Dada dos particulas, si estan en cuadrantes distintas
	 * @param particle1
	 * @param particle2
	 * @return
	 */
	private boolean centralHoleInBetween(Particle particle1, Particle particle2) {
		double centralHoleLowerLimit = (boxHeight / 2) - (centralHoleUnits / 2);
		double centralHoleHigherLimit = boxHeight - centralHoleLowerLimit;

		double x1 = particle1.getPosition().getX();
		double y1 = particle1.getPosition().getY();
		double x2 = particle2.getPosition().getX();
		double y2 = particle2.getPosition().getY();

		// Both particles at left or right, one above the other
		if (x1 == x2) return false;

		// Calculate line between particles' positions
		double m = (y2 - y1) / (x2 - x1);
		double b = y1 - m * x1;

		// Calculate central hole's height is in that line
		double xCentralHole = boxWidth / 2;
		double yCentralHoleBetweenParticles = m * xCentralHole + b;

		// Return true if central hole's y is between the gap
		// And particles are at different sides
		return yCentralHoleBetweenParticles < centralHoleHigherLimit
				&& yCentralHoleBetweenParticles > centralHoleLowerLimit
				&& ((x1 < boxWidth / 2 && x2 > boxWidth / 2)
				|| (x1 > boxWidth / 2 && x2 < boxWidth / 2));
	}

	/**
	 * Agrega en neighbours set las particulas falsas necesarias
	 *
	 * @param particle
	 * @param neighbours
	 */
	private static void addFakeWallParticles(Particle particle, Set<Particle> neighbours) {
		double centralHoleLowerLimit = (boxHeight / 2) - (centralHoleUnits / 2);
		double centralHoleHigherLimit = boxHeight - centralHoleLowerLimit;

		int fakeId = -1;

		// Analyse left wall
		double distanceToLeftWall = particle.getPosition().getX();
		double distanceToCentralWidth = distanceToLeftWall - boxWidth / 2;
		if (distanceToCentralWidth > 0
				&& distanceToCentralWidth <= interactionRadius
				&& (particle.getPosition().getY() <= centralHoleLowerLimit
				|| particle.getPosition().getY() >= centralHoleHigherLimit)) {
			// Add fake wall particle to its left at right side of middle wall
			Particle leftWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			leftWallParticle.setPosition(new Vector2D(boxWidth / 2, particle.getPosition().getY()));
			leftWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(leftWallParticle);
		} else if (distanceToLeftWall <= interactionRadius) {
			// Add fake wall particle to its left at the box's left wall
			Particle leftWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			leftWallParticle.setPosition(new Vector2D(0.0, particle.getPosition().getY()));
			leftWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(leftWallParticle);
		}

		// Analyse right wall
		double distanceToRightWall = boxWidth - particle.getPosition().getX();
		distanceToCentralWidth = distanceToRightWall - boxWidth / 2;
		if (distanceToCentralWidth > 0
				&& distanceToCentralWidth <= interactionRadius
				&& (particle.getPosition().getY() <= centralHoleLowerLimit
				|| particle.getPosition().getY() >= centralHoleHigherLimit)) {
			// Add fake wall particle to its left at right side of middle wall
			Particle rightWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			rightWallParticle.setPosition(new Vector2D(boxWidth / 2, particle.getPosition().getY()));
			rightWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(rightWallParticle);
		} else if (distanceToRightWall <= interactionRadius) {
			// Add fake wall particle to its left at the box's right wall
			Particle rightWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			rightWallParticle.setPosition(new Vector2D(boxWidth, particle.getPosition().getY()));
			rightWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(rightWallParticle);
		}

		// Analyse up wall
		double distanceToTopWall = boxHeight - particle.getPosition().getY();
		double distanceToCentralHoleHigherLimit = centralHoleHigherLimit - particle.getPosition().getY();
		// If CentralHoleHigherLimit is within interaction of particle, don't add a fake wall particle
		if (!(particle.getPosition().getX() == boxWidth / 2)
				|| !(distanceToCentralHoleHigherLimit >= 0)
				|| !(distanceToCentralHoleHigherLimit <= interactionRadius)) {
			if (distanceToTopWall <= interactionRadius) {
				Particle topWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
				topWallParticle.setPosition(new Vector2D(particle.getPosition().getX(), boxHeight));
				topWallParticle.setVelocity(Vector2D.ZERO);
				neighbours.add(topWallParticle);
			}
		}

		// Analyse down wall
		double distanceToLowerWall = particle.getPosition().getY() - 0.0;
		double distanceToCentralHoleLowerLimit = particle.getPosition().getY() - centralHoleLowerLimit;
		// If distanceToCentralHoleLowerLimit is within interaction of particle, don't add a fake wall particle
		if (!(particle.getPosition().getX() == boxWidth / 2)
				|| !(distanceToCentralHoleLowerLimit >= 0)
				|| !(distanceToCentralHoleLowerLimit <= interactionRadius)) {
			if (distanceToLowerWall <= interactionRadius) {
				Particle lowerWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
				lowerWallParticle.setPosition(new Vector2D(particle.getPosition().getX(), 0.0));
				lowerWallParticle.setVelocity(Vector2D.ZERO);
				neighbours.add(lowerWallParticle);
			}
		}

		// Particle between ys of central hole
		if (particle.getPosition().getY() > centralHoleLowerLimit
				&& particle.getPosition().getY() < centralHoleHigherLimit) {
			Vector2D centralHoleLowerLimitPosition = new Vector2D(boxWidth / 2, centralHoleLowerLimit);
			Vector2D centralHoleHigherLimitPosition = new Vector2D(boxWidth / 2, centralHoleHigherLimit);
			double centralHoleLowerLimitDistance = particle.getPosition().distance(centralHoleLowerLimitPosition);
			double centralHoleHigherLimitDistance = particle.getPosition().distance(centralHoleHigherLimitPosition);

			if (centralHoleLowerLimitDistance <= interactionRadius) {
				Particle centralHoleLowerLimitWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
				centralHoleLowerLimitWallParticle.setPosition(centralHoleLowerLimitPosition);
				centralHoleLowerLimitWallParticle.setVelocity(Vector2D.ZERO);
				neighbours.add(centralHoleLowerLimitWallParticle);
			}

			if (centralHoleHigherLimitDistance <= interactionRadius) {
				Particle centralHoleHigherLimitWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
				centralHoleHigherLimitWallParticle.setPosition(centralHoleHigherLimitPosition);
				centralHoleHigherLimitWallParticle.setVelocity(Vector2D.ZERO);
				neighbours.add(centralHoleHigherLimitWallParticle);
			}
		}
	}

	private static void printFirstFrame(StringBuffer buff, List<Particle> particles) {


		// Print dummy particles to simulation output file
		buff.append(particles.size() + 2).append("\n").append(0 + "\n");
		printGridDummyParticles(buff);

		// Print remaining particles
		particles.forEach(particle -> buff.append(particleToString(particle)));
	}

	private static void printGridDummyParticles(StringBuffer buff) {
		// Particles for fixing Ovito grid
		Particle dummy1 = new Particle(-100, 0);
		Particle dummy2 = new Particle(-101, 0);
		dummy1.setPosition(new Vector2D(0, 0));
		dummy1.setVelocity(new Vector2D(0, 0));
		dummy2.setPosition(new Vector2D(boxWidth, 200));
		dummy2.setVelocity(new Vector2D(0, 0));
		buff
				.append(particleToString(dummy1))
				.append(particleToString(dummy2));
	}

	private static String particleToString(Particle p) {
		return p.getId() + " " +
				p.getPosition().getX() + " " +
				p.getPosition().getY() + " " +
				p.getVelocity().getX() + " " +
				p.getVelocity().getY() + " \n"
				;
	}
}
