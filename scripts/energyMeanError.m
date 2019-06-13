function energyMeanError(N)
    means = [0.98663, 0.98339, 0.98029, 0.97999, 0.98166, 0.98180, 0.98136];

    stds = [0.0057712, 0.0029841, 0.0025027, 0.0026020, 0.0025134, 0.0024872, 0.0025348];

    dts = [];

    initial_dt = 0.0025;

    times = 6;

    dts = [0.003];

    for k = 0:1:times-1
        dts = [dts, initial_dt / (2**k)];
    endfor

    hold off

    props = {"marker", '.', 'LineStyle', 'none'};
    h = errorbar(dts, means, stds);
    set(h, props{:})
    xlabel("dt [s]");
    ylabel("Energía Total_{t} / Energía_{0} [J]");
    %xlim([95, 96])
    grid on

    print(sprintf("%s/N=%d/mean-energy.png", './output/ex2', N), "-dpngcairo", "-F:12")
end