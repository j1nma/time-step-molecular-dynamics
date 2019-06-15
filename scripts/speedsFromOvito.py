import math
import sys

def create_speed_file(data, verbose, fileName):
	output = []

	# Delete first N line
	del data[0]

	# Delete first t line
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
		line = "%s" % (v)
		output.append(line)
		if v > max_v:
		    max_v = v
		c = c + 1
		if (c % N == 0) and (c != len(data)):
			del data[c]
			del data[c]
		if verbose:
			print("Progress: {p}".format(p = "{:.2f}".format(100 * (c/len(data)))))
	print("Progress: {p}".format(p = "{:.2f}".format(100 * (c/len(data)))))
	f = open(fileName, "w")
	f.write("\n".join(output))
	f.close()
	print('max speed: {max_v}'.format(max_v = max_v))

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
data0 = fo.readlines()
N = int(data0[0])

initial_size = len(data0)

first_third_index = (initial_size*1 // 3)
second_third_index = (initial_size*2 // 3)
last_frame_index = initial_size - (N+2)

data = data0[first_third_index:first_third_index+N+2]
create_speed_file(data, verbose, "./output/ex2/N={N}/speeds_file_first_third_dT={dt}.txt".format(N = N, dt = dt))

data = data0[second_third_index:second_third_index+N+2]
create_speed_file(data, verbose, "./output/ex2/N={N}/speeds_file_second_third_dT={dt}.txt".format(N = N, dt = dt))

data = data0[last_frame_index:]
create_speed_file(data, verbose, "./output/ex2/N={N}/speeds_file_last_dT={dt}.txt".format(N = N, dt = dt))

fo.close()