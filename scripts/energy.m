function energy(N, dt)
    disp(sprintf("./output/ex2/N=%d/energy_dT=%s.txt", N, dt))
    fid = fopen(sprintf("./output/ex2/N=%d/energy_dT=%s.txt", N, dt));

    t = [];
    energy = [];

    % Read file
    while (!feof(fid))
        % Parse time-energy
        timeEnergy = fgetl(fid);
        [timeT energyT] = strsplit(timeEnergy(1:end), " "){1,:};
        t = [t, str2num(timeT)];
        energy = [energy, str2num(energyT)];
    endwhile

    fclose(fid);

    props = {"marker", '.', 'LineStyle', 'none'};
    h = plot(t, energy / energy(1), sprintf(";dT = %s s;", dt));
    %set(h, props{:})
    xlabel("Tiempo [s]");
    %xlim([0, 5])
    ylabel("Energía Total_{t} / Energía_{0} [J]");
    legend("location", "eastoutside");
    grid on

    disp(sprintf("mean: %f", mean(energy / energy(1))))
    disp(sprintf("std: %f", std(energy / energy(1))))

    hold all

    print(sprintf("%s/N=%d/energy-dT=%s.png", './output/ex2', N, dt), "-dpngcairo", "-F:12")
end