package com.example.pibackend.controller;

import com.example.pibackend.DTO.LoginRequest;
import com.example.pibackend.entity.Usuario;
import com.example.pibackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Usuario> usuarioOptional = usuarioService.autenticarUsuario(loginRequest.getCorreo(), loginRequest.getContraseña());

            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                return ResponseEntity.ok(new LoginResponse("Login exitoso", usuario));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }
    }

    public static class LoginResponse {
        private String message;
        private Usuario usuario;

        public LoginResponse(String message, Usuario usuario) {
            this.message = message;
            this.usuario = usuario;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
    }
}
