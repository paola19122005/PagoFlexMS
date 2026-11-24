package com.pagoflex.usuarios.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    private final PasswordHasher passwordHasher = new PasswordHasher();

    @Test
    void generaSaltDiferenteCadaVez() {
        String saltUno = passwordHasher.generarSalt();
        String saltDos = passwordHasher.generarSalt();

        assertFalse(saltUno.isBlank());
        assertNotEquals(saltUno, saltDos);
    }

    @Test
    void hashEsDeterministaYVerificable() {
        String salt = passwordHasher.generarSalt();
        String hash = passwordHasher.hash("PagoFlex123!", salt);

        assertEquals(hash, passwordHasher.hash("PagoFlex123!", salt));
        assertTrue(passwordHasher.verificar("PagoFlex123!", salt, hash));
        assertFalse(passwordHasher.verificar("OtraClave1!", salt, hash));
    }

    @Test
    void validaFortalezaDeContrasena() {
        assertTrue(passwordHasher.validar("PagoFlex123!").isEmpty());

        List<String> errores = passwordHasher.validar("debil");
        assertFalse(errores.isEmpty());
        assertTrue(errores.stream().anyMatch(error -> error.contains("8 caracteres")));
    }
}
