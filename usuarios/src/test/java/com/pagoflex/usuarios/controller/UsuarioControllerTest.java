package com.pagoflex.usuarios.controller;

import com.pagoflex.usuarios.dto.request.UsuarioActivoRequest;
import com.pagoflex.usuarios.dto.request.UsuarioLoginRequest;
import com.pagoflex.usuarios.dto.request.UsuarioPerfilRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRegistroRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRolRequest;
import com.pagoflex.usuarios.dto.response.UsuarioResponse;
import com.pagoflex.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController controller;

    @Test
    void registrarDevuelveCreated() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.registrar(any())).thenReturn(response);

        var resultado = controller.registrar(new UsuarioRegistroRequest());

        assertEquals(201, resultado.getStatusCode().value());
        assertSame(response, resultado.getBody());
    }

    @Test
    void loginDevuelveUsuario() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.login(any())).thenReturn(response);

        assertSame(response, controller.login(new UsuarioLoginRequest()));
    }

    @Test
    void listarTodosDevuelveLista() {
        when(usuarioService.listarTodos()).thenReturn(List.of(new UsuarioResponse()));

        assertEquals(1, controller.listarTodos().size());
    }

    @Test
    void obtenerPorIdDelegacion() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.obtenerPorId(1L)).thenReturn(response);

        assertSame(response, controller.obtenerPorId(1L));
    }

    @Test
    void obtenerPorCorreoDelegacion() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.obtenerPorCorreo("a@correo.cl")).thenReturn(response);

        assertSame(response, controller.obtenerPorCorreo("a@correo.cl"));
    }

    @Test
    void actualizarPerfilDelegacion() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.actualizarPerfil(eq(1L), any())).thenReturn(response);

        assertSame(response, controller.actualizarPerfil(1L, new UsuarioPerfilRequest()));
    }

    @Test
    void cambiarRolDelegacion() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.cambiarRol(eq(1L), any())).thenReturn(response);

        assertSame(response, controller.cambiarRol(1L, new UsuarioRolRequest()));
    }

    @Test
    void cambiarActivoDelegacion() {
        UsuarioResponse response = new UsuarioResponse();
        when(usuarioService.cambiarActivo(1L, true)).thenReturn(response);

        UsuarioActivoRequest request = new UsuarioActivoRequest();
        request.setActivo(true);

        assertSame(response, controller.cambiarActivo(1L, request));
    }
}
