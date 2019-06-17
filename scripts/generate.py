from functions import is_int_string, generate_lennard_jones_gas_file
import sys

if len(sys.argv) != 4:
    sys.exit("Arguments missing. Exit.")

number_of_particles = sys.argv[1]
area_length = sys.argv[2]
initial_speed = sys.argv[3]

if not is_int_string(number_of_particles):
    sys.exit("Must be integer. Exit.")

generate_lennard_jones_gas_file(int(number_of_particles),
float(area_length),
float(initial_speed),
float(1),
float(0.1))
