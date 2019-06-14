function speedsPDF(N, dt, ratio, initial_ratio)
    disp(sprintf("./output/ex2/N=%d/initial_speeds_file_r=%s_dT=%s.txt", N, initial_ratio, dt))
	data = load(sprintf("./output/ex2/N=%d/initial_speeds_file_r=%s_dT=%s.txt", N, initial_ratio, dt));
	count = numel(data)
	dataMean = mean(data)
	maxSpeed = max(data)
	sprintf("Promedio del módulo de las velocidades (en t=0) = %5.3f ± %5.3f", dataMean, std(data))
	xRange = 0:0.2:maxSpeed; % el bin es importante ojo con que quede todo entre un par de velocidades

	[nn, xx] = hist(data, xRange); % Bin the data
	bar(xx, nn ./ (count * (xx(2) - xx(1))));
	xlabel("Módulo de velocidad [m/s]");
	ylabel("Densidad de probabilidad");
	axis([0 maxSpeed])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-initial.png", './output/ex2', N), "-dpngcairo", "-F:14")

	disp(sprintf("%s/N=%d/speeds_file_r=%s_dT=%s.txt", './output/ex2', N, ratio, dt))
	data2 = load(sprintf("%s/N=%d/speeds_file_r=%s_dT=%s.txt", './output/ex2', N, ratio, dt));
	count2 = numel(data2)
	dataMean2 = mean(data2)
	maxSpeed2 = max(data2)
	sprintf("Promedio del módulo de las velocidades (en el último tercio) = %5.3f ± %5.3f", dataMean2, std(data2))
	xRange2 = 0:0.2:maxSpeed2;

	[nn2, xx2] = hist(data2, xRange2); % Bin the data
	bar(xx2, nn2);
	xlabel("Módulo de velocidad [m/s]");
	ylabel("Densidad de probabilidad");
	axis([0 maxSpeed2])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-last-thirds.png", './output/ex2', N), "-dpngcairo", "-F:14")
end