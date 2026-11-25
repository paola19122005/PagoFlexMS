package com.pagoflex.empresas.mapper;

import org.springframework.stereotype.Component;

import com.pagoflex.empresas.dto.request.EmpresasRegistroRequest;
import com.pagoflex.empresas.dto.response.EmpresasResponse;
import com.pagoflex.empresas.entity.Empresas;

@Component
public class EmpresasMapper {

    public Empresas toEntity(EmpresasRegistroRequest request) {

        Empresas empresas = new Empresas();

        empresas.setCodigo(request.getCodigo());
        empresas.setRazonSocial(request.getRazonSocial());
        empresas.setNombreFantasia(request.getNombreFantasia());
        empresas.setRut(request.getRut());
        empresas.setRubro(request.getRubro());
        empresas.setEmail(request.getEmail());
        empresas.setTelefono(request.getTelefono());
        empresas.setRecargo(request.getRecargo());

        return empresas;
    }

    public EmpresasResponse toResponse(Empresas empresas) {

        EmpresasResponse response = new EmpresasResponse();

        response.setId(empresas.getId());
        response.setCodigo(empresas.getCodigo());
        response.setRazonSocial(empresas.getRazonSocial());
        response.setNombreFantasia(empresas.getNombreFantasia());
        response.setRut(empresas.getRut());
        response.setRubro(empresas.getRubro());
        response.setEmail(empresas.getEmail());
        response.setTelefono(empresas.getTelefono());
        response.setRecargo(empresas.getRecargo());

        return response;
    }

    public void actualizarEntity(
            Empresas empresas,
            EmpresasRegistroRequest request) {
        
        empresas.setCodigo(request.getCodigo());
        empresas.setRazonSocial(request.getRazonSocial());
        empresas.setNombreFantasia(request.getNombreFantasia());
        empresas.setRut(request.getRut());
        empresas.setRubro(request.getRubro());
        empresas.setEmail(request.getEmail());
        empresas.setTelefono(request.getTelefono());
        empresas.setRecargo(request.getRecargo());
    }
}
