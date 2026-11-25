package com.pagoflex.empresas.data;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pagoflex.empresas.entity.Empresas;
import com.pagoflex.empresas.repository.EmpresasRepository;

@Component
public class EmpresaSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EmpresaSeeder.class);

    private final EmpresasRepository empresasRepository;
    
    @Value("${empresas.seed-data: false}")
    private boolean seedData;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmpresaSeeder(EmpresasRepository empresasRepository) {
        this.empresasRepository = empresasRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (empresasRepository.count() > 0 || !seedData) {
            return;
        }

        List<Empresas> empresasInicial = crearEmpresasInicial();
        if (empresasInicial.isEmpty()) {
            log.info("No se crearon datos iniciales.");
            return;
        }

        try {
            String jsonEmpresas = objectMapper.writeValueAsString(empresasInicial);
            List<Empresas> guardados = objectMapper.readValue(jsonEmpresas, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Empresas.class));
            
            empresasRepository.saveAll(guardados);
            log.info("Datos iniciales de empresas creados exitosamente.");
        } catch (JsonProcessingException e) {
            log.error("Error al serializar/deserializar datos iniciales", e);
        }
    }

    private List<Empresas> crearEmpresasInicial() throws JsonProcessingException {
        // Lista de empresas iniciales - puedes personalizarla según tus necesidades
        String json = "[" +
            "  {" +
            "    \"codigo\": \"EMP001\"," +
            "    \"razonSocial\": \"Empresa Ejemplo S.A.\"," +
            "    \"nombreFantasia\": \"Ejemplos\"," +
            "    \"rut\": \"70.123.456-9\"," +
            "    \"rubro\": \"Tecnología\"," +
            "    \"email\": \"contacto@ejemplo.cl\"," +
            "    \"telefono\": \"56-2-2222222\"," +
            "    \"recargo\": 0" +
            "  }," +
            "  {" +
            "    \"codigo\": \"EMP002\"," +
            "    \"razonSocial\": \"Comercial Andes Ltda.\"," +
            "    \"nombreFantasia\": \"Andes\"," +
            "    \"rut\": \"80.987.654-3\"," +
            "    \"rubro\": \"Comercio\"," +
            "    \"email\": \"ventas@andes.cl\"," +
            "    \"telefono\": \"56-2-3333333\"," +
            "    \"recargo\": 0" +
            "  }," +
            "  {" +
            "    \"codigo\": \"EMP003\"," +
            "    \"razonSocial\": \"Servicios Digitales Mapocho SpA.\"," +
            "    \"nombreFantasia\": \"Digimapocho\"," +
            "    \"rut\": \"90.654.321-8\"," +
            "    \"rubro\": \"Desarrollo de Software\"," +
            "    \"email\": \"info@mapocho.cl\"," +
            "    \"telefono\": \"56-2-4444444\"," +
            "    \"recargo\": 10" +
            "  }" +
            "]"
        ;

        return objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Empresas.class));
    }
}