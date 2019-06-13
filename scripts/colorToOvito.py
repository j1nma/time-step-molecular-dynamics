import math
import sys

N = sys.argv[1]
dt = sys.argv[2]

data0 = open("./output/ex2/N={N}/ovito_file_dT={dt}.txt".format(N = N, dt = dt), "r").readlines()
N = int(data0[0])

initial_size = len(data0)

print(initial_size)

cut = (initial_size*3//4)

print(cut)

i = 0
for line in data0[cut:]:
    line = line.rstrip()
    if (line == str(N)):
        break;
    i = i + 1

print(i)

print(data0[cut + i])

data = data0[cut+i:]

output = []

# Delete first N line
output.append(data[0].rstrip())
del data[0]

# Delete first t line
output.append(data[0].rstrip())
del data[0]

c = 0
for line in data:
	line = line.rstrip()
	xvs = line.split()
	vx = float(xvs[3])
	vy = float(xvs[4])
	v = math.sqrt(vx*vx + vy*vy)
	line = "%s %s" % (line, v)
	output.append(line)
	# output = output + line
	c = c + 1
	print("Progress: {p}".format(p = 100 * (c/(initial_size-cut))))
	if (c % N == 0) and (c != len(data)):
		output.append(data[c].rstrip())
		del data[c]
		output.append(data[c].rstrip())
		del data[c]
f = open("./output/ex2/N={N}/color_ovito_file_dT={dt}.txt".format(N = N, dt = dt), "w")
f.write("\n".join(output))
f.close()