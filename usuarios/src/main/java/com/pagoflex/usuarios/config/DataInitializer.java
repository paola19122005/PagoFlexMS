package com.pagoflex.usuarios.config;

import com.pagoflex.usuarios.entity.PreguntasSeguridad;
import com.pagoflex.usuarios.entity.Usuario;
import com.pagoflex.usuarios.repository.UsuarioRepository;
import com.pagoflex.usuarios.security.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Carga los tres usuarios de prueba de la app Android cuando la tabla esta
 * vacia. Las credenciales y datos coinciden con los seeds de AppDatabase.kt
 * (contraseña: PagoFlex123!).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final String PASSWORD_DEFAULT = "PagoFlex123!";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-uuuu");

    private final UsuarioRepository usuarioRepository;
    private final PasswordHasher passwordHasher;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordHasher passwordHasher) {
        this.usuarioRepository = usuarioRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Tabla usuarios con datos existentes; no se ejecuta la carga inicial");
            return;
        }

        String salt = passwordHasher.generarSalt();
        String hash = passwordHasher.hash(PASSWORD_DEFAULT, salt);

        Usuario usuario = crear(
                "TEST-USUARIO", "Usuario Prueba", "11.111.111-1", "usuario.prueba@pagoflex.cl",
                "USUARIO", "+56 9 6000 0001", "Santiago", null, null, null, null, salt, hash
        );
        Usuario ejecutivo = crear(
                "TEST-EJECUTIVO", "Ejecutivo Prueba", "12.222.222-2", "ejecutivo.prueba@pagoflex.cl",
                "EJECUTIVO", "+56 9 6000 0002", "Las Condes", 1009L, "Cobranza",
                "Ejecutivo de cobranza", "Banco del Sur", salt, hash
        );
        Usuario agente = crear(
                "TEST-AGENTE", "Agente Prueba", "13.333.333-3", "agente.prueba@pagoflex.cl",
                "AGENTE", "+56 9 6000 0003", "Providencia", null, "Soporte",
                "Agente de soporte", null, salt, hash
        );

        usuario = usuarioRepository.save(usuario);
        usuarioRepository.save(ejecutivo);
        Usuario agenteGuardado = usuarioRepository.save(agente);

        usuario.setAgenteAsignadoId(agenteGuardado.getId());
        usuarioRepository.save(usuario);

        log.info("Carga inicial de usuarios completada (3 cuentas de prueba)");
    }

    private Usuario crear(String codigo,
                          String nombre,
                          String rut,
                          String email,
                          String rol,
                          String telefono,
                          String comuna,
                          Long empresaId,
                          String area,
                          String cargo,
                          String empresa,
                          String salt,
                          String hash) {
        Usuario usuario = new Usuario();
        usuario.setCodigo(codigo);
        usuario.setNombre(nombre);
        usuario.setRut(rut);
        usuario.setEmail(email);
        usuario.setPasswordHash(hash);
        usuario.setSalt(salt);
        usuario.setRol(rol);
        usuario.setTelefono(telefono);
        usuario.setComuna(comuna);
        usuario.setEmpresaId(empresaId);
        usuario.setArea(area);
        usuario.setCargo(cargo);
        usuario.setActivo(true);
        usuario.setFechaIncorporacion(LocalDate.now().format(FORMATO_FECHA));
        usuario.setAvisosDias(3);
        usuario.setAvisosActivos(true);
        usuario.setEmpresa(empresa);
        usuario.setPreguntaSeguridad(PreguntasSeguridad.PREGUNTA_DEFAULT);
        usuario.setRespuestaSeguridad(PreguntasSeguridad.RESPUESTA_DEFAULT);
        return usuario;
    }
}
