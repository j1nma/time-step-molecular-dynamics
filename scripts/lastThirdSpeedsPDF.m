function lastThirdSpeedsPDF(N, dt)

    disp(sprintf("./output/ex2/N=%d/ovito_file_dT=%s.txt", N, dt))
	data = load(sprintf("./output/ex2/N=%d/ovito_file_dT=%s.txt", N, dt));
	count = numel(data)
	dataMean = mean(data)
	maxSpeed = max(data)
	sprintf("Promedio del módulo de las velocidades (en t=0) = %5.3f ± %5.3f", dataMean, std(data))
	xRange = 0:0.002:maxSpeed; # el bin es importante ojo con que quede todo entre un par de velocidades

	figure(1)
	[nn, xx] = hist(data, xRange); %# Bin the data
	bar(xx, nn ./ (count * (xx(2) - xx(1))));
	xlabel("Módulo de velocidad (m/s)", 'fontsize', 16);
	ylabel("Densidad de probabilidad", 'fontsize', 16);
	set(gca, 'fontsize', 18);
	axis([0 maxSpeed])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-initial.png", './output/ex2', N), "-dpngcairo", "-F:12")

	data2 = load(sprintf("%s/N=%d/last_third_speeds.txt", './output/ex2', N));
	count2 = numel(data2)
	dataMean2 = mean(data2)
	maxSpeed2 = max(data2)
	sprintf("Promedio del módulo de las velocidades (en el último tercio) = %5.3f ± %5.3f", dataMean2, std(data2))
	xRange2 = 0:0.001:maxSpeed2;

	figure(2)
	[nn2, xx2] = hist(data2, xRange2); %# Bin the data
	bar(xx2, nn2);
	xlabel("Módulo de velocidad", 'fontsize', 16);
	ylabel("Densidad de probabilidad", 'fontsize', 16);
	set(gca, 'fontsize', 18);
	axis([0 maxSpeed2])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-last-thirds.png", './output/ex2', N), "-dpngcairo", "-F:12")

end