function energyMeanError(N)
    means = [0.98663, 0.980553, 0.981286, 0.980930, 0.981292, 0.981050, 0.981507];

    stds = [0.0057712, 0.002735, 0.002528, 0.002503, 0.002497, 0.002468, 0.002531];

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