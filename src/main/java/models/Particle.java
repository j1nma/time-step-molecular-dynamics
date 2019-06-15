package models;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class Particle implements Cloneable {

	private final int id;
	private final double mass;
	private double potentialEnergy;
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D force;
	private Set<Particle> neighbours;

	public Particle(int id, double mass) {
		this.id = id;
		this.mass = mass;
		this.neighbours = new HashSet<>();
	}

	public double getDistanceBetween(Particle particle) {
		Vector2D particlePosition = particle.getPosition();
		return Math.sqrt(Math.pow(position.getX() - particlePosition.getX(), 2)
				+ Math.pow(position.getY() - particlePosition.getY(), 2));
	}

	public void addNeighbour(Particle neighbour) {
		this.neighbours.add(neighbour);
	}

	public double getKineticEnergy() {
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

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public double getMass() {
		return mass;
	}

	public Set<Particle> getNeighbours() {
		return neighbours;
	}

	public void clearNeighbours() {
		this.neighbours = new HashSet<>();
	}

	public Vector2D getForce() {
		return force;
	}

	public void setForce(Vector2D force) {
		this.force = force;
	}

	public double getPotentialEnergy() {
		return potentialEnergy;
	}

	public void setPotentialEnergy(double potentialEnergy) {
		this.potentialEnergy = potentialEnergy;
	}
}
