import os
import subprocess
import numpy
from oct2py import octave
octave.addpath('./scripts/')

N = 1000
dt = 0.00125
func = 'speedsPDF(' + str(N) + ',\"' + str(dt) + '\")';
octave.eval(func);