package com.pagoflex.usuarios.entity;

import java.util.List;
import java.util.Locale;

/**
 * Preguntas de seguridad por defecto, replicadas de la app Android
 * ({@code PreguntasSeguridad} en UserEntity.kt).
 */
public final class PreguntasSeguridad {

    public static final String PREGUNTA_DEFAULT = "¿Cuál es el nombre de tu primera mascota?";
    public static final String RESPUESTA_DEFAULT = "perro";

    public static final List<String> LISTA = List.of(
            "¿Cuál es el nombre de tu primera mascota?",
            "¿En qué ciudad naciste?",
            "¿Cuál es el nombre de tu escuela primaria?",
            "¿Cuál es tu comida favorita?",
            "¿Cuál es el segundo nombre de tu madre?"
    );

    private PreguntasSeguridad() {
    }

    /**
     * Normaliza la respuesta de seguridad igual que la app:
     * recorta, colapsa espacios y pasa a minúsculas.
     */
    public static String normalizarRespuesta(String texto) {
        if (texto == null) {
            return RESPUESTA_DEFAULT;
        }
        String normalizada = texto.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
        return normalizada.isEmpty() ? RESPUESTA_DEFAULT : normalizada;
    }
}
