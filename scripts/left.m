function left(N, dt)
    disp(sprintf("./output/ex2/N=%d/left_dT=%s.txt", N, dt))
    fid = fopen(sprintf("./output/ex2/N=%d/left_dT=%s.txt", N, dt));

    t = [];
    left = [];

    % Read file
    while (!feof(fid))
        % Parse time-left
        timeLeft = fgetl(fid);
        [timeT leftT] = strsplit(timeLeft(1:end), " "){1,:};
        timeT
        t = [t, str2num(timeT)];
        left = [left, str2num(leftT)];
    endwhile

    fclose(fid);

    hold off

    props = {"marker", '.', 'LineStyle', 'none'};
    h = plot(t, left / N, sprintf(";dT = %s s;", dt));
    set(h, props{:})
    xlabel("Tiempo [s]");
    ylabel("Fracción de partículas en el recinto izquierdo");
    legend("location", "eastoutside");
    grid on

    print(sprintf("%s/N=%d/left-dT=%s.png", './output/ex2', N, dt), "-dpngcairo", "-F:12")
end