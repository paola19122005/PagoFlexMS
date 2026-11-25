package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.response.CompromisoResponse;

public record CompromisoRegistroResultado(boolean creado, CompromisoResponse respuesta) {
}
