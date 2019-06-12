function energy(N, dt)
    disp(sprintf("./output/ex2/N=%d/energy_dT=%s.txt", N, dt))
    fid = fopen(sprintf("./output/ex2/N=%d/energy_dT=%s.txt", N, dt));

    energy = [0.0];
    # Read file
    while (!feof(fid))
            % Parse out energy
            energy = [energy, str2num(fgetl(fid))];
    endwhile

    fclose(fid);

    props = {"marker", '.', 'LineStyle', 'none'};
    h = plot((1:size(energy, 2)), energy, sprintf(";dT = %s;", dt));
    set(h, props{:})
    xlabel("something");
    ylabel("Energía");
    legend("location", "eastoutside");
    grid on

    print(sprintf("%s/N=%d/energy-dT=%s.png", './output/ex2', N, dt), "-dpngcairo", "-F:12")
end