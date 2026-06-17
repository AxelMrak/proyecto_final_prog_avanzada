package universidad;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final int ANIO_MINIMO = 1900;
    private static final int ANIO_MAXIMO = 2099;
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 10.0;

    public static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return "El email no puede estar vacio.";
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El email tiene un formato invalido. Ejemplo: usuario@dominio.com";
        }
        return null;
    }

    public static String validarNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            return campo + " no puede estar vacio.";
        }
        return null;
    }

    public static String validarAnioIngreso(int anio) {
        if (anio < ANIO_MINIMO || anio > ANIO_MAXIMO) {
            return "El anio de ingreso debe estar entre " + ANIO_MINIMO + " y " + ANIO_MAXIMO + ".";
        }
        return null;
    }

    public static String validarNota(double nota) {
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            return "La nota debe estar entre " + NOTA_MINIMA + " y " + NOTA_MAXIMA + ".";
        }
        return null;
    }

    public static String validarEnteroPositivo(String valor, String campo) {
        try {
            int num = Integer.parseInt(valor.trim());
            if (num <= 0) {
                return campo + " debe ser un numero positivo.";
            }
            return null;
        } catch (NumberFormatException e) {
            return campo + " debe ser un numero valido.";
        }
    }
}
