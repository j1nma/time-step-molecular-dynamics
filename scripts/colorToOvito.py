import math
import sys

def t_or_f(arg):
    ua = str(arg).upper()
    if 'TRUE'.startswith(ua):
       return True
    elif 'FALSE'.startswith(ua):
       return False
    else:
       pass

N = sys.argv[1]
dt = sys.argv[2]
verbose = t_or_f(sys.argv[3])

print("Opening file... ./output/ex2/N={N}/ovito_file_dT={dt}.txt".format(N = N, dt = dt))
fo = open("./output/ex2/N={N}/ovito_file_dT={dt}.txt".format(N = N, dt = dt), "r")
data = fo.readlines()
N = int(data[0])

initial_size = len(data)

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
	if(len(xvs) != 5):
		break
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
	if verbose and (c % N == 0):
	    print("Progress: {p}".format(p = "{:.2f}".format(100 * (c/len(data)))))
print("Progress: {p}".format(p = "{:.2f}".format(100 * (c/len(data)))))
f = open("./output/ex2/N={N}/color_ovito_file_dT={dt}.txt".format(N = N, dt = dt), "w")
f.write("\n".join(output))
f.close()
fo.close()
print('max speed: {max_v}'.format(max_v = max_v))