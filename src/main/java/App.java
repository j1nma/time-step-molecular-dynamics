import algorithms.TimeStepMolecularDynamics;
import com.google.devtools.common.options.OptionsParser;
import io.OvitoWriter;
import io.SimulationOptions;
import models.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;

public class App {

	private static final String OUTPUT_DIRECTORY = "./output";
	private static final String OVITO_FILE = OUTPUT_DIRECTORY + "/ovito_file.txt";
	private static final String COLLISION_DIRECTORY = OUTPUT_DIRECTORY + "/collisionFrequency";
	private static final String COLLISION_FREQUENCY_FILE = COLLISION_DIRECTORY + "/collision_frequency.txt";
	private static PrintWriter eventWriter;

	public static void main(String[] args) throws IOException {

		// Create directories
		new File(OUTPUT_DIRECTORY).mkdirs();
		new File(COLLISION_DIRECTORY).mkdirs();

		// Parse command line options
		OptionsParser parser = OptionsParser.newOptionsParser(SimulationOptions.class);
		parser.parseAndExitUponError(args);
		SimulationOptions options = parser.getOptions(SimulationOptions.class);
		assert options != null;
		if (options.limitTime <= 0
				|| options.deltaT <= 0
				|| options.printDeltaT <= 0
				|| options.mass <= 0) {
			printUsage(parser);
		}

		// Initialize file writers
		eventWriter = new PrintWriter(new FileWriter(COLLISION_FREQUENCY_FILE));

		// Run algorithm
		runAlgorithm(
				options.limitTime,
				options.deltaT,
				options.printDeltaT,
				options.k,
				options.vdc,
				options.initialPosition,
				options.initialVelocity
		);
	}

	private static void runAlgorithm(double limitTime,
	                                 double deltaT,
	                                 double printDeltaT,
	                                 double k,
	                                 double vdc,
	                                 double initialPosition,
	                                 double initialVelocity) {

		StringBuffer buffer = new StringBuffer();
		long startTime = System.currentTimeMillis();

		// Print temperature (constant over time)
//		System.out.println("Temperature (K):\t" + TimeStepMolecularDynamics.calculateTemperature(particles));

//		TimeStepMolecularDynamics.run(
//				particles,
//				L,
//				limitTime,
//				maxEvents,
//				buffer,
//				eventWriter
//		);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println("======================== Results ========================");
		System.out.println("Event Driven Molecular Dynamics execution limitTime (ms):\t" + elapsedTime);

		OvitoWriter<Particle> ovitoWriter;
		try {
			ovitoWriter = new OvitoWriter<>(Paths.get(OVITO_FILE));
			ovitoWriter.writeBuffer(buffer);
			ovitoWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Average time between collisions (s):\t" +
				TimeStepMolecularDynamics.getAverageTimeBetweenCollisions());
	}

	private static void printUsage(OptionsParser parser) {
		System.out.println("Usage: java -jar molecular-dynamics-simulation-1.0-SNAPSHOT.jar OPTIONS");
		System.out.println(parser.describeOptions(Collections.emptyMap(),
				OptionsParser.HelpVerbosity.LONG));
	}

}
