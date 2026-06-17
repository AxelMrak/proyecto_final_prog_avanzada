package universidad.domain;

public class Administrativo extends UsuarioAcademico {

    private final String sector;

    public Administrativo(Integer id, String nombre, String legajo, String email, String sector) {
        super(id, nombre, legajo, email);
        this.sector = sector;
    }

    public Administrativo(String nombre, String legajo, String email, String sector) {
        this(null, nombre, legajo, email, sector);
    }

    @Override
    public String obtenerRol() { return "Administrativo"; }

    @Override
    public String accederPortal() {
        return nombre + " accede al Panel Administrativo - Sector: " + sector;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== ADMINISTRATIVO ===");
        System.out.println("Nombre : " + nombre);
        System.out.println("Legajo : " + legajo);
        System.out.println("Email  : " + email);
        System.out.println("Sector : " + sector);
    }

    public String getSector() { return sector; }
}
