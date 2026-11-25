package com.pagoflex.usuarios.controller;

import com.pagoflex.usuarios.dto.request.UsuarioActivoRequest;
import com.pagoflex.usuarios.dto.request.UsuarioCambioContrasenaRequest;
import com.pagoflex.usuarios.dto.request.UsuarioLoginRequest;
import com.pagoflex.usuarios.dto.request.UsuarioResetContrasenaRequest;
import com.pagoflex.usuarios.dto.request.UsuarioPerfilRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRegistroRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRolRequest;
import com.pagoflex.usuarios.dto.response.UsuarioResponse;
import com.pagoflex.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRegistroRequest request) {
        UsuarioResponse response = usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody UsuarioLoginRequest request) {
        return usuarioService.login(request);
    }

    @GetMapping
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping("/correo/{email}")
    public UsuarioResponse obtenerPorCorreo(@PathVariable String email) {
        return usuarioService.obtenerPorCorreo(email);
    }

    @PutMapping("/{id}/perfil")
    public UsuarioResponse actualizarPerfil(@PathVariable Long id,
                                            @Valid @RequestBody UsuarioPerfilRequest request) {
        return usuarioService.actualizarPerfil(id, request);
    }

    @PutMapping("/{id}/rol")
    public UsuarioResponse cambiarRol(@PathVariable Long id,
                                      @Valid @RequestBody UsuarioRolRequest request) {
        return usuarioService.cambiarRol(id, request);
    }

    @PatchMapping("/{id}/activo")
    public UsuarioResponse cambiarActivo(@PathVariable Long id,
                                         @Valid @RequestBody UsuarioActivoRequest request) {
        return usuarioService.cambiarActivo(id, request.getActivo());
    }

    @PutMapping("/{id}/contrasena")
    public ResponseEntity<Void> cambiarContrasena(@PathVariable Long id,
                                                  @Valid @RequestBody UsuarioCambioContrasenaRequest request) {
        usuarioService.cambiarContrasena(id, request.getActual(), request.getNueva());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reset-contrasena")
    public ResponseEntity<Void> resetContrasena(@Valid @RequestBody UsuarioResetContrasenaRequest request) {
        usuarioService.resetContrasena(request.getEmail(), request.getNuevaPassword());
        return ResponseEntity.noContent().build();
    }
}
