package universidad.domain;

import universidad.Validador;

public final class Materia {

    private final Integer id;
    private final String  nombre;
    private final String  codigo;
    private final int     anio;

    public Materia(Integer id, String nombre, String codigo, int anio) {
        String error = Validador.validarNoVacio(nombre, "El nombre de la materia");
        if (error != null) throw new NegocioException(error);

        error = Validador.validarNoVacio(codigo, "El codigo de la materia");
        if (error != null) throw new NegocioException(error);

        if (anio < 1 || anio > 10) {
            throw new NegocioException("El anio de la materia debe estar entre 1 y 10.");
        }

        this.id     = id;
        this.nombre = nombre.trim();
        this.codigo = codigo.trim();
        this.anio   = anio;
    }

    public Materia(String nombre, String codigo, int anio) {
        this(null, nombre, codigo, anio);
    }

    public Integer getId()     { return id;     }
    public String  getNombre() { return nombre; }
    public String  getCodigo() { return codigo; }
    public int     getAnio()   { return anio;   }
}
