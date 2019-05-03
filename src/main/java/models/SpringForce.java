package models;

public class SpringForce implements Force {

	private final float k;
	private final float gamma;

	public SpringForce(float k, float gamma) {
		this.k = k;
		this.gamma = gamma;
	}

	@Override
	public float F(float x, float v) {
		return (-k * x - gamma * v);
	}
}
