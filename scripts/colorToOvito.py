import math
import sys

N = sys.argv[1]
dt = sys.argv[2]
ratio_N = int(sys.argv[3])
ratio_D = int(sys.argv[4])
verbose = int(sys.argv[5])

data0 = open("./output/ex2/N={N}/ovito_file_dT={dt}.txt".format(N = N, dt = dt), "r").readlines()
N = int(data0[0])

initial_size = len(data0)

cut = (initial_size*ratio_N // ratio_D)

i = 0
if cut != 0:
    for line in data0[cut:]:
       line = line.rstrip()
       if (line == str(N)):
           break;
       i = i + 1

if verbose == 1:
    print('initial_size: {initial_size}'.format(initial_size = initial_size))
    print('cut index: {cut}'.format(cut = cut))
    print('real cut index: {cut}'.format(cut = cut+i))

data = data0[cut+i:]

output = []

# Delete first N line
output.append(data[0].rstrip())
del data[0]

# Delete first t line
output.append(data[0].rstrip())
del data[0]

max_v = 0.0

c = 0
for line in data:
	line = line.rstrip()
	xvs = line.split()
	vx = float(xvs[3])
	vy = float(xvs[4])
	v = math.sqrt(vx*vx + vy*vy)
	line = "%s %s" % (line, v)
	output.append(line)
	if v > max_v:
	    max_v = v
	c = c + 1
	if (c % N == 0) and (c != len(data)):
	    output.append(data[c].rstrip())
	    del data[c]
	    output.append(data[c].rstrip())
	    del data[c]
	if verbose == 1 and (c % N == 0):
	    print("Progress: {p}".format(p = 100 * (c/len(data))))
f = open("./output/ex2/N={N}/color_ovito_file_dT={dt}.txt".format(N = N, dt = dt), "w")
f.write("\n".join(output))
f.close()
print('max speed: {max_v}'.format(max_v = max_v))