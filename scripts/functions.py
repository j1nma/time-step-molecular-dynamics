from numpy import random, pi, power, concatenate, sqrt, cos, sin, empty
import sys
import os
import fileinput

def is_valid_position(otherX, otherY, otherRadius, newX, newY, newRadius):
    return (power(otherX - newX, 2) + pow(otherY - newY, 2)) > pow(otherRadius + newRadius, 2)

def generate_static_file(name, number_of_particles, area_length, particle_radius, particle_mass, particle_bonding_energy):
    with open(name, 'w') as f:
        f.write('{}\n'.format(number_of_particles))

        for x in range(0, number_of_particles):
            f.write('{} {} {}\n'.format(particle_radius, particle_mass, particle_bonding_energy))

def generate_dynamic_file(name, number_of_particles, area_length, initial_speed, particle_radius):
    with open(name, 'w') as f:
        f.write('{}\n'.format(number_of_particles))

        # Generate particles
        particles = empty((0,3), float)
        for i in range(0, number_of_particles):
            validPosition = False
            x = 0
            y = 0
            while not validPosition:
                x = random.uniform() * (area_length - 2*particle_radius) + particle_radius
                y = random.uniform() * (area_length - 2*particle_radius) + particle_radius
                j = 0
                validPosition = True
                while j < len(particles) and validPosition:
                    validPosition = is_valid_position(particles[j][0], particles[j][1], particles[j][2], x, y, particle_radius)
                    j = j + 1

            angle = random.uniform() * 2 * pi
            vx = cos(angle) * initial_speed
            vy = sin(angle) * initial_speed
            particles = concatenate((particles, [[x, y, particle_radius]]), axis=0)
            f.write('{}\t{}\t{}\t{}\t{}\n'.format(i + 1, x, y, vx, vy))

def generate_files(number_of_small_particles, area_length, max_velocity_module, particle_radius, particle_mass, large_particle_radius, large_particle_mass):
    dirName = './data';
    if not os.path.exists(dirName):
            os.mkdir(dirName)
            print("Directory " , dirName ,  " Created ")
    generate_static_file(dirName + '/Static-N=' + str(number_of_small_particles) + '.txt', number_of_small_particles, area_length, particle_radius, particle_mass, large_particle_radius, large_particle_mass)
    generate_dynamic_file(dirName + '/Dynamic-N=' + str(number_of_small_particles) + '.txt', number_of_small_particles, area_length, max_velocity_module, particle_radius, large_particle_radius)

def is_int_string(s):
    try:
        int(s)
        return True
    except ValueError:
        return False

def get_radius():
    while True:
        try:
            return float(input("Enter particle radius r: "))
        except ValueError:
            print("Number not a float.")

def get_area_length():
    while True:
        try:
            return float(input("Enter area length L: "))
        except ValueError:
            print("Number not a float.")

def generate_lennard_jones_gas_file(number_of_particles, area_length, initial_speed, particle_radius, particle_mass, particle_bonding_energy):
    dirName = './data';
    if not os.path.exists(dirName):
                os.mkdir(dirName)
                print("Directory " , dirName ,  " Created ")
    lennardJonesGasDirName = dirName + '/lennardJonesGas';
    if not os.path.exists(lennardJonesGasDirName):
                    os.mkdir(lennardJonesGasDirName)
                    print("Directory " , lennardJonesGasdirName ,  " Created ")
    generate_static_file(lennardJonesGasDirName + '/Static-N=' + str(number_of_particles) + '.txt', number_of_particles, area_length, particle_radius, particle_mass, particle_bonding_energy)
    generate_dynamic_file(lennardJonesGasDirName + '/Dynamic-N=' + str(number_of_particles) + '.txt', number_of_particles, area_length, initial_speed, particle_radius)


