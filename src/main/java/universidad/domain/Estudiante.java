package universidad.domain;

import universidad.Validador;

public class Estudiante extends UsuarioAcademico {

    private final Integer id;
    private final String  carrera;
    private final int     anioIngreso;

    public Estudiante(Integer id, String nombre, String legajo, String email,
                      String carrera, int anioIngreso) {
        super(id, nombre, legajo, email);

        String error = Validador.validarNoVacio(carrera, "La carrera");
        if (error != null) throw new NegocioException(error);

        error = Validador.validarAnioIngreso(anioIngreso);
        if (error != null) throw new NegocioException(error);

        this.id          = id;
        this.carrera     = carrera.trim();
        this.anioIngreso = anioIngreso;
    }

    public Estudiante(String nombre, String legajo, String email,
                      String carrera, int anioIngreso) {
        this(null, nombre, legajo, email, carrera, anioIngreso);
    }

    public int getIdOrThrow() {
        if (id == null) {
            throw new IllegalStateException("Estudiante aun no persistido (id null).");
        }
        return id;
    }

    public Integer getId()         { return id;          }
    public String  getCarrera()    { return carrera;     }
    public int     getAnioIngreso() { return anioIngreso; }

    @Override
    public String obtenerRol() { return "Estudiante"; }

    @Override
    public String accederPortal() {
        return nombre + " accede al Portal del Alumno - Carrera: " + carrera;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== ESTUDIANTE ===");
        System.out.println("Nombre  : " + nombre);
        System.out.println("Legajo  : " + legajo);
        System.out.println("Email   : " + email);
        System.out.println("Carrera : " + carrera);
        System.out.println("Ingreso : " + anioIngreso);
    }
}
