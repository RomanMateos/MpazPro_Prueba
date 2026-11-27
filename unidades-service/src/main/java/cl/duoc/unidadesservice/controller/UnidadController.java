package cl.duoc.unidadesservice.controller;

import cl.duoc.unidadesservice.model.Unidad;
import cl.duoc.unidadesservice.service.UnidadService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/unidades")
public class UnidadController {

    private final UnidadService service;

    public UnidadController(UnidadService service) {
        this.service = service;
    }

    @GetMapping
    public List<Unidad> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public Unidad obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Unidad> crear(@Valid @RequestBody Unidad unidad) {
        Unidad creada = service.crear(unidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public Unidad actualizar(@PathVariable Long id, @Valid @RequestBody Unidad unidad) {
        return service.actualizar(id, unidad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
