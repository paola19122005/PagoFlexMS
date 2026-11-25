package com.pagoflex.usuarios.service;

import com.pagoflex.usuarios.dto.request.UsuarioLoginRequest;
import com.pagoflex.usuarios.dto.request.UsuarioPerfilRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRegistroRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRolRequest;
import com.pagoflex.usuarios.dto.response.UsuarioResponse;
import com.pagoflex.usuarios.entity.Usuario;
import com.pagoflex.usuarios.exception.CredencialesInvalidasException;
import com.pagoflex.usuarios.exception.CuentaDeshabilitadaException;
import com.pagoflex.usuarios.exception.EmailDuplicadoException;
import com.pagoflex.usuarios.exception.RolInvalidoException;
import com.pagoflex.usuarios.exception.UsuarioNoEncontradoException;
import com.pagoflex.usuarios.mapper.UsuarioMapper;
import com.pagoflex.usuarios.repository.UsuarioRepository;
import com.pagoflex.usuarios.security.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Spy
    private UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UsuarioService service;

    private UsuarioRegistroRequest registroValido() {
        UsuarioRegistroRequest request = new UsuarioRegistroRequest();
        request.setNombre("Camila Andrea Rojas");
        request.setEmail("Camila.Rojas@Correo.cl");
        request.setPassword("ClaveSegura1!");
        request.setRut("11.111.111-1");
        request.setTelefono("+56 9 1234 5678");
        request.setComuna("Maipú");
        request.setRespuestaSeguridad("  Mi   Perro  ");
        return request;
    }

    private Usuario usuario(Long id, String email, String rol, boolean activo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail(email);
        usuario.setNombre("Usuario Prueba");
        usuario.setRol(rol);
        usuario.setActivo(activo);
        usuario.setSalt("salt");
        usuario.setPasswordHash("hash");
        usuario.setPreguntaSeguridad("¿Cuál es el nombre de tu primera mascota?");
        usuario.setRespuestaSeguridad("perro");
        return usuario;
    }

    private void prepararRegistro() {
        when(passwordHasher.validar("ClaveSegura1!")).thenReturn(List.of());
    }

    private void prepararGuardado() {
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario guardado = invocation.getArgument(0);
            if (guardado.getId() == null) {
                guardado.setId(10L);
            }
            return guardado;
        });
    }

    @Test
    void registrarFuerzaRolUsuarioYGuardaCredenciales() {
        prepararRegistro();
        when(passwordHasher.generarSalt()).thenReturn("salt-nuevo");
        when(passwordHasher.hash("ClaveSegura1!", "salt-nuevo")).thenReturn("hash-nuevo");
        when(usuarioRepository.existsByEmailIgnoreCase("camila.rojas@correo.cl")).thenReturn(false);
        prepararGuardado();

        UsuarioResponse response = service.registrar(registroValido());

        assertEquals(10L, response.getId().longValue());
        assertEquals("camila.rojas@correo.cl", response.getEmail());
        assertEquals("USUARIO", response.getRol());
        assertTrue(response.isActivo());
        assertEquals(3, response.getAvisosDias().intValue());
        assertTrue(response.isAvisosActivos());
        assertNotNull(response.getFechaIncorporacion());
        assertEquals(10, response.getFechaIncorporacion().length());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();
        assertEquals("hash-nuevo", guardado.getPasswordHash());
        assertEquals("salt-nuevo", guardado.getSalt());
        assertEquals("mi perro", guardado.getRespuestaSeguridad());
        assertEquals("¿Cuál es el nombre de tu primera mascota?", guardado.getPreguntaSeguridad());
        assertNull(guardado.getCodigo());
        assertNull(guardado.getEmpresaId());
        assertNull(guardado.getAgenteAsignadoId());
    }

    @Test
    void registrarRechazaEmailDuplicado() {
        prepararRegistro();
        when(usuarioRepository.existsByEmailIgnoreCase("camila.rojas@correo.cl")).thenReturn(true);

        assertThrows(EmailDuplicadoException.class, () -> service.registrar(registroValido()));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarRechazaEmailInvalido() {
        UsuarioRegistroRequest request = registroValido();
        request.setEmail("sin-arroba.cl");

        assertThrows(IllegalArgumentException.class, () -> service.registrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarRechazaContrasenaDebil() {
        when(passwordHasher.validar("debil")).thenReturn(List.of("Debe tener al menos 8 caracteres"));
        UsuarioRegistroRequest request = registroValido();
        request.setPassword("debil");

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> service.registrar(request));
        assertTrue(error.getMessage().contains("Contraseña inválida"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarRechazaRutInvalido() {
        prepararRegistro();
        UsuarioRegistroRequest request = registroValido();
        request.setRut("12.222.222-2");

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> service.registrar(request));
        assertTrue(error.getMessage().contains("RUT"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarRechazaTelefonoInvalido() {
        prepararRegistro();
        UsuarioRegistroRequest request = registroValido();
        request.setTelefono("telefono-invalido");

        assertThrows(IllegalArgumentException.class, () -> service.registrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void loginExitoso() {
        Usuario usuario = usuario(1L, "usuario.prueba@pagoflex.cl", "USUARIO", true);
        when(usuarioRepository.buscarPorIdentificador("usuario.prueba@pagoflex.cl"))
                .thenReturn(Optional.of(usuario));
        when(passwordHasher.verificar("PagoFlex123!", "salt", "hash")).thenReturn(true);

        UsuarioResponse response = service.login(login("usuario.prueba@pagoflex.cl", "PagoFlex123!"));

        assertEquals(1L, response.getId().longValue());
        assertEquals("USUARIO", response.getRol());
    }

    @Test
    void loginConCodigoDeUsuario() {
        Usuario usuario = usuario(1L, "usuario.prueba@pagoflex.cl", "USUARIO", true);
        when(usuarioRepository.buscarPorIdentificador("TEST-USUARIO")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verificar("PagoFlex123!", "salt", "hash")).thenReturn(true);

        UsuarioResponse response = service.login(login("TEST-USUARIO", "PagoFlex123!"));

        assertEquals("usuario.prueba@pagoflex.cl", response.getEmail());
    }

    @Test
    void loginRechazaPasswordIncorrecta() {
        Usuario usuario = usuario(1L, "usuario.prueba@pagoflex.cl", "USUARIO", true);
        when(usuarioRepository.buscarPorIdentificador("usuario.prueba@pagoflex.cl"))
                .thenReturn(Optional.of(usuario));
        when(passwordHasher.verificar("incorrecta", "salt", "hash")).thenReturn(false);

        assertThrows(CredencialesInvalidasException.class,
                () -> service.login(login("usuario.prueba@pagoflex.cl", "incorrecta")));
    }

    @Test
    void loginRechazaCuentaDeshabilitada() {
        Usuario usuario = usuario(1L, "usuario.prueba@pagoflex.cl", "USUARIO", false);
        when(usuarioRepository.buscarPorIdentificador("usuario.prueba@pagoflex.cl"))
                .thenReturn(Optional.of(usuario));

        CuentaDeshabilitadaException error = assertThrows(CuentaDeshabilitadaException.class,
                () -> service.login(login("usuario.prueba@pagoflex.cl", "PagoFlex123!")));

        assertEquals(UsuarioService.MENSAJE_CUENTA_DESHABILITADA, error.getMessage());
        verify(passwordHasher, never()).verificar(any(), any(), any());
    }

    @Test
    void loginRechazaUsuarioInexistente() {
        when(usuarioRepository.buscarPorIdentificador("nadie@correo.cl")).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.login(login("nadie@correo.cl", "PagoFlex123!")));
    }

    @Test
    void listarTodosMapeaUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(
                usuario(1L, "a@correo.cl", "USUARIO", true),
                usuario(2L, "b@correo.cl", "AGENTE", true)
        ));

        List<UsuarioResponse> usuarios = service.listarTodos();

        assertEquals(2, usuarios.size());
    }

    @Test
    void obtenerPorIdExistente() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario(1L, "a@correo.cl", "USUARIO", true)));

        assertEquals(1L, service.obtenerPorId(1L).getId().longValue());
    }

    @Test
    void obtenerPorIdInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void obtenerPorCorreoExistente() {
        when(usuarioRepository.findByEmailIgnoreCase("a@correo.cl"))
                .thenReturn(Optional.of(usuario(1L, "a@correo.cl", "USUARIO", true)));

        assertEquals(1L, service.obtenerPorCorreo("A@Correo.cl").getId().longValue());
    }

    @Test
    void obtenerPorCorreoInexistente() {
        when(usuarioRepository.findByEmailIgnoreCase("nadie@correo.cl")).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.obtenerPorCorreo("nadie@correo.cl"));
    }

    @Test
    void actualizarPerfilModificaCamposPermitidos() {
        Usuario existente = usuario(1L, "viejo@correo.cl", "USUARIO", true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmailIgnoreCase("nuevo@correo.cl")).thenReturn(false);
        prepararGuardado();

        UsuarioPerfilRequest request = new UsuarioPerfilRequest();
        request.setNombre("  Nombre Nuevo  ");
        request.setEmail("Nuevo@Correo.cl");
        request.setTelefono("+56 9 1111 2222");
        request.setPreguntaSeguridad("¿En qué ciudad naciste?");
        request.setRespuestaSeguridad("  Santiago  ");

        UsuarioResponse response = service.actualizarPerfil(1L, request);

        assertEquals("Nombre Nuevo", response.getNombre());
        assertEquals("nuevo@correo.cl", response.getEmail());
        assertEquals("+56 9 1111 2222", response.getTelefono());
        assertEquals("¿En qué ciudad naciste?", response.getPreguntaSeguridad());
        assertEquals("santiago", existente.getRespuestaSeguridad());
    }

    @Test
    void actualizarPerfilRechazaEmailDuplicado() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario(1L, "viejo@correo.cl", "USUARIO", true)));
        when(usuarioRepository.existsByEmailIgnoreCase("ocupado@correo.cl")).thenReturn(true);

        UsuarioPerfilRequest request = new UsuarioPerfilRequest();
        request.setEmail("ocupado@correo.cl");

        assertThrows(EmailDuplicadoException.class, () -> service.actualizarPerfil(1L, request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarPerfilRechazaNombreCorto() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario(1L, "a@correo.cl", "USUARIO", true)));

        UsuarioPerfilRequest request = new UsuarioPerfilRequest();
        request.setNombre("ab");

        assertThrows(IllegalArgumentException.class, () -> service.actualizarPerfil(1L, request));
    }

    @Test
    void actualizarPerfilUsuarioInexistente() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.actualizarPerfil(5L, new UsuarioPerfilRequest()));
    }

    @Test
    void cambiarRolAEjecutivoAsignaEmpresa() {
        Usuario existente = usuario(1L, "a@correo.cl", "USUARIO", true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        prepararGuardado();

        UsuarioRolRequest request = new UsuarioRolRequest();
        request.setRol("ejecutivo");
        request.setEmpresaId(1009L);
        request.setEmpresa("Banco del Sur");

        UsuarioResponse response = service.cambiarRol(1L, request);

        assertEquals("EJECUTIVO", response.getRol());
        assertEquals(1009L, response.getEmpresaId().longValue());
        assertEquals("Banco del Sur", response.getEmpresa());
        assertEquals("Cobranza", response.getArea());
        assertEquals("Ejecutivo", response.getCargo());
    }

    @Test
    void cambiarRolAAgenteLimpiaEmpresa() {
        Usuario existente = usuario(1L, "a@correo.cl", "EJECUTIVO", true);
        existente.setEmpresaId(1009L);
        existente.setEmpresa("Banco del Sur");
        existente.setAgenteAsignadoId(3L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        prepararGuardado();

        UsuarioRolRequest request = new UsuarioRolRequest();
        request.setRol("AGENTE");

        UsuarioResponse response = service.cambiarRol(1L, request);

        assertEquals("AGENTE", response.getRol());
        assertNull(response.getEmpresaId());
        assertNull(response.getEmpresa());
        assertNull(response.getAgenteAsignadoId());
        assertEquals("Soporte", response.getArea());
        assertEquals("Agente de soporte", response.getCargo());
    }

    @Test
    void cambiarRolRechazaRolInvalido() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario(1L, "a@correo.cl", "USUARIO", true)));

        UsuarioRolRequest request = new UsuarioRolRequest();
        request.setRol("ADMIN");

        assertThrows(RolInvalidoException.class, () -> service.cambiarRol(1L, request));
    }

    @Test
    void cambiarRolAEjecutivoExigeEmpresa() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario(1L, "a@correo.cl", "USUARIO", true)));

        UsuarioRolRequest request = new UsuarioRolRequest();
        request.setRol("EJECUTIVO");

        assertThrows(IllegalArgumentException.class, () -> service.cambiarRol(1L, request));
    }

    @Test
    void cambiarActivoDeshabilitaCuenta() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario(1L, "a@correo.cl", "USUARIO", true)));
        prepararGuardado();

        UsuarioResponse response = service.cambiarActivo(1L, false);

        assertFalse(response.isActivo());
    }

    private UsuarioLoginRequest login(String email, String password) {
        UsuarioLoginRequest request = new UsuarioLoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }
}
