package com.pagoflex.empresas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pagoflex.empresas.dto.request.EmpresasRegistroRequest;
import com.pagoflex.empresas.dto.response.EmpresasResponse;
import com.pagoflex.empresas.entity.Empresas;
import com.pagoflex.empresas.exception.EmpresaDuplicadaException;
import com.pagoflex.empresas.exception.EmpresaNoEncontradaException;
import com.pagoflex.empresas.mapper.EmpresasMapper;
import com.pagoflex.empresas.repository.EmpresasRepository;

@ExtendWith(MockitoExtension.class)
class EmpresasServiceTest {

    @Mock
    private EmpresasRepository empresasRepository;

    @Mock
    private EmpresasMapper empresasMapper;

    @InjectMocks
    private EmpresasService service;

    @Test
    void crear() {

        EmpresasRegistroRequest request =
                new EmpresasRegistroRequest();

        request.setCodigo("EMP001");
        request.setRut("76.123.456-7");

        Empresas empresas = new Empresas();
        Empresas guardada = new Empresas();
        EmpresasResponse response = new EmpresasResponse();

        when(empresasRepository.existsByCodigo("EMP001"))
                .thenReturn(false);

        when(empresasRepository.existsByRut("76.123.456-7"))
                .thenReturn(false);

        when(empresasMapper.toEntity(request))
                .thenReturn(empresas);

        when(empresasRepository.save(empresas))
                .thenReturn(guardada);

        when(empresasMapper.toResponse(guardada))
                .thenReturn(response);

        assertEquals(
                response,
                service.crear(request)
        );
    }

    @Test
    void crearCodigoDuplicado() {

        EmpresasRegistroRequest request =
                new EmpresasRegistroRequest();

        request.setCodigo("EMP001");

        when(empresasRepository.existsByCodigo("EMP001"))
                .thenReturn(true);

        assertThrows(
                EmpresaDuplicadaException.class,
                () -> service.crear(request)
        );
    }

    @Test
    void crearRutDuplicado() {

        EmpresasRegistroRequest request =
                new EmpresasRegistroRequest();

        request.setCodigo("EMP001");
        request.setRut("76.123.456-7");

        when(empresasRepository.existsByCodigo("EMP001"))
                .thenReturn(false);

        when(empresasRepository.existsByRut("76.123.456-7"))
                .thenReturn(true);

        assertThrows(
                EmpresaDuplicadaException.class,
                () -> service.crear(request)
        );
    }

    @Test
    void listarTodos() {

        Empresas empresas = new Empresas();

        when(empresasRepository.findAll())
                .thenReturn(List.of(empresas));

        when(empresasMapper.toResponse(any()))
                .thenReturn(new EmpresasResponse());

        assertEquals(
                1,
                service.listarTodos().size()
        );
    }

    @Test
    void obtenerPorIdEncontrada() {

        Empresas empresas = new Empresas();
        EmpresasResponse response = new EmpresasResponse();

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(empresas));

        when(empresasMapper.toResponse(empresas))
                .thenReturn(response);

        assertEquals(
                response,
                service.obtenerPorId(1L)
        );
    }

    @Test
    void obtenerPorIdNoEncontrada() {

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmpresaNoEncontradaException.class,
                () -> service.obtenerPorId(1L)
        );
    }

    @Test
    void obtenerPorCodigoEncontrada() {

        Empresas empresas = new Empresas();
        EmpresasResponse response = new EmpresasResponse();

        when(empresasRepository.findByCodigo("EMP001"))
                .thenReturn(Optional.of(empresas));

        when(empresasMapper.toResponse(empresas))
                .thenReturn(response);

        assertEquals(
                response,
                service.obtenerPorCodigo("EMP001")
        );
    }

    @Test
    void obtenerPorCodigoNoEncontrada() {

        when(empresasRepository.findByCodigo("EMP001"))
                .thenReturn(Optional.empty());

        assertThrows(
                EmpresaNoEncontradaException.class,
                () -> service.obtenerPorCodigo("EMP001")
        );
    }

    @Test
    void obtenerPorRutEncontrada() {

        Empresas empresas = new Empresas();
        EmpresasResponse response = new EmpresasResponse();

        when(empresasRepository.findByRut("76.123.456-7"))
                .thenReturn(Optional.of(empresas));

        when(empresasMapper.toResponse(empresas))
                .thenReturn(response);

        assertEquals(
                response,
                service.obtenerPorRut("76.123.456-7")
        );
    }

    @Test
    void obtenerPorRutNoEncontrada() {

        when(empresasRepository.findByRut("76.123.456-7"))
                .thenReturn(Optional.empty());

        assertThrows(
                EmpresaNoEncontradaException.class,
                () -> service.obtenerPorRut("76.123.456-7")
        );
    }

    @Test
    void actualizar() {

        EmpresasRegistroRequest request =
                new EmpresasRegistroRequest();

        request.setCodigo("EMP001");
        request.setRut("76.123.456-7");

        Empresas empresas = new Empresas();
        empresas.setId(1L);

        Empresas guardada = new Empresas();
        EmpresasResponse response = new EmpresasResponse();

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(empresas));

        when(empresasRepository.findByCodigo("EMP001"))
                .thenReturn(Optional.empty());

        when(empresasRepository.findByRut("76.123.456-7"))
                .thenReturn(Optional.empty());

        when(empresasRepository.save(empresas))
                .thenReturn(guardada);

        when(empresasMapper.toResponse(guardada))
                .thenReturn(response);

        assertEquals(
                response,
                service.actualizar(1L, request)
        );
    }

    @Test
    void actualizarNoEncontrada() {

        EmpresasRegistroRequest request =
                new EmpresasRegistroRequest();

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmpresaNoEncontradaException.class,
                () -> service.actualizar(1L, request)
        );
    }

    @Test
    void eliminar() {

        Empresas empresas = new Empresas();

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(empresas));

        service.eliminar(1L);
    }

    @Test
    void eliminarNoEncontrada() {

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmpresaNoEncontradaException.class,
                () -> service.eliminar(1L)
        );
    }
}
