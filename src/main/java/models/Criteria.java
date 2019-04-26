package models;

import java.util.List;

public interface Criteria {

	boolean test(final List<Particle> particles, final double time);
}