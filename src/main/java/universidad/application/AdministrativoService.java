package universidad.application;

import universidad.domain.Administrativo;
import universidad.domain.repository.IAdministrativoRepository;

import java.util.List;

public class AdministrativoService {

    private final IAdministrativoRepository repo;

    public AdministrativoService(IAdministrativoRepository repo) {
        this.repo = repo;
    }

    public Long crear(String nombre, String legajo, String email, String sector) {
        Administrativo a = new Administrativo(nombre, legajo, email, sector);
        return repo.guardar(a, a.getSector());
    }

    public List<Administrativo> listar() {
        return repo.listar();
    }
}
