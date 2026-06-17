package universidad.domain;

public enum EstadoInscripcion {
    ACTIVA     (1, "ACTIVA"),
    APROBADA   (2, "APROBADA"),
    DESAPROBADA(3, "DESAPROBADA"),
    ABANDONADA (4, "ABANDONADA");

    private final int    id;
    private final String nombre;

    EstadoInscripcion(int id, String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    public int    getId()     { return id;     }
    public String getNombre() { return nombre; }

    public static EstadoInscripcion desdeId(int id) {
        for (EstadoInscripcion e : values()) {
            if (e.id == id) return e;
        }
        throw new NegocioException("Estado de inscripcion desconocido con id=" + id);
    }

    public static EstadoInscripcion desdeNombre(String nombre) {
        for (EstadoInscripcion e : values()) {
            if (e.nombre.equalsIgnoreCase(nombre)) return e;
        }
        throw new NegocioException("Estado de inscripcion desconocido: " + nombre);
    }
}
