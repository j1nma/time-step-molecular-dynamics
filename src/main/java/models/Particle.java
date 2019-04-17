package models;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class Particle implements Cloneable {

	private final int id;
	private Vector2D position;
	private Vector2D velocity;
	private double radius;
	private double mass;
	private Set<Particle> neighbours;

	public Particle(int id, double radius, double mass) {
		this.id = id;
		this.radius = radius;
		this.mass = mass;
		this.neighbours = new HashSet<>();
	}

	/**
	 * The distance between points contemplates border-to-border distance.
	 * That is why the radii are subtracted.
	 */
	public double getDistanceBetween(Particle particle) {
		Vector2D particlePosition = particle.getPosition();
		return Math.sqrt(Math.pow(position.getX() - particlePosition.getX(), 2) +
				Math.pow(position.getY() - particlePosition.getY(), 2))
				- radius - particle.getRadius();
	}

	public void addNeighbour(Particle neighbour) {
		this.neighbours.add(neighbour);
	}

	private double getKineticEnergy() {
		return 0.5 * mass * velocity.getNormSq();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Particle particle = (Particle) o;
		return id == particle.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("###.0000000000");
		return df.format(position.getX()) + " "
				+ df.format(position.getY()) + " "
				+ df.format(velocity.getX()) + " "
				+ df.format(velocity.getY()) + " "
				+ mass + " "
				+ radius
				+ df.format(getKineticEnergy()) + " ";
	}

	public int getId() {
		return id;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public double getSpeed() {
		return velocity.getNorm();
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public double getRadius() {
		return radius;
	}

	public double getMass() {
		return mass;
	}

	public Set<Particle> getNeighbours() {
		return neighbours;
	}
}
