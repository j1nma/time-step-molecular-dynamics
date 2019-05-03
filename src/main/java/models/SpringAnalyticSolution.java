package models;

public class SpringAnalyticSolution implements AnalyticSolution {

	@Override
	public float getPosition(float k, float gamma, float mass, float t) {
		return (float) (Math.exp(-(gamma / (2 * mass)) * t) * Math.cos(Math.sqrt((k / mass) - (gamma * gamma / (4 * mass * mass))) * t));
	}
}
