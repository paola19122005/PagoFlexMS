package com.pagoflex.usuarios.service;

import com.pagoflex.usuarios.dto.request.UsuarioLoginRequest;
import com.pagoflex.usuarios.dto.request.UsuarioPerfilRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRegistroRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRolRequest;
import com.pagoflex.usuarios.dto.response.UsuarioResponse;
import com.pagoflex.usuarios.entity.PreguntasSeguridad;
import com.pagoflex.usuarios.entity.Usuario;
import com.pagoflex.usuarios.exception.CredencialesInvalidasException;
import com.pagoflex.usuarios.exception.CuentaDeshabilitadaException;
import com.pagoflex.usuarios.exception.EmailDuplicadoException;
import com.pagoflex.usuarios.exception.RolInvalidoException;
import com.pagoflex.usuarios.exception.UsuarioNoEncontradoException;
import com.pagoflex.usuarios.mapper.UsuarioMapper;
import com.pagoflex.usuarios.repository.UsuarioRepository;
import com.pagoflex.usuarios.security.PasswordHasher;
import com.pagoflex.usuarios.validation.RutValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    public static final String ROL_USUARIO = "USUARIO";
    public static final String ROL_EJECUTIVO = "EJECUTIVO";
    public static final String ROL_AGENTE = "AGENTE";

    public static final String MENSAJE_CUENTA_DESHABILITADA =
            "Esta cuenta ha sido deshabilitada. Contacta a un agente o administrador.";

    private static final Set<String> ROLES_VALIDOS = Set.of(ROL_USUARIO, ROL_EJECUTIVO, ROL_AGENTE);
    private static final Pattern FORMATO_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private static final int AVISOS_DIAS_DEFAULT = 3;

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordHasher passwordHasher;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          PasswordHasher passwordHasher) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public UsuarioResponse registrar(UsuarioRegistroRequest request) {
        String email = normalizarEmail(request.getEmail());
        validarNombre(request.getNombre());
        validarCorreo(email);
        validarContrasena(request.getPassword());
        validarRut(request.getRut());
        validarTelefonoOpcional(request.getTelefono());

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailDuplicadoException("El correo ya está registrado: " + email);
        }

        String salt = passwordHasher.generarSalt();
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordHasher.hash(request.getPassword(), salt));
        usuario.setSalt(salt);
        usuario.setRol(ROL_USUARIO);
        usuario.setActivo(true);
        usuario.setAvisosDias(AVISOS_DIAS_DEFAULT);
        usuario.setAvisosActivos(true);
        usuario.setFechaIncorporacion(LocalDate.now().format(FORMATO_FECHA));
        usuario.setCodigo(null);
        usuario.setAgenteAsignadoId(null);
        usuario.setEmpresaId(null);
        usuario.setArea(null);
        usuario.setCargo(null);
        usuario.setFotoUri(null);

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse login(UsuarioLoginRequest request) {
        String identificador = request.getEmail() == null ? "" : request.getEmail().trim();
        if (identificador.isEmpty()) {
            throw new IllegalArgumentException("Debes ingresar tu correo o código de usuario");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Debes ingresar tu contraseña");
        }

        Usuario usuario = usuarioRepository.buscarPorIdentificador(identificador)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "Usuario no encontrado: " + identificador));

        if (!usuario.isActivo()) {
            throw new CuentaDeshabilitadaException(MENSAJE_CUENTA_DESHABILITADA);
        }
        if (!passwordHasher.verificar(request.getPassword(), usuario.getSalt(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        }
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return usuarioMapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorCorreo(String email) {
        String correo = normalizarEmail(email);
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "Usuario no encontrado para el correo: " + correo));
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse actualizarPerfil(Long id, UsuarioPerfilRequest request) {
        Usuario usuario = buscar(id);

        if (request.getNombre() != null) {
            validarNombre(request.getNombre());
        }
        if (request.getEmail() != null) {
            String email = normalizarEmail(request.getEmail());
            validarCorreo(email);
            if (!email.equalsIgnoreCase(usuario.getEmail())
                    && usuarioRepository.existsByEmailIgnoreCase(email)) {
                throw new EmailDuplicadoException("El correo ya está registrado: " + email);
            }
        }
        if (request.getTelefono() != null) {
            validarTelefonoOpcional(request.getTelefono());
        }
        if (request.getPreguntaSeguridad() != null && request.getPreguntaSeguridad().isBlank()) {
            throw new IllegalArgumentException("La pregunta de seguridad no puede estar vacía");
        }
        if (request.getRespuestaSeguridad() != null && request.getRespuestaSeguridad().isBlank()) {
            throw new IllegalArgumentException("La respuesta de seguridad no puede estar vacía");
        }

        usuarioMapper.aplicarPerfil(usuario, request);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Transactional
    public UsuarioResponse cambiarRol(Long id, UsuarioRolRequest request) {
        Usuario usuario = buscar(id);
        String rol = request.getRol() == null
                ? ""
                : request.getRol().trim().toUpperCase(Locale.ROOT);

        if (!ROLES_VALIDOS.contains(rol)) {
            throw new RolInvalidoException(
                    "Rol inválido. Valores permitidos: USUARIO, EJECUTIVO, AGENTE");
        }

        switch (rol) {
            case ROL_EJECUTIVO -> {
                if (request.getEmpresaId() == null) {
                    throw new IllegalArgumentException(
                            "Para el rol EJECUTIVO debes indicar empresaId");
                }
                usuario.setEmpresaId(request.getEmpresaId());
                if (request.getEmpresa() != null && !request.getEmpresa().isBlank()) {
                    usuario.setEmpresa(request.getEmpresa().trim());
                }
                usuario.setArea(valorODefault(usuario.getArea(), "Cobranza"));
                usuario.setCargo(valorODefault(usuario.getCargo(), "Ejecutivo"));
            }
            case ROL_AGENTE -> {
                usuario.setEmpresaId(null);
                usuario.setEmpresa(null);
                usuario.setArea(valorODefault(usuario.getArea(), "Soporte"));
                usuario.setCargo(valorODefault(usuario.getCargo(), "Agente de soporte"));
                usuario.setAgenteAsignadoId(null);
            }
            default -> {
                // USUARIO: conserva empresa y agente asignado existentes.
            }
        }

        usuario.setRol(rol);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Transactional
    public UsuarioResponse cambiarActivo(Long id, boolean activo) {
        Usuario usuario = buscar(id);
        usuario.setActivo(activo);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "Usuario no encontrado: " + id));
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private void validarNombre(String nombre) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
    }

    private void validarCorreo(String email) {
        if (email.isEmpty() || !FORMATO_CORREO.matcher(email).matches()) {
            throw new IllegalArgumentException(
                    "Ingresa un correo válido, por ejemplo nombre@dominio.cl");
        }
    }

    private void validarContrasena(String password) {
        List<String> errores = passwordHasher.validar(password);
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("Contraseña inválida: " + String.join(". ", errores));
        }
    }

    private void validarRut(String rut) {
        if (rut == null || rut.isBlank()) {
            return;
        }
        if (!RutValidator.esValido(rut)) {
            throw new IllegalArgumentException("RUT inválido. Usa el formato 12.345.678-9");
        }
    }

    private void validarTelefonoOpcional(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            return;
        }
        String texto = telefono.trim();
        boolean caracteresPermitidos = texto.chars()
                .allMatch(c -> Character.isDigit(c) || c == ' ' || c == '+' || c == '-' || c == '(' || c == ')');
        if (!caracteresPermitidos) {
            throw new IllegalArgumentException(
                    "El teléfono solo puede contener números, espacios y los signos +, - o paréntesis");
        }

        String digitos = texto.replaceAll("\\D", "");
        String nacional = digitos.startsWith("56") && digitos.length() > 9
                ? digitos.substring(2)
                : digitos;

        boolean esMovil = nacional.length() == 9 && nacional.startsWith("9");
        boolean esFijoSantiago = nacional.length() == 9 && nacional.startsWith("2");
        if (!esMovil && !esFijoSantiago) {
            throw new IllegalArgumentException(
                    "Ingresa un teléfono chileno válido, por ejemplo +56 9 1234 5678");
        }
    }

    private String valorODefault(String valor, String defecto) {
        return valor == null || valor.isBlank() ? defecto : valor;
    }

    /**
     * Pregunta por defecto usada por la app Android; se expone para el
     * inicializador de datos y para futuras pantallas de recuperacion.
     */
    public static String preguntaSeguridadDefault() {
        return PreguntasSeguridad.PREGUNTA_DEFAULT;
    }
}
