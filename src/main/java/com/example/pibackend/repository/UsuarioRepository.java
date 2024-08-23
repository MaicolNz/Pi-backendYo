package com.example.pibackend.repository;

import com.example.pibackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Puedes definir métodos adicionales aquí si es necesario
    Optional<Usuario> findByCorreoAndContraseña(String correo, String contraseña);

}
