package universidad.domain;

public enum Rol {
    ESTUDIANTE      (1, "ESTUDIANTE"),
    DOCENTE         (2, "DOCENTE"),
    ADMINISTRATIVO  (3, "ADMINISTRATIVO");

    private final int    id;
    private final String nombre;

    Rol(int id, String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    public int    getId()     { return id;     }
    public String getNombre() { return nombre; }

    public static Rol desdeId(int id) {
        for (Rol r : values()) {
            if (r.id == id) return r;
        }
        throw new NegocioException("Rol desconocido con id=" + id);
    }

    public static Rol desdeNombre(String nombre) {
        for (Rol r : values()) {
            if (r.nombre.equalsIgnoreCase(nombre)) return r;
        }
        throw new NegocioException("Rol desconocido: " + nombre);
    }
}
