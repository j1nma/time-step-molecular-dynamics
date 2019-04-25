package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Stack;

public class OctaveWriter {

	private final FileWriter fileWriter;

	private OctaveWriter(File file) throws IOException {
		Optional<File> containingDir = Optional.ofNullable(file.getParentFile());
		if (containingDir.map(dir -> !dir.exists() && !dir.mkdirs()).orElse(false)) {
			throw new IllegalStateException("Could not create dir: " + containingDir);
		}
		this.fileWriter = new FileWriter(file);
	}

	public OctaveWriter(Path path) throws IOException {
		this(path.toFile());
	}

	public void close() throws IOException {
		this.fileWriter.close();
	}

	public void writePositionsThroughTime(final Stack<Double> timeStepValues,
	                                      final Stack<Double> analyticValues,
	                                      final Stack<Double> positionsValues) throws IOException { //TODO adapt
		final StringBuilder builder = new StringBuilder();
		builder.append("x = [").append(Double.toString(timeStepValues.pop()));
		while (!timeStepValues.isEmpty()) {
			builder.append(", ").append(Double.toString(timeStepValues.pop()));
		}
		builder.append("];").append("\n")
				.append("y1 = [").append(Double.toString(analyticValues.pop()));
		while (!analyticValues.isEmpty()) {
			builder.append(", ").append(Double.toString(analyticValues.pop()));
		}
		builder.append("];").append("\n")
				.append("y2 = [").append(Double.toString(positionsValues.pop()));
		while (!positionsValues.isEmpty()) {
			builder.append(", ").append(Double.toString(positionsValues.pop()));
		}
		builder.append("];").append("\n")
				.append("plot(x, y1, \";Analítica;\");").append("\n")
				.append("xlabel(\"Tiempo (s)\");").append("\n")
				.append("ylabel(\"Posición (m)\");").append("\n")
				.append("set(gca, 'ytick', -1:0.2:1);").append("\n")
				.append("hold all").append("\n")
				.append("plot(x, y2, \";Beeman;\", \"color\", 'g','LineStyle','--');").append("\n")
				.append("print(\"./output/beeman/beeman.svg\", \"-dsvg\", \"-F:12\")").append("\n")
		;

		fileWriter.append(builder.toString());
		fileWriter.flush();
	}
}
