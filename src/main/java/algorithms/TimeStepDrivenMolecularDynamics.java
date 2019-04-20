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

		System.out.println("Generar en java todas las particulas. no hace falta hacerlo en python. Recibir por" +
				"parametro la cantidad y las dimensiones del lugar");

		System.out.println("hacer el while y que en cada frame se calcule la posicion y velocidad de todas respecto de otras");

		System.out.println("Es decir por cada particula calcular la sumatoria de todas las fuerzas de todas las particulas alrededor que le generan a esta.");

		System.out.println("Para eso ultimo mirar verlet basico y usar ese.");

		System.out.println("recordar que esto es por tiempo. no por eventos");

		System.out.println("no c como manejar el caso de cuando choca contra la esquina cuando cambia de box....");
		
	}
}
