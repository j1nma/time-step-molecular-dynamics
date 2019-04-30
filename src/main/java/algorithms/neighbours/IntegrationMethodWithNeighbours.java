package algorithms.neighbours;

import models.Particle;

import java.util.Set;

public interface IntegrationMethodWithNeighbours {

	Particle updatePosition(Particle particle, Set<Particle> neighbours, double dt);
}
