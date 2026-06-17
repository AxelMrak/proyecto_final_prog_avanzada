package universidad;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String leerTexto(String mensaje, String nombreCampo) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
            String error = Validador.validarNoVacio(valor, nombreCampo);
            if (error == null) return valor;
            System.out.println("  Error: " + error);
        }
    }

    public String leerEmail(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
            String error = Validador.validarEmail(valor);
            if (error == null) return valor;
            System.out.println("  Error: " + error);
        }
    }

    public int leerEnteroEnRango(String mensaje, String nombreCampo, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("  Error: " + nombreCampo + " no puede estar vacio.");
                continue;
            }
            int valor;
            try {
                valor = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("  Error: " + nombreCampo + " debe ser un numero valido.");
                continue;
            }
            if (valor < min || valor > max) {
                System.out.println("  Error: " + nombreCampo + " debe estar entre " + min + " y " + max + ".");
                continue;
            }
            return valor;
        }
    }

    public double leerDoubleEnRango(String mensaje, String nombreCampo, double min, double max) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("  Error: " + nombreCampo + " no puede estar vacio.");
                continue;
            }
            double valor;
            try {
                valor = Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("  Error: " + nombreCampo + " debe ser un numero valido.");
                continue;
            }
            if (valor < min || valor > max) {
                System.out.println("  Error: " + nombreCampo + " debe estar entre " + min + " y " + max + ".");
                continue;
            }
            return valor;
        }
    }

    public String seleccionarDeLista(String titulo, String prompt, List<String> valores, Function<Integer, String> resolver) {
        System.out.println(titulo);
        for (int i = 0; i < valores.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + valores.get(i));
        }
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("  Error: ingrese un numero de la lista o el valor.");
                continue;
            }
            int n;
            try {
                n = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                return linea;
            }
            if (n < 1 || n > valores.size()) {
                System.out.println("  Error: numero fuera de rango (1-" + valores.size() + ").");
                continue;
            }
            try {
                return resolver.apply(n - 1);
            } catch (RuntimeException e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }
    }
}
