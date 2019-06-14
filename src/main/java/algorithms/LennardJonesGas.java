package algorithms;

import algorithms.neighbours.IntegrationMethodWithNeighbours;
import algorithms.neighbours.VerletWithNeighbours;
import models.Criteria;
import models.Particle;
import models.StabilizedBoxCriteria;
import models.TimeCriteria;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

	// Box dimensions
	private static final double boxHeight = 200;
	private static final double boxWidth = 400;
	private static final double centralHoleUnits = 10;

	// Potential distance cut
	private static final double interactionRadius = 5;

	// Distance at minimum potential
	private static final double RM = 1.0;

	// Hole depth
	private static final double e = 2.0;

	// Initial State
	private static double time = 0.0;

	// Each particle's integration method
	private static final Map<Particle, IntegrationMethodWithNeighbours> particleIntegrationMethods = new HashMap<>();

	public static void run(
			List<Particle> particles,
			BufferedWriter buffer,
			BufferedWriter energyBuffer,
			BufferedWriter leftBuffer,
			double limitTime,
			double limitFraction,
			double dt,
			double printDeltaT) throws IOException {
//		Particle p1 = particles.get(0);
//		Particle p2 = particles.get(1);
//		p1.setPosition(new Vector2D(100, 196));
//		p2.setPosition(new Vector2D(100, 194));
//		p1.setVelocity(new Vector2D(-7.596971994059741, -6.50276991146634));
//		p2.setVelocity(new Vector2D(-1.5045248019520396, -9.886172420118474));
//		List<Particle> test2particles = new ArrayList<>();
//		test2particles.add(p1);
//		test2particles.add(p2);
//		particles = test2particles;

		// Run Lennard-Jones Gas by default with StabilizedBoxCriteria end criteria
		Criteria endCriteria;
		if (limitTime < 0) {
			endCriteria = new StabilizedBoxCriteria(limitFraction, boxWidth / 2);
		} else {
			endCriteria = new TimeCriteria(limitTime);
		}

		// Print every 60 frames
		int currentFrame = 1;
		int printFrame = (int) Math.ceil(printDeltaT / dt);

		// Accumulate number of particles at left side of box for each time step
		AtomicReference<Integer> leftParticles = new AtomicReference<>(0);

		// Print first frame
		printFrame(buffer, energyBuffer, leftBuffer, particles.size(), limitTime, particles);

		while (!endCriteria.isDone(particles, time)) {
			time += dt;

			// Calculate neighbours
			CellIndexMethod.run(particles,
					Math.max(boxHeight, boxWidth),
					(int) Math.floor(Math.max(boxHeight, boxWidth) / interactionRadius),
					interactionRadius);

			// Add fake walls and calculate forces
			particles.stream().parallel().forEach(p -> {
				Set<Particle> neighboursCustom = new HashSet<>(p.getNeighbours());
				addFakeWallParticles(p, neighboursCustom);
				calculateForce(p, neighboursCustom);
			});

			// Only at first frame, initialize previous position of Verlet with Euler
			if (time == dt) {
				particles.forEach(p -> {
					if (time == dt) {
						Vector2D currentForce = p.getForce();
						double posX = p.getPosition().getX() - dt * p.getVelocity().getX();
						double posY = p.getPosition().getY() - dt * p.getVelocity().getY();
						posX += Math.pow(dt, 2) * currentForce.getX() / (2 * p.getMass());
						posY += Math.pow(dt, 2) * currentForce.getY() / (2 * p.getMass());

						particleIntegrationMethods.put(p,
								new VerletWithNeighbours(new Vector2D(posX, posY)));

						// Remove neighbours
						p.clearNeighbours();

						// Calculate left particles
						if (p.getPosition().getX() < boxWidth / 2)
							leftParticles.accumulateAndGet(1, (x, y) -> x + y);
					}
				});
			} else {
				// Update position
				particles.stream().parallel().forEach(p -> {
					moveParticle(p, dt);

					// Remove neighbours
					p.clearNeighbours();

					// Calculate left particles
					if (p.getPosition().getX() < boxWidth / 2)
						leftParticles.accumulateAndGet(1, (x, y) -> x + y);
				});
			}

			if ((currentFrame % printFrame) == 0)
				printFrame(buffer, energyBuffer, leftBuffer, leftParticles.get(), limitTime, particles);

			currentFrame++;
			leftParticles.set(0);
		}
	}

	/**
	 * Calcula la sumatoria de fuerzas sobre la particula
	 */
	private static void calculateForce(Particle particle, Set<Particle> neighbours) {
		AtomicReference<Double> potentialEnergy = new AtomicReference<>(0.0);

		// Particle force calculation
		Vector2D F = new Vector2D(0, 0);
		F = neighbours.stream().map(p2 -> {
			// Calculate distance between centers
			double distance = particle.getPosition().distance(p2.getPosition());

			// Calculate force module
			double fraction = RM / distance;
			double force = (12 * e / RM) * (Math.pow(fraction, 13) - Math.pow(fraction, 7));

			// Calculate x component of contact unit vector e
			double Enx = (p2.getPosition().getX() - particle.getPosition().getX()) / distance;

			// Calculate y component of contact unit vector e
			double Eny = (p2.getPosition().getY() - particle.getPosition().getY()) / distance;

			double Fx = -force * Enx;
			double Fy = -force * Eny;

			potentialEnergy.accumulateAndGet(calculatePotential(distance), (x, y) -> x + y);

			return new Vector2D(Fx, Fy);
		}).reduce(F, Vector2D::add);

		// Particle knows its force at THIS frame
		particle.setForce(F);

		// Set particle's potential energy
		particle.setPotentialEnergy(potentialEnergy.get());
	}

	/**
	 * Calcula el potencial entre dos particulas
	 *
	 * @param distanceBetweenParticles
	 * @return
	 */
	private static double calculatePotential(double distanceBetweenParticles) {
		double fraction = RM / distanceBetweenParticles;
		return e * (Math.pow(fraction, 12) - 2.0 * Math.pow(fraction, 6));
	}

	/**
	 * @param particle
	 * @return
	 */
	private static void moveParticle(Particle particle, double dt) {
		IntegrationMethodWithNeighbours integrationMethod = particleIntegrationMethods.get(particle);
		integrationMethod.updatePosition(particle, dt);
	}

	/**
	 * Agrega en neighbours set las particulas falsas necesarias
	 *
	 * @param particle
	 * @param neighbours
	 */
	private static void addFakeWallParticles(Particle particle, Set<Particle> neighbours) {
		double centralHoleLowerLimit = (boxHeight / 2) - (centralHoleUnits / 2);
		double centralHoleHigherLimit = (boxHeight / 2) + (centralHoleUnits / 2);

		int fakeId = -1;

		// Analyse left wall
		double distanceToLeftWall = particle.getPosition().getX();
		if (distanceToLeftWall <= interactionRadius) {
			// Add fake wall particle to its left at the box's left wall
			Particle leftWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			leftWallParticle.setPosition(new Vector2D(0.0, particle.getPosition().getY()));
			leftWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(leftWallParticle);
		}

		// Analyse right wall
		double distanceToRightWall = boxWidth - particle.getPosition().getX();
		if (distanceToRightWall <= interactionRadius) {
			// Add fake wall particle to its left at the box's right wall
			Particle rightWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			rightWallParticle.setPosition(new Vector2D(boxWidth, particle.getPosition().getY()));
			rightWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(rightWallParticle);
		}

		// Analyse up wall
		double distanceToTopWall = boxHeight - particle.getPosition().getY();
		if (distanceToTopWall <= interactionRadius) {
			Particle topWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			topWallParticle.setPosition(new Vector2D(particle.getPosition().getX(), boxHeight));
			topWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(topWallParticle);
		}

		// Analyse down wall
		double distanceToLowerWall = particle.getPosition().getY();
		if (distanceToLowerWall <= interactionRadius) {
			Particle lowerWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			lowerWallParticle.setPosition(new Vector2D(particle.getPosition().getX(), 0.0));
			lowerWallParticle.setVelocity(Vector2D.ZERO);
			neighbours.add(lowerWallParticle);
		}

		// Particle between ys of central hole
		if (particle.getPosition().getY() < centralHoleHigherLimit
				&& particle.getPosition().getY() > centralHoleLowerLimit) {
			Particle higherHoleWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			higherHoleWallParticle.setPosition(new Vector2D(boxWidth / 2, centralHoleHigherLimit));
			higherHoleWallParticle.setVelocity(Vector2D.ZERO);
			double particleDistance = particle.getPosition().distance(higherHoleWallParticle.getPosition());
			if (particleDistance <= interactionRadius) {
				neighbours.add(higherHoleWallParticle);
			}

			Particle lowerHoleWallParticle = new Particle(fakeId-- / 2, Double.POSITIVE_INFINITY);
			lowerHoleWallParticle.setPosition(new Vector2D(boxWidth / 2, centralHoleLowerLimit));
			lowerHoleWallParticle.setVelocity(Vector2D.ZERO);
			particleDistance = particle.getPosition().distance(lowerHoleWallParticle.getPosition());
			if (particleDistance <= interactionRadius) {
				neighbours.add(lowerHoleWallParticle);
			}
		} else {
			// Particle close to central line
			Particle middleWallParticle = new Particle(fakeId--, Double.POSITIVE_INFINITY);
			middleWallParticle.setPosition(new Vector2D(boxWidth / 2, particle.getPosition().getY()));
			middleWallParticle.setVelocity(Vector2D.ZERO);
			double particleDistance = particle.getPosition().distance(middleWallParticle.getPosition());
			if (particleDistance <= interactionRadius) {
				neighbours.add(middleWallParticle);
			}
		}
	}

	private static void printFrame(BufferedWriter buffer,
	                               BufferedWriter energyBuffer,
	                               BufferedWriter leftBuffer,
	                               Integer leftParticles,
	                               double limitTime,
	                               List<Particle> particles) throws IOException {
		buffer.write(String.valueOf(particles.size()));
		buffer.newLine();
		buffer.write("t=");
		buffer.write(String.valueOf(new DecimalFormat("#.#").format(time)));
		buffer.write("s");
		buffer.newLine();

		AtomicReference<Double> totalEnergy = new AtomicReference<>(0.0);

		// Print particles
		particles.parallelStream().forEach(particle -> {
			try {
				buffer.write(particleToString(particle));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			totalEnergy.accumulateAndGet(particle.getPotentialEnergy() + particle.getKineticEnergy(), (x, y) -> x + y);
		});

		// Print energies
		try {
			energyBuffer.write(time + " " + String.valueOf(totalEnergy.get()));
			energyBuffer.newLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Print left particles count and current time
		try {
			leftBuffer.write(time + " " + String.valueOf(leftParticles));
			leftBuffer.newLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (limitTime < 0) {
			System.out.println("Left particles: " + String.valueOf(leftParticles));
		} else {
			System.out.println("Current progress: " + String.valueOf(new DecimalFormat("#.#").format(100 * (time / limitTime))));
		}
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
