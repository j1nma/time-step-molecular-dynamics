import os
import subprocess
import numpy
from oct2py import octave
octave.addpath('./scripts/')

N = 1000
dt = "0.00125"
ratio = "0.75"
initial_ratio = "0.33"
func = 'speedsPDF(' + str(N) + ',\"' + str(dt) + '\",\"' + str(ratio) + '\",\"' + str(initial_ratio) + '\")';
octave.eval(func);