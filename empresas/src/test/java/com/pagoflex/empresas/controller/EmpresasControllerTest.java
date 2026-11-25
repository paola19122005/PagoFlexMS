package com.pagoflex.empresas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pagoflex.empresas.dto.request.EmpresasRegistroRequest;
import com.pagoflex.empresas.dto.response.EmpresasResponse;
import com.pagoflex.empresas.service.EmpresasService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EmpresasControllerTest {

    @Mock
    private EmpresasService empresasService;

    @InjectMocks
    private EmpresasController controller;

    @Test
    void crear() throws Exception {

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();
        request.setCodigo("EMP001");
        request.setRazonSocial("Empresa S.A.");
        request.setNombreFantasia("Mi Empresa");
        request.setRut("76.123.456-7");
        request.setRubro("Tecnologia");
        request.setEmail("contacto@empresa.com");
        request.setTelefono("+56912345678");
        request.setRecargo(0);

        EmpresasResponse response = new EmpresasResponse();
        response.setId(1L);
        response.setCodigo("EMP001");
        response.setRazonSocial("Empresa S.A.");
        response.setNombreFantasia("Mi Empresa");
        response.setRut("76.123.456-7");
        response.setRubro("Tecnologia");
        response.setEmail("contacto@empresa.com");
        response.setTelefono("+56912345678");
        response.setRecargo(0);

        ResponseEntity<EmpresasResponse> serviceResult = 
            ResponseEntity.status(HttpStatus.CREATED).body(response);
        
        when(empresasService.crear(any())).thenReturn(serviceResult.getBody());

        ResponseEntity<EmpresasResponse> result = controller.crear(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("EMP001", result.getBody().getCodigo());
    }

    @Test
    void listarTodos() {

        EmpresasResponse response1 = new EmpresasResponse();
        response1.setId(1L);
        response1.setCodigo("EMP001");
        
        EmpresasResponse response2 = new EmpresasResponse();
        response2.setId(2L);
        response2.setCodigo("EMP002");

        List<EmpresasResponse> body = List.of(response1, response2);
        when(empresasService.listarTodos()).thenReturn(body);

        List<EmpresasResponse> result = controller.listarTodos();

        assertEquals(2, result.size());
    }

    @Test
    void obtenerPorId() {

        EmpresasResponse response = new EmpresasResponse();
        response.setId(1L);
        response.setCodigo("EMP001");

        when(empresasService.obtenerPorId(1L))
                .thenReturn(response);

        EmpresasResponse result = controller.obtenerPorId(1L);
        
        assertEquals(EmpresasResponse.class, result.getClass());
        assertEquals(1L, response.getId());
    }

    @Test
    void obtenerPorCodigo() {

        EmpresasResponse response = new EmpresasResponse();
        response.setId(1L);
        response.setCodigo("EMP001");

        when(empresasService.obtenerPorCodigo("EMP001"))
                .thenReturn(response);

        EmpresasResponse result = controller.obtenerPorCodigo("EMP001");
        
        assertEquals(EmpresasResponse.class, result.getClass());
        assertEquals("EMP001", response.getCodigo());
    }

    @Test
    void obtenerPorRut() {

        EmpresasResponse response = new EmpresasResponse();
        response.setId(1L);
        response.setRut("76.123.456-7");

        when(empresasService.obtenerPorRut("76.123.456-7"))
                .thenReturn(response);

        EmpresasResponse result = controller.obtenerPorRut("76.123.456-7");
        
        assertEquals(EmpresasResponse.class, result.getClass());
        assertEquals("76.123.456-7", response.getRut());
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

        EmpresasResponse response = new EmpresasResponse();
        response.setId(1L);
        response.setCodigo("EMP001");

        when(
                empresasService.actualizar(
                        eq(1L),
                        any()
                )
        ).thenReturn(response);

        EmpresasResponse result = controller.actualizar(
                1L,
                request
        );

        assertEquals(EmpresasResponse.class, result.getClass());
        assertEquals("EMP001", result.getCodigo());
    }

    @Test
    void eliminar() {

        ResponseEntity<Void> result = controller.eliminar(1L);

        assertEquals(
                HttpStatus.NO_CONTENT,
                result.getStatusCode()
        );
    }
}
