import os
import subprocess
import csv
import numpy
import matplotlib.pyplot as plt
import math
from numpy import vstack
from numpy import zeros
from oct2py import octave
octave.addpath('./scripts/')

N = 100
dT = "1.0E-3"
dT = "0.0025"
func = 'energy(' + str(N) + ',\"' + dT + '\")';
octave.eval(func);