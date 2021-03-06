import os
import subprocess
import numpy
from numpy import arange
from oct2py import octave
import sys
octave.addpath('./scripts/')

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

graph =  t_or_f(sys.argv[1])

N = 1000
L = 200
initial_speed = 10

limitTime = 120.0

initial_dt = 0.0025

times = 6

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
	current_dt = format(initial_dt / (2**k), '.{f}f'.format(f = 4 + k));
	print(current_dt)
	if not graph:
		p = subprocess.Popen(['java', '-jar', './target/time-step-molecular-dynamics-1.0-SNAPSHOT.jar',
			'--lennardJonesGas=true',
			'--dynamicFile={dynamicFile}'.format(dynamicFile = "./data/lennardJonesGas/Dynamic-N={N}.txt".format(N = N)),
			'--staticFile={staticFile}'.format(staticFile = "./data/lennardJonesGas/Static-N={N}.txt".format(N = N)),
			'--deltaT={deltaT}'.format(deltaT = current_dt),
			'--limitTime={limitTime}'.format(limitTime = limitTime)]);
		processes.append(p);
	else:
		func = 'energy(' + str(N) + ',\"' + str(current_dt) + '\")';
		octave.eval(func);
if not graph:
    # Wait
    for process in processes:
        process.wait()
if graph:
 	func = 'energyMeanError(' + str(N) + ')';
 	octave.eval(func);