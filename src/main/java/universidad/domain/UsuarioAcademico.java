package universidad.domain;

import universidad.Validador;

public abstract class UsuarioAcademico {

    protected final Integer id;
    protected final String  nombre;
    protected final String  legajo;
    protected final String  email;

    protected UsuarioAcademico(Integer id, String nombre, String legajo, String email) {
        String error;

        error = Validador.validarNoVacio(nombre, "El nombre");
        if (error != null) throw new NegocioException(error);

        error = Validador.validarNoVacio(legajo, "El legajo");
        if (error != null) throw new NegocioException(error);

        error = Validador.validarEmail(email);
        if (error != null) throw new NegocioException(error);

        this.id     = id;
        this.nombre = nombre.trim();
        this.legajo = legajo.trim();
        this.email  = email.trim();
    }

    public Integer getId()     { return id;     }
    public String  getNombre() { return nombre; }
    public String  getLegajo() { return legajo; }
    public String  getEmail()  { return email;  }

    public int getIdOrThrow() {
        if (id == null) {
            throw new IllegalStateException("Usuario academico aun no persistido (id null).");
        }
        return id;
    }

    public abstract String obtenerRol();
    public abstract String accederPortal();
    public abstract void mostrarInformacion();
}
