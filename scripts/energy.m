function energy(N, dt)
    disp(sprintf("./output/ex2/N=%d/energy_dT=%s.txt", N, dt))
    fid = fopen(sprintf("./output/ex2/N=%d/energy_dT=%s.txt", N, dt));

    energy = [];
    # Read file
    while (!feof(fid))
        % Parse out energy
        energy = [energy, str2num(fgetl(fid))];
    endwhile

    fclose(fid);

    print_dt = 0.016666667;

    props = {"marker", '.', 'LineStyle', 'none'};
    h = plot((1:size(energy, 2)) * print_dt, energy / energy(1), sprintf(";dT = %s s;", dt));
    %set(h, props{:})
    xlabel("Tiempo [s]");
    ylabel("Energía Total_{t} / Energía_{0} [J]");
    %xlim([0, 5])
    legend("location", "eastoutside");
    grid on

    mean(energy / energy(1))
    std(energy / energy(1))

    hold all

    print(sprintf("%s/N=%d/energy-dT=%s.png", './output/ex2', N, dt), "-dpngcairo", "-F:12")
end