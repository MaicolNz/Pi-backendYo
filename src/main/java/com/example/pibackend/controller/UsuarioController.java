package com.example.pibackend.controller;

import com.example.pibackend.entity.Usuario;
import com.example.pibackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")  // Permitir solicitudes desde el origen de tu frontend
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Método por defecto para obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Método para obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Método para registrar un nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioGuardado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    // Método para actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        if (!usuarioExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuarioActual = usuarioExistente.get();
        // Actualizar solo los campos que no sean nulos
        if (usuario.getNombre() != null) {
            usuarioActual.setNombre(usuario.getNombre());
        }
        if (usuario.getApellido() != null) {
            usuarioActual.setApellido(usuario.getApellido());
        }
        if (usuario.getCorreo() != null) {
            usuarioActual.setCorreo(usuario.getCorreo());
        }
        // Solo actualizar la contraseña si ha sido proporcionada
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            usuarioActual.setContraseña(usuario.getContraseña());
        }
        usuarioActual.setEsAdmin(usuario.getEsAdmin());

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuarioActual);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }


    // Método para eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (!usuarioService.obtenerUsuarioPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Método para asignar rol de administrador a un usuario
    @PatchMapping("/{id}/admin")
    public ResponseEntity<Usuario> asignarAdmin(@PathVariable Integer id) {
        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        if (!usuarioExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioExistente.get();
        usuario.setEsAdmin(true);
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario);

        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    // Método para obtener usuarios paginados
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<Usuario> usuariosPage = usuarioService.obtenerUsuariosPaginados(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", usuariosPage.getContent()); // Lista de usuarios
        response.put("totalPages", usuariosPage.getTotalPages()); // Número total de páginas
        response.put("totalElements", usuariosPage.getTotalElements()); // Número total de elementos
        response.put("currentPage", usuariosPage.getNumber()); // Página actual

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/{id}/remove-admin")
    public ResponseEntity<Usuario> removerAdmin(@PathVariable Integer id) {
        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        if (!usuarioExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioExistente.get();
        usuario.setEsAdmin(false);
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario);

        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

}
