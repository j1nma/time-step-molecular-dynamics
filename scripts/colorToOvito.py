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
ratio_N = int(sys.argv[3])
ratio_D = int(sys.argv[4])
verbose = t_or_f(sys.argv[5])
speedFile = t_or_f(sys.argv[6])
ratioAsUpperLimit = t_or_f(sys.argv[7])

data0 = open("./output/ex2/N={N}/ovito_file_dT={dt}.txt".format(N = N, dt = dt), "r").readlines()
N = int(data0[0])

initial_size = len(data0)

cut = (initial_size*ratio_N // ratio_D)

i = 0
if not ratioAsUpperLimit:
	if cut != 0:
	    for line in data0[cut:]:
	       line = line.rstrip()
	       if (line == str(N)):
	           break;
	       i = i + 1

	data = data0[cut+i:]
else:
	i = cut
	if cut != 0:
		# Only for ratioAsUpperLimit upto 0.9 or so
	    for line in data0[cut:cut+N]:
	       line = line.rstrip()
	       if (line == str(N)):
	           break;
	       i = i + 1

	data = data0[:i]

if verbose == 1:
	print('initial_size: {initial_size}'.format(initial_size = initial_size))
	print('cut index: {cut}'.format(cut = cut))
	if not ratioAsUpperLimit:
		print('real cut index: {cut}'.format(cut = cut+i))
	else:
		print('real cut index: {cut}'.format(cut = i))

output = []

# Delete first N line
if not speedFile:
	output.append(data[0].rstrip())
del data[0]

# Delete first t line
if not speedFile:
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
	if not speedFile:
		line = "%s %s" % (line, v)
	else:
		line = "%s" % (v)
	output.append(line)
	if v > max_v:
	    max_v = v
	c = c + 1
	if (c % N == 0) and (c != len(data)):
		if not speedFile:
			output.append(data[c].rstrip())
		del data[c]
		if not speedFile:
			output.append(data[c].rstrip())
		del data[c]
	# if verbose == 1 and (c % N == 0):
	    #print("Progress: {p}".format(p = 100 * (c/len(data))))
if speedFile:
	if not ratioAsUpperLimit:
		f = open("./output/ex2/N={N}/speeds_file_r={ratio}_dT={dt}.txt".format(N = N, ratio = "{:.2f}".format(ratio_N/ratio_D), dt = dt), "w")
	else:
		f = open("./output/ex2/N={N}/initial_speeds_file_r={ratio}_dT={dt}.txt".format(N = N, ratio = "{:.2f}".format(ratio_N/ratio_D), dt = dt), "w")
else:
	f = open("./output/ex2/N={N}/color_ovito_file_dT={dt}.txt".format(N = N, dt = dt), "w")
f.write("\n".join(output))
f.close()
print('max speed: {max_v}'.format(max_v = max_v))