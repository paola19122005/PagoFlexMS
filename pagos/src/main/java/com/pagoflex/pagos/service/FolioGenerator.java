package com.pagoflex.pagos.service;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

/**
 * Genera identificadores cortos y unicos, replicando {@code FolioGenerator}
 * de la app Android.
 */
@Component
public class FolioGenerator {

    public String generarFolioUnico() {
        return "PF-" + marcaTiempo() + "-" + fragmentoAleatorio();
    }

    public String generarNumeroComprobante() {
        return "CPR-" + marcaTiempo() + "-" + fragmentoAleatorio();
    }

    private String marcaTiempo() {
        return Long.toString(System.currentTimeMillis(), 36).toUpperCase(Locale.ROOT);
    }

    private String fragmentoAleatorio() {
        return UUID.randomUUID().toString().replace("-", "")
                .substring(0, 4)
                .toUpperCase(Locale.ROOT);
    }
}
