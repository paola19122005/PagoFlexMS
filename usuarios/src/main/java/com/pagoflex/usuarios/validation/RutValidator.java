package com.pagoflex.usuarios.validation;

/**
 * Validacion de RUT chileno (modulo 11). Acepta el cuerpo con o sin puntos
 * y con o sin guion: "12.345.678-5", "12345678-5" o "123456785".
 */
public final class RutValidator {

    private RutValidator() {
    }

    public static boolean esValido(String rut) {
        if (rut == null) {
            return false;
        }
        String limpio = rut.replace(".", "").replace("-", "").replace(" ", "").toUpperCase();
        if (limpio.length() < 8 || limpio.length() > 9) {
            return false;
        }

        String cuerpo = limpio.substring(0, limpio.length() - 1);
        String digitoVerificador = limpio.substring(limpio.length() - 1);

        if (!cuerpo.matches("\\d+") || !digitoVerificador.matches("[0-9K]")) {
            return false;
        }
        if (cuerpo.chars().allMatch(c -> c == '0')) {
            return false;
        }

        int suma = 0;
        int multiplicador = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }

        int resto = 11 - (suma % 11);
        String esperado = switch (resto) {
            case 11 -> "0";
            case 10 -> "K";
            default -> String.valueOf(resto);
        };
        return esperado.equals(digitoVerificador);
    }
}
