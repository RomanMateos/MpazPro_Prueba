package cl.duoc.unidadesservice.service;

import cl.duoc.unidadesservice.exception.RecursoNoEncontradoException;
import cl.duoc.unidadesservice.model.Unidad;
import cl.duoc.unidadesservice.repository.UnidadRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UnidadService {

    private final UnidadRepository repository;

    public UnidadService(UnidadRepository repository) {
        this.repository = repository;
    }

    public List<Unidad> listarTodas() {
        return repository.findAll();
    }

    public Unidad obtenerPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Unidad no encontrada con id: " + id));
    }

    public Unidad crear(Unidad unidad) {
        unidad.setId(null); // Fuerza inserción nueva
        return repository.save(unidad);
    }

    public Unidad actualizar(Long id, Unidad datos) {
        Unidad existente = obtenerPorId(id);
        existente.setTitulo(datos.getTitulo());
        existente.setAsignatura(datos.getAsignatura());
        existente.setOa(datos.getOa());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Unidad existente = obtenerPorId(id);
        repository.delete(existente);
    }
}
