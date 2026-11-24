package com.pagoflex.empresas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
public class EmpresasControllerTest {

    @Mock
    private EmpresasService empresasService;

    @InjectMocks
    private EmpresasController controller;

    @Test
    void crear() {

        when(empresasService.crear(any()))
                .thenReturn(new EmpresasResponse());

        assertEquals(
                201,
                controller.crear(
                        new EmpresasRegistroRequest()
                ).getStatusCode().value()
        );
    }

    @Test
    void listarTodos() {

        when(empresasService.listarTodos())
                .thenReturn(
                        List.of(new EmpresasResponse())
                );

        assertEquals(
                1,
                controller.listarTodos().size()
        );
    }

    @Test
    void obtenerPorId() {

        when(empresasService.obtenerPorId(1L))
                .thenReturn(new EmpresasResponse());

        controller.obtenerPorId(1L);
    }

    @Test
    void obtenerPorCodigo() {

        when(empresasService.obtenerPorCodigo("EMP001"))
                .thenReturn(new EmpresasResponse());

        controller.obtenerPorCodigo("EMP001");
    }

    @Test
    void obtenerPorRut() {

        when(empresasService.obtenerPorRut("76.123.456-7"))
                .thenReturn(new EmpresasResponse());

        controller.obtenerPorRut("76.123.456-7");
    }

    @Test
    void actualizar() {

        when(
                empresasService.actualizar(
                        eq(1L),
                        any()
                )
        ).thenReturn(new EmpresasResponse());

        controller.actualizar(
                1L,
                new EmpresasRegistroRequest()
        );
    }

    @Test
    void eliminar() {

        assertEquals(
                204,
                controller.eliminar(1L)
                        .getStatusCode()
                        .value()
        );
    }
}
