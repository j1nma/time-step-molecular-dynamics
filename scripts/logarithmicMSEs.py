import os
import subprocess
import csv
import numpy
from numpy import vstack, zeros, log
import matplotlib.pyplot as plt

printDeltaT = 0.001

dirName = './output';
ex1DirName = dirName + '/ex1';

if not os.path.exists(ex1DirName):
        os.mkdir(ex1DirName)
        print("Directory ", ex1DirName, " created.")

xRange = range(-6, 0, 1)

mse_size = numpy.size(xRange)

beeman_mse = zeros(mse_size);
verlet_mse = zeros(mse_size);
gear_mse = zeros(mse_size);

k = 0;

# Generate a file with set of parameters
for e in xRange:
	print(10**e)
	command = 'java -jar ./target/time-step-molecular-dynamics-1.0-SNAPSHOT.jar --deltaT={deltaT} --printDeltaT={printDeltaT}'.format(
						deltaT = 10**e,
						printDeltaT = printDeltaT,
						)
	print(command)
	p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=0)
	number = None;
	line = p.stdout.readline(); # Beeman line
	number = line.decode()
	number = number.split('\t')
	number = number[1]
	number = number.replace(' [m^2]\n', '')
	beeman_mse[k] = float(number)
	line = p.stdout.readline(); # VelocityVerlet line
	number = line.decode()
	number = number.split('\t')
	number = number[1]
	number = number.replace(' [m^2]\n', '')
	verlet_mse[k] = float(number)
	line = p.stdout.readline(); # Order5GearPredictorCorrector line
	print(line)
	number = line.decode()
	number = number.split('\t')
	number = number[1]
	number = number.replace(' [m^2]\n', '')
	gear_mse[k] = float(number)
	print(gear_mse[k])
	k = k + 1

# Prepare MSE plot
f, ax = plt.subplots(1)

# Plot data
plt.plot(xRange, numpy.log10(beeman_mse), linestyle='None', marker='o')
plt.plot(xRange, numpy.log10(verlet_mse), linestyle='None', marker='o')
plt.plot(xRange, numpy.log10(gear_mse), linestyle='None', marker='o')
plt.legend(['Beeman', 'Velocity Verlet', 'Order 5 Gear PC'], loc=2)
ax.grid()
plt.xlabel("log$_{10}$(Δt) [s]")
plt.ylabel("log$_{10}$(MSE) [$m^2$]")
plt.xticks(xRange)
plt.ylim([-30, 30])
plt.yticks(numpy.arange(-30, 30, 5))
# Save plot
plt.savefig('./output/ex1/logarithmicMSEs.svg')