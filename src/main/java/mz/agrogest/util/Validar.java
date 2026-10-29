package mz.agrogest.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import mz.agrogest.service.ValidacaoException;

/** Pequenas validacoes de campos de formulario. */
public final class Validar {

    private Validar() {
    }

    public static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("O campo \"" + campo + "\" é obrigatório.");
        }
        return valor.trim();
    }

    public static String opcional(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public static int inteiro(String valor, String campo) {
        try {
            return Integer.parseInt(texto(valor, campo));
        } catch (NumberFormatException e) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve ser um número inteiro.");
        }
    }

    public static int inteiroPositivo(String valor, String campo) {
        int v = inteiro(valor, campo);
        if (v <= 0) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve ser maior que zero.");
        }
        return v;
    }

    public static double decimal(String valor, String campo) {
        try {
            return Double.parseDouble(texto(valor, campo).replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve ser um número.");
        }
    }

    public static double decimalPositivo(String valor, String campo) {
        double v = decimal(valor, campo);
        if (v <= 0) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve ser maior que zero.");
        }
        return v;
    }

    public static LocalDate data(String valor, String campo) {
        try {
            return LocalDate.parse(texto(valor, campo));
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("O campo \"" + campo + "\" tem uma data inválida.");
        }
    }

    public static <E extends Enum<E>> E enumeracao(Class<E> tipo, String valor, String campo) {
        try {
            return Enum.valueOf(tipo, texto(valor, campo));
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException("Valor inválido para \"" + campo + "\".");
        }
    }

    public static String email(String valor) {
        String e = texto(valor, "Email").toLowerCase();
        if (!e.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ValidacaoException("Email inválido.");
        }
        return e;
    }
}
