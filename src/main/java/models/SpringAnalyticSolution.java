package models;

public class SpringAnalyticSolution implements AnalyticSolution {

	@Override
	public double getPosition(double k, double gamma, double mass, double t) {
		return Math.exp(-(gamma / (2 * mass)) * t) * Math.cos(Math.sqrt((k / mass) - (gamma * gamma / (4 * mass * mass))) * t);
	}
}
