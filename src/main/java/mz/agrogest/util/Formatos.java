package mz.agrogest.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Formatacao de datas para mostrar nas paginas (dd/MM/aaaa). */
public final class Formatos {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Formatos() {
    }

    public static String data(LocalDate d) {
        return d == null ? "-" : d.format(DATA);
    }

    public static String dataHora(LocalDateTime d) {
        return d == null ? "-" : d.format(DATA_HORA);
    }

    /** Arredonda valores monetarios a 2 casas decimais. */
    public static double dinheiro(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
