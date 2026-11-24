package com.pagoflex.usuarios.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Misma implementacion que {@code PasswordHasher} de la app Android:
 * PBKDF2WithHmacSHA256, 120000 iteraciones, 256 bits y Base64 sin saltos
 * de linea. Asi los hashes creados en la app son verificables aqui y
 * viceversa.
 */
@Component
public class PasswordHasher {

    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generarSalt() {
        byte[] bytes = new byte[SALT_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String hash(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    Base64.getDecoder().decode(salt),
                    ITERATIONS,
                    KEY_LENGTH
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException("No se pudo generar el hash de la contraseña", ex);
        }
    }

    public boolean verificar(String password, String salt, String hash) {
        if (password == null || salt == null || hash == null) {
            return false;
        }
        return MessageDigest.isEqual(
                hash(password, salt).getBytes(StandardCharsets.UTF_8),
                hash.getBytes(StandardCharsets.UTF_8)
        );
    }

    /** Devuelve la lista de incumplimientos; vacia si la contraseña es valida. */
    public List<String> validar(String password) {
        List<String> errores = new ArrayList<>();
        if (password == null || password.length() < 8) {
            errores.add("Debe tener al menos 8 caracteres");
        }
        if (password == null || password.chars().noneMatch(Character::isUpperCase)) {
            errores.add("Debe incluir al menos una letra mayúscula");
        }
        if (password == null || password.chars().noneMatch(Character::isLowerCase)) {
            errores.add("Debe incluir al menos una letra minúscula");
        }
        if (password == null || password.chars().noneMatch(Character::isDigit)) {
            errores.add("Debe incluir al menos un número");
        }
        if (password == null || password.chars().allMatch(Character::isLetterOrDigit)) {
            errores.add("Debe incluir al menos un carácter especial");
        }
        return errores;
    }
}
