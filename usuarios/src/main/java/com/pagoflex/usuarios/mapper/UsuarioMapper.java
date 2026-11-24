package com.pagoflex.usuarios.mapper;

import com.pagoflex.usuarios.dto.request.UsuarioPerfilRequest;
import com.pagoflex.usuarios.dto.request.UsuarioRegistroRequest;
import com.pagoflex.usuarios.dto.response.UsuarioResponse;
import com.pagoflex.usuarios.entity.PreguntasSeguridad;
import com.pagoflex.usuarios.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRegistroRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(limpiar(request.getNombre()));
        usuario.setRut(limpiarONulo(request.getRut()));
        usuario.setEmail(limpiar(request.getEmail()).toLowerCase(Locale.ROOT));
        usuario.setTelefono(limpiarONulo(request.getTelefono()));
        usuario.setComuna(limpiarONulo(request.getComuna()));
        usuario.setEmpresa(limpiarONulo(request.getEmpresa()));
        usuario.setPreguntaSeguridad(preguntaOrDefault(request.getPreguntaSeguridad()));
        usuario.setRespuestaSeguridad(PreguntasSeguridad.normalizarRespuesta(request.getRespuestaSeguridad()));
        return usuario;
    }

    public void aplicarPerfil(Usuario usuario, UsuarioPerfilRequest request) {
        if (request.getNombre() != null) {
            usuario.setNombre(limpiar(request.getNombre()));
        }
        if (request.getEmail() != null) {
            usuario.setEmail(limpiar(request.getEmail()).toLowerCase(Locale.ROOT));
        }
        if (request.getTelefono() != null) {
            String telefono = limpiar(request.getTelefono());
            usuario.setTelefono(telefono.isEmpty() ? null : telefono);
        }
        if (request.getPreguntaSeguridad() != null) {
            usuario.setPreguntaSeguridad(limpiar(request.getPreguntaSeguridad()));
        }
        if (request.getRespuestaSeguridad() != null) {
            usuario.setRespuestaSeguridad(
                    PreguntasSeguridad.normalizarRespuesta(request.getRespuestaSeguridad())
            );
        }
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setCodigo(usuario.getCodigo());
        response.setNombre(usuario.getNombre());
        response.setRut(usuario.getRut());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol());
        response.setTelefono(usuario.getTelefono());
        response.setComuna(usuario.getComuna());
        response.setEmpresaId(usuario.getEmpresaId());
        response.setArea(usuario.getArea());
        response.setCargo(usuario.getCargo());
        response.setActivo(usuario.isActivo());
        response.setFechaIncorporacion(usuario.getFechaIncorporacion());
        response.setAvisosDias(usuario.getAvisosDias());
        response.setAvisosActivos(usuario.isAvisosActivos());
        response.setEmpresa(usuario.getEmpresa());
        response.setAgenteAsignadoId(usuario.getAgenteAsignadoId());
        response.setFotoUri(usuario.getFotoUri());
        response.setPreguntaSeguridad(usuario.getPreguntaSeguridad());
        return response;
    }

    private String preguntaOrDefault(String pregunta) {
        String limpia = limpiarONulo(pregunta);
        return limpia == null ? PreguntasSeguridad.PREGUNTA_DEFAULT : limpia;
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private String limpiarONulo(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
