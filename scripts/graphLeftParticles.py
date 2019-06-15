import os
import subprocess
import numpy
from numpy import arange
from oct2py import octave
import sys

def t_or_f(arg):
    ua = str(arg).upper()
    if 'TRUE'.startswith(ua):
       return True
    elif 'FALSE'.startswith(ua):
       return False
    else:
       pass

if len(sys.argv) != 2:
    sys.exit("Arguments missing. Exit.")

graph = t_or_f(sys.argv[1])

octave.addpath('./scripts/')

N = 1000
L = 200
initial_speed = 10

limitTime = -1
limitFraction = 0.5

dt = 0.00125
print_dt = 1.0

processes = []

if not graph:
	os.system('mvn clean package')

	print('Generating N={N} L={L} initial_speed={initial_speed}...'.format(
        N = N,
        L = L,
        initial_speed = initial_speed))

	# Generate random Dynamic and Static files. m = 0.1 kg. r = 1m.
	os.system('python3 ./scripts/generate.py {N} {L} {initial_speed}'.format(
	 		N = N,
	 		L = L,
	 		initial_speed = initial_speed
	 		));

	p = subprocess.Popen(['java', '-jar', './target/time-step-molecular-dynamics-1.0-SNAPSHOT.jar',
		'--lennardJonesGas=true',
		'--dynamicFile={dynamicFile}'.format(dynamicFile = "./data/lennardJonesGas/Dynamic-N={N}.txt".format(N = N)),
		'--staticFile={staticFile}'.format(staticFile = "./data/lennardJonesGas/Static-N={N}.txt".format(N = N)),
		'--deltaT={deltaT}'.format(deltaT = dt),
		'--printDeltaT={printDeltaT}'.format(printDeltaT = print_dt),
		'--limitTime={limitTime}'.format(limitTime = limitTime),
		'--limitFraction={limitFraction}'.format(limitFraction = limitFraction)]);
	processes.append(p);

	for process in processes:
		process.wait()
else:
	func = 'left(' + str(N) + ',\"' + str(dt) + '\")';
	octave.eval(func);