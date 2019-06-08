import math
import sys

dt = sys.argv[1]

data = open("./output/ovito_file_dT={dt}.txt".format(dt = dt), "r").readlines()
N = int(data[0])

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
	if (c % N == 0) and (c != len(data)):
		output.append(data[c].rstrip())
		del data[c]
		output.append(data[c].rstrip())
		del data[c]
f = open("./output/color_ovito_file_dT={dt}.txt".format(dt = dt), "w")
f.write("\n".join(output))
f.close()