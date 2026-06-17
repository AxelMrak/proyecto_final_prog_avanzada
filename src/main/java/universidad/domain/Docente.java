package universidad.domain;

public class Docente extends UsuarioAcademico {

    private final String departamento;
    private final String categoria;

    public Docente(Integer id, String nombre, String legajo, String email,
                   String departamento, String categoria) {
        super(id, nombre, legajo, email);
        this.departamento = departamento;
        this.categoria    = categoria;
    }

    public Docente(String nombre, String legajo, String email,
                   String departamento, String categoria) {
        this(null, nombre, legajo, email, departamento, categoria);
    }

    @Override
    public String obtenerRol() { return "Docente"; }

    @Override
    public String accederPortal() {
        return nombre + " accede al Portal Docente - Depto: " + departamento;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== DOCENTE ===");
        System.out.println("Nombre       : " + nombre);
        System.out.println("Legajo       : " + legajo);
        System.out.println("Email        : " + email);
        System.out.println("Departamento : " + departamento);
        System.out.println("Categoria    : " + categoria);
    }

    public String getDepartamento() { return departamento; }
    public String getCategoria()    { return categoria;    }
}
