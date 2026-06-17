package universidad.domain;

public final class Nota {

    public static final double MINIMA = 0.0;
    public static final double MAXIMA = 10.0;

    private final double valor;

    public Nota(double valor) {
        if (valor < MINIMA || valor > MAXIMA) {
            throw new IllegalArgumentException(
                "La nota debe estar entre " + MINIMA + " y " + MAXIMA + "."
            );
        }
        this.valor = valor;
    }

    public double getValor() { return valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nota otra)) return false;
        return Double.compare(otra.valor, valor) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(valor);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
