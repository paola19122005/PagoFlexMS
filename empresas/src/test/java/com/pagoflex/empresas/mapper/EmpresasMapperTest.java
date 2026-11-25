package com.pagoflex.empresas.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pagoflex.empresas.dto.request.EmpresasRegistroRequest;
import com.pagoflex.empresas.dto.response.EmpresasResponse;
import com.pagoflex.empresas.entity.Empresas;

class EmpresasMapperTest {
    @Test
    void toEntity() {

        EmpresasRegistroRequest request = new EmpresasRegistroRequest();

        request.setCodigo("EMP001");
        request.setRazonSocial("PagoFlex Servicios SpA");
        request.setNombreFantasia("PagoFlex");
        request.setRut("76.123.456-7");
        request.setRubro("Servicios Financieros");
        request.setEmail("contacto@pagoflex.cl");
        request.setTelefono("+56912345678");
        request.setRecargo(5);

        EmpresasMapper mapper = new EmpresasMapper();

        Empresas empresas = mapper.toEntity(request);

        assertEquals("EMP001", empresas.getCodigo());
        assertEquals("PagoFlex Servicios SpA", empresas.getRazonSocial());
        assertEquals("PagoFlex", empresas.getNombreFantasia());
        assertEquals("76.123.456-7", empresas.getRut());
        assertEquals("Servicios Financieros", empresas.getRubro());
        assertEquals("contacto@pagoflex.cl", empresas.getEmail());
        assertEquals("+56912345678", empresas.getTelefono());
        assertEquals(5, empresas.getRecargo());
    }

    @Test
    void toResponse() {

        Empresas empresa = new Empresas();

        empresa.setId(1L);
        empresa.setCodigo("EMP001");
        empresa.setRazonSocial("PagoFlex Servicios SpA");
        empresa.setNombreFantasia("PagoFlex");
        empresa.setRut("76.123.456-7");
        empresa.setRubro("Servicios Financieros");
        empresa.setEmail("contacto@pagoflex.cl");
        empresa.setTelefono("+56912345678");
        empresa.setRecargo(5);

        EmpresasMapper mapper = new EmpresasMapper();

        EmpresasResponse response = mapper.toResponse(empresa);

        assertEquals(1L, response.getId());
        assertEquals("EMP001", response.getCodigo());
        assertEquals("PagoFlex Servicios SpA", response.getRazonSocial());
        assertEquals("PagoFlex", response.getNombreFantasia());
        assertEquals("76.123.456-7", response.getRut());
        assertEquals("Servicios Financieros", response.getRubro());
        assertEquals("contacto@pagoflex.cl", response.getEmail());
        assertEquals("+56912345678", response.getTelefono());
        assertEquals(5, response.getRecargo());
    }

    @Test
    void actualizarEntity() {

        Empresas empresa = new Empresas();

        empresa.setCodigo("ANTIGUO");
        empresa.setNombreFantasia("Nombre antiguo");

        EmpresasRegistroRequest request =
                new EmpresasRegistroRequest();

        request.setCodigo("EMP002");
        request.setRazonSocial("Empresa Nueva SpA");
        request.setNombreFantasia("Empresa Nueva");
        request.setRut("77.123.456-8");
        request.setRubro("Tecnología");
        request.setEmail("nueva@empresa.cl");
        request.setTelefono("+56987654321");
        request.setRecargo(10);

        EmpresasMapper mapper = new EmpresasMapper();

        mapper.actualizarEntity(empresa, request);

        assertEquals("EMP002", empresa.getCodigo());
        assertEquals("Empresa Nueva SpA", empresa.getRazonSocial());
        assertEquals("Empresa Nueva", empresa.getNombreFantasia());
        assertEquals("77.123.456-8", empresa.getRut());
        assertEquals("Tecnología", empresa.getRubro());
        assertEquals("nueva@empresa.cl", empresa.getEmail());
        assertEquals("+56987654321", empresa.getTelefono());
        assertEquals(10, empresa.getRecargo());
    }
}
