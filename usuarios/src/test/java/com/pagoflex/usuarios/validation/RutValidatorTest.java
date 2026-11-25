package com.pagoflex.usuarios.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RutValidatorTest {

    @Test
    void aceptaRutValidoConFormatoCompleto() {
        assertTrue(RutValidator.esValido("11.111.111-1"));
        assertTrue(RutValidator.esValido("12.345.678-5"));
    }

    @Test
    void aceptaRutValidoSinPuntosNiGuion() {
        assertTrue(RutValidator.esValido("123456785"));
    }

    @Test
    void rechazaDigitoVerificadorIncorrecto() {
        assertFalse(RutValidator.esValido("12.222.222-2"));
    }

    @Test
    void rechazaValoresInvalidos() {
        assertFalse(RutValidator.esValido(null));
        assertFalse(RutValidator.esValido(""));
        assertFalse(RutValidator.esValido("abc-def"));
        assertFalse(RutValidator.esValido("0-0"));
    }
}
