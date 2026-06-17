package universidad.domain;

public enum CategoriaDocente {
    TITULAR                 (1, "Titular"),
    ADJUNTO                 (2, "Adjunto"),
    JEFE_TRABAJOS_PRACTICOS (3, "Jefe de Trabajos Practicos"),
    AYUDANTE                (4, "Ayudante");

    private final int    id;
    private final String nombre;

    CategoriaDocente(int id, String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    public int    getId()     { return id;     }
    public String getNombre() { return nombre; }

    public static CategoriaDocente desdeId(int id) {
        for (CategoriaDocente c : values()) {
            if (c.id == id) return c;
        }
        throw new NegocioException("Categoria de docente desconocida con id=" + id);
    }

    public static CategoriaDocente desdeNombre(String nombre) {
        for (CategoriaDocente c : values()) {
            if (c.nombre.equalsIgnoreCase(nombre)) return c;
        }
        throw new NegocioException("Categoria de docente desconocida: " + nombre);
    }
}
