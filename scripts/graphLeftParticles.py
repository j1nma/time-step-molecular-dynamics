import os
import subprocess
import numpy
from numpy import arange
from oct2py import octave
import sys

if len(sys.argv) != 2:
    sys.exit("Arguments missing. Exit.")

graph = sys.argv[1]

octave.addpath('./scripts/')

N = 1000
L = 200
initial_speed = 10

limitTime = 120.0
limitFraction = 0.56

dt = 0.00125

times = 1

if not graph:
	os.system('mvn clean package')

	# Generate random Dynamic and Static files. m = 0.1 kg. r = 1m.
	os.system('python3 ./scripts/generate.py {N} {L} {initial_speed}'.format(
	 		N = N,
	 		L = L,
	 		initial_speed = initial_speed
	 		));

processes = []

# For each dt value
for k in arange(0, times):
	if not graph:
		p = subprocess.Popen(['java', '-jar', './target/time-step-molecular-dynamics-1.0-SNAPSHOT.jar',
			'--lennardJonesGas=true',
			'--dynamicFile={dynamicFile}'.format(dynamicFile = "./data/lennardJonesGas/Dynamic-N={N}.txt".format(N = N)),
			'--staticFile={staticFile}'.format(staticFile = "./data/lennardJonesGas/Static-N={N}.txt".format(N = N)),
			'--deltaT={deltaT}'.format(deltaT = dt),
			'--limitTime={limitTime}'.format(limitTime = limitTime),
			'--limitFraction={limitFraction}'.format(limitFraction = limitFraction)]);
		processes.append(p);
	else:
		func = 'left(' + str(N) + ',\"' + str(dt) + '\")';
		octave.eval(func);
if not graph:
    # Wait
    for process in processes:
        process.wait()