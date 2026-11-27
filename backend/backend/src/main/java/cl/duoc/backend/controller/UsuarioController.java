package cl.duoc.backend.controller;

import cl.duoc.backend.model.Usuario;
import cl.duoc.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario u) {
        return repo.save(u);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario u) {
        u.setId(id);
        return repo.save(u);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}