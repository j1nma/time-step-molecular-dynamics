import algorithms.LennardJonesGas;
import com.google.devtools.common.options.OptionsParser;
import io.Parser;
import io.SimulationOptions;
import models.Particle;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class LennardJonesGasApp {

	private static final String OUTPUT_DIRECTORY = "./output";
	private static final String EX_2_DIRECTORY = OUTPUT_DIRECTORY + "/ex2";
	private static final String LEFT_PARTICLES_PLOT_FILE = EX_2_DIRECTORY + "/leftParticles.m";

	private static final String OVITO_FILE = OUTPUT_DIRECTORY + "/ovito_file.txt";
	private static PrintWriter eventWriter;

	public static void main(String[] args) {

		// Create output directories
		new File(OUTPUT_DIRECTORY).mkdirs();
		new File(EX_2_DIRECTORY).mkdirs();

		// Parse command line options
		OptionsParser parser = OptionsParser.newOptionsParser(SimulationOptions.class);
		parser.parseAndExitUponError(args);
		SimulationOptions options = parser.getOptions(SimulationOptions.class);
		assert options != null;
		if (options.limitTime <= 0
				|| options.deltaT <= 0
				|| options.printDeltaT <= 0
				|| options.mass <= 0
				|| !options.lennardJonesGas
				|| options.staticFile.isEmpty()
				|| options.dynamicFile.isEmpty()) {
			printUsage(parser);
		}

		// Parse static and dynamic files
		Parser staticAndDynamicParser = new Parser(options.staticFile, options.dynamicFile);
		if (!staticAndDynamicParser.parse()) return;
		List<Particle> particles = staticAndDynamicParser.getParticles();

		// Initialize file writers
//		eventWriter = new PrintWriter(new FileWriter("TODO GAS FILE")); //TODO CORRECT FILE NAME

		// Run algorithm
		runAlgorithm(
				particles,
				options.limitTime,
				options.deltaT,
				options.printDeltaT,
				options.k,
				options.vdc,
				options.initialPosition,
				options.mass
		);
	}

	private static void runAlgorithm(List<Particle> particles,
	                                 double limitTime,
	                                 double deltaT,
	                                 double printDeltaT,
	                                 double k,
	                                 double vdc,
	                                 double initialPosition,
	                                 double mass) {

		StringBuffer buffer = new StringBuffer();

		LennardJonesGas.run(
				particles,
				buffer,
				eventWriter,
				limitTime,
				deltaT,
				printDeltaT,
				k,
				vdc,
				initialPosition,
				mass,
				LEFT_PARTICLES_PLOT_FILE
		);

//		OvitoWriter<Particle> ovitoWriter;
//		try {
//			ovitoWriter = new OvitoWriter<>(Paths.get(OVITO_FILE));
//			ovitoWriter.writeBuffer(buffer);
//			ovitoWriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private static void printUsage(OptionsParser parser) {
		System.out.println("Usage: java -jar time-step-molecular-dynamics-1.0-SNAPSHOT.jar OPTIONS");
		System.out.println(parser.describeOptions(Collections.emptyMap(),
				OptionsParser.HelpVerbosity.LONG));
	}

}