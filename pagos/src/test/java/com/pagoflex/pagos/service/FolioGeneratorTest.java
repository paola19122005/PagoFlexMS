package com.pagoflex.pagos.service;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FolioGeneratorTest {

    private final FolioGenerator generador = new FolioGenerator();

    @Test
    void generaFolioUnicoConPrefijoPf() {
        String folio = generador.generarFolioUnico();

        assertTrue(folio.startsWith("PF-"));
        assertTrue(folio.matches("PF-[A-Z0-9]+-[A-Z0-9]{4}"));
        assertTrue(folio.length() <= 40);
    }

    @Test
    void generaFoliosDistintos() {
        Set<String> folios = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            folios.add(generador.generarFolioUnico());
        }

        assertEquals(200, folios.size());
    }

    @Test
    void generaNumeroComprobanteConPrefijoCpr() {
        String numero = generador.generarNumeroComprobante();

        assertTrue(numero.startsWith("CPR-"));
        assertTrue(numero.matches("CPR-[A-Z0-9]+-[A-Z0-9]{4}"));
        assertTrue(numero.length() <= 30);
    }
}
