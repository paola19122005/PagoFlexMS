package com.pagoflex.empresas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
        request.setCodigo("EMP001");
        request.setRazonSocial("Empresa S.A.");
        request.setNombreFantasia("Mi Empresa");
        request.setRut("76.123.456-7");
        request.setRubro("Tecnologia");
        request.setEmail("contacto@empresa.com");
        request.setTelefono("+56912345678");
        request.setRecargo(0);

        Empresas empresas = new Empresas();
        EmpresasResponse response = new EmpresasResponse();

        when(empresasRepository.existsByCodigo("EMP001"))
                .thenReturn(false);

        when(empresasRepository.existsByRut("76.123.456-7"))
                .thenReturn(false);

        when(empresasMapper.toEntity(request))
                .thenReturn(empresas);

        when(empresasRepository.save(empresas))
                .thenReturn(empresas);

        when(empresasMapper.toResponse(empresas))
                .thenReturn(response);

        assertEquals(
                response,
                service.crear(request)
        );
    }

    @Test
    void crearCodigoDuplicado() {

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
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

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
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

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(empresas));

        when(empresasMapper.toResponse(empresas))
                .thenReturn(new EmpresasResponse());

        assertNotNull(
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

        when(empresasRepository.findByCodigo("EMP001"))
                .thenReturn(Optional.of(empresas));

        when(empresasMapper.toResponse(empresas))
                .thenReturn(new EmpresasResponse());

        assertNotNull(
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

        when(empresasRepository.findByRut("76.123.456-7"))
                .thenReturn(Optional.of(empresas));

        when(empresasMapper.toResponse(empresas))
                .thenReturn(new EmpresasResponse());

        assertNotNull(
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

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
        request.setCodigo("EMP001");
        request.setRazonSocial("Empresa Actualizada S.A.");
        request.setNombreFantasia("Mi Empresa Actualizada");
        request.setRut("76.123.456-7");
        request.setRubro("Tecnologia");
        request.setEmail("contacto@empresa.com");
        request.setTelefono("+56912345678");
        request.setRecargo(0);

        Empresas empresas = new Empresas();
        empresas.setId(1L);

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(empresas));

        when(empresasRepository.findByCodigo("EMP001"))
                .thenReturn(Optional.empty());

        when(empresasRepository.findByRut("76.123.456-7"))
                .thenReturn(Optional.empty());

        when(empresasRepository.save(empresas))
                .thenReturn(empresas);

        when(empresasMapper.toResponse(empresas))
                .thenReturn(new EmpresasResponse());

        assertNotNull(
                service.actualizar(1L, request)
        );
    }

    @Test
    void actualizarCodigoDuplicado() {

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
        request.setCodigo("EMP002");

        Empresas empresaExistenteConEseCodigo = new Empresas();
        empresaExistenteConEseCodigo.setId(2L); // ID diferente al que actualizamos (1L)

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(new Empresas()));

        // Stubbing con la empresa existente que tiene ese codigo y un ID diferente
        when(empresasRepository.findByCodigo("EMP002"))
                .thenReturn(Optional.of(empresaExistenteConEseCodigo));

        assertThrows(
                EmpresaDuplicadaException.class,
                () -> service.actualizar(1L, request)
        );
    }

    @Test
    void actualizarRutDuplicado() {

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
        request.setCodigo("EMP001"); // El servicio verifica codigo y rut
        request.setRut("76.123.456-8");

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(new Empresas()));

        when(empresasRepository.findByCodigo("EMP001"))
                .thenReturn(Optional.empty());

        // Stubbing con la empresa existente que tiene ese rut y un ID diferente al que actualizamos (2L != 1L)
        Empresas empresaExistenteConEseRut = new Empresas();
        empresaExistenteConEseRut.setId(2L);
        when(empresasRepository.findByRut("76.123.456-8"))
                .thenReturn(Optional.of(empresaExistenteConEseRut));

        assertThrows(
                EmpresaDuplicadaException.class,
                () -> service.actualizar(1L, request)
        );
    }

    @Test
    void actualizarNoEncontrada() {

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmpresaNoEncontradaException.class,
                () -> service.actualizar(1L, new EmpresasRegistroRequest())
        );
    }

    @Test
    void eliminar() {

        when(empresasRepository.findById(1L))
                .thenReturn(Optional.of(new Empresas()));

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
