package universidad.domain;

import java.time.LocalDate;

public final class Inscripcion {

    private final Integer             id;
    private final int                 estudianteId;
    private final int                 materiaId;
    private final LocalDate           fecha;
    private final EstadoInscripcion   estado;
    private final Nota                notaFinal;

    public Inscripcion(Integer id, int estudianteId, int materiaId,
                       LocalDate fecha, EstadoInscripcion estado, Nota notaFinal) {
        this.id            = id;
        this.estudianteId  = estudianteId;
        this.materiaId     = materiaId;
        this.fecha         = fecha;
        this.estado        = estado;
        this.notaFinal     = notaFinal;
    }

    public Inscripcion(int estudianteId, int materiaId, LocalDate fecha) {
        this(null, estudianteId, materiaId, fecha, EstadoInscripcion.ACTIVA, null);
    }

    public Integer           getId()           { return id;           }
    public int               getEstudianteId() { return estudianteId; }
    public int               getMateriaId()    { return materiaId;    }
    public LocalDate         getFecha()        { return fecha;        }
    public EstadoInscripcion getEstado()       { return estado;       }
    public Nota              getNotaFinal()    { return notaFinal;    }
}
