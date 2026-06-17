package universidad.ui.menu;

import universidad.InputReader;
import universidad.application.AdministrativoService;
import universidad.domain.Administrativo;
import universidad.domain.NegocioException;

import java.util.List;

public class MenuAdministrativos {

    private final AdministrativoService service;
    private final InputReader           input;

    public MenuAdministrativos(AdministrativoService service, InputReader input) {
        this.service = service;
        this.input   = input;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- Administrativos ---");
            System.out.println("1. Listar");
            System.out.println("2. Agregar");
            System.out.println("0. Volver");
            opcion = input.leerEnteroEnRango("Opcion: ", "Opcion", 0, 2);
            try {
                switch (opcion) {
                    case 1 -> listar();
                    case 2 -> agregar();
                }
            } catch (NegocioException e) {
                System.out.println("  Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Administrativo> lista = service.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay administrativos cargados.");
            return;
        }
        lista.forEach(Administrativo::mostrarInformacion);
    }

    private void agregar() {
        String nombre = input.leerTexto("Nombre: ", "El nombre");
        String legajo = input.leerTexto("Legajo: ", "El legajo");
        String email  = input.leerEmail("Email: ");
        String sector = input.leerTexto("Sector: ", "El sector");
        Long id = service.crear(nombre, legajo, email, sector);
        System.out.println("Administrativo creado con id " + id + ".");
    }
}
