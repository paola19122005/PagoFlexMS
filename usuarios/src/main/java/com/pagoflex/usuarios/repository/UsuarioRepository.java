package com.pagoflex.usuarios.repository;

import com.pagoflex.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Optional<Usuario> findByCodigoIgnoreCase(String codigo);

    boolean existsByEmailIgnoreCase(String email);

    /**
     * Login por correo o codigo, replicando la consulta de UserDao en Android.
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(TRIM(u.email)) = LOWER(TRIM(:identificador)) "
            + "OR LOWER(TRIM(u.codigo)) = LOWER(TRIM(:identificador))")
    Optional<Usuario> buscarPorIdentificador(@Param("identificador") String identificador);
}
