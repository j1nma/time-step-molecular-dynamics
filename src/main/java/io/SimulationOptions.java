package io;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

/**
 * Command-line options definition for example server.
 */
public class SimulationOptions extends OptionsBase {

	@Option(
			name = "help",
			abbrev = 'h',
			help = "Prints usage info.",
			defaultValue = "false"
	)
	public boolean help;

	@Option(
			name = "staticFile",
			abbrev = 's',
			help = "Path to static file.",
			category = "startup",
			defaultValue = "/"
	)
	public String staticFile;

	@Option(
			name = "dynamicFile",
			abbrev = 'd',
			help = "Path to dynamic file.",
			category = "startup",
			defaultValue = "/"
	)
	public String dynamicFile;

	@Option(
			name = "limitTime",
			abbrev = 't',
			help = "Maximum time of simulation (s).",
			category = "startup",
			defaultValue = "5.0"
	)
	public double limitTime;

	@Option(
			name = "deltaT",
			abbrev = 'd',
			help = "Simulation delta time (s).",
			category = "startup",
			defaultValue = "0.1"
	)
	public double deltaT;

	@Option(
			name = "printDeltaT",
			abbrev = 's',
			help = "Simulation print delta time (s).",
			category = "startup",
			defaultValue = "0.5"
	)
	public double printDeltaT;

	@Option(
			name = "mass",
			abbrev = 'm',
			help = "Particle mass (kg).",
			category = "startup",
			defaultValue = "70"
	)
	public double mass;

	@Option(
			name = "stiffness",
			abbrev = 'k',
			help = "Stiffness (N/m).",
			category = "startup",
			defaultValue = "10000"
	)
	public double k;

	@Option(
			name = "vdc",
			abbrev = 'k',
			help = "Viscous damping coefficient (kg/s).",
			category = "startup",
			defaultValue = "100"
	)
	public double vdc;

	@Option(
			name = "initialPosition",
			abbrev = 'p',
			help = "Initial position at t=0 (m).",
			category = "startup",
			defaultValue = "1.0"
	)
	public double initialPosition;


	//TODO: WHAT ABOUT A in  - A 𝛾/(2m) ?
	@Option(
			name = "initialVelocity",
			abbrev = 'v',
			help = "Initial velocity at t=0 (m/s).",
			category = "startup",
			defaultValue = "1.0"
	)
	public double initialVelocity;

}
