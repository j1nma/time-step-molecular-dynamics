function speedsPDF(N, dt)

	bin_value = 0.15
    
    % First third distribution
    disp(sprintf("./output/ex2/N=%d/speeds_file_first_third_dT=%s.txt", N, dt))
	data = load(sprintf("./output/ex2/N=%d/speeds_file_first_third_dT=%s.txt", N, dt));
	count = numel(data)
	dataMean = mean(data)
	maxSpeed = max(data)
	sprintf("Promedio del módulo de las velocidades (en el primer tercio) = %5.3f ± %5.3f", dataMean, std(data))
	xRange = 0:bin_value:maxSpeed; % el bin es importante ojo con que quede todo entre un par de velocidades

	[nn, xx] = hist(data, xRange); % Bin the data
	bar(xx, nn ./ (count * (xx(2) - xx(1))));
	xlabel("Módulo de velocidad [m/s]");
	ylabel("Densidad de probabilidad");
	axis([0 maxSpeed])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-first-third.png", './output/ex2', N), "-dpngcairo", "-F:14")

	% Last distribution
	disp(sprintf("%s/N=%d/speeds_file_last_dT=%s.txt", './output/ex2', N, dt))
	data2 = load(sprintf("%s/N=%d/speeds_file_last_dT=%s.txt", './output/ex2', N, dt));
	count2 = numel(data2)
	dataMean2 = mean(data2)
	maxSpeed2 = max(data2)
	sprintf("Promedio del módulo de las velocidades (en el último tercio) = %5.3f ± %5.3f", dataMean2, std(data2))
	xRange2 = 0:bin_value:maxSpeed2;

	[nn2, xx2] = hist(data2, xRange2); % Bin the data
	% bar(xx2, nn2);
	bar(xx2, nn2 ./ (count2 * (xx2(2) - xx2(1))));
	xlabel("Módulo de velocidad [m/s]");
	ylabel("Densidad de probabilidad");
	axis([0 maxSpeed2])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-last.png", './output/ex2', N), "-dpngcairo", "-F:14")

	% Second third distribution
	disp(sprintf("%s/N=%d/speeds_file_second_third_dT=%s.txt", './output/ex2', N, dt))
	data3 = load(sprintf("%s/N=%d/speeds_file_second_third_dT=%s.txt", './output/ex2', N, dt));
	count3 = numel(data3)
	dataMean3 = mean(data3)
	maxSpeed3 = max(data3)
	sprintf("Promedio del módulo de las velocidades (en el segundo tercio) = %5.3f ± %5.3f", dataMean3, std(data3))
	xRange3 = 0:bin_value:maxSpeed3;

	[nn3, xx3] = hist(data3, xRange3); % Bin the data
	% bar(xx3, nn3);
	bar(xx3, nn3 ./ (count3 * (xx3(2) - xx3(1))));
	xlabel("Módulo de velocidad [m/s]");
	ylabel("Densidad de probabilidad");
	axis([0 maxSpeed3])
	grid on
	print(sprintf("%s/N=%d/PDF-speeds-second-third.png", './output/ex2', N), "-dpngcairo", "-F:14")
end