from functions import is_int_string, generate_lennard_jones_gas_file
import sys

if len(sys.argv) != 7:
    sys.exit("Arguments missing. Exit.")

number_of_particles = sys.argv[1]
area_length = sys.argv[2]
initial_speed = sys.argv[3]
particle_radius = sys.argv[4]
particle_mass = sys.argv[5]
particle_bonding_energy = sys.argv[6]

if not is_int_string(number_of_particles):
    sys.exit("Must be integer. Exit.")

generate_lennard_jones_gas_file(int(number_of_particles),
float(area_length),
float(initial_speed),
float(particle_radius),
float(particle_mass),
float(particle_bonding_energy))
