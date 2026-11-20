CREATE DATABASE IF NOT EXISTS agente_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE agente_db;

CREATE TABLE IF NOT EXISTS conversaciones (
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_usuario BIGINT NOT NULL,
    fecha_creacion VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    PRIMARY KEY (id),
    INDEX idx_conversaciones_usuario (id_usuario),
    INDEX idx_conversaciones_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS mensajes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_conversacion BIGINT NOT NULL,
    id_remitente BIGINT NOT NULL,
    rol_remitente VARCHAR(20) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_hora VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_mensajes_conversacion (id_conversacion),
    CONSTRAINT fk_mensajes_conversacion
        FOREIGN KEY (id_conversacion) REFERENCES conversaciones (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
