package algorithms;

public class SpringForce implements Force {

	private final double k;
	private final double gamma;

	public SpringForce(double k, double gamma) {
		this.k = k;
		this.gamma = gamma;
	}

	@Override
	public double F(double x, double v) {
		return (-k * x - gamma * v);
	}
}
