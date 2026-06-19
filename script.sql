CREATE DATABASE IF NOT EXISTS api_registry_db;
USE api_registry_db;

CREATE TABLE IF NOT EXISTS equipos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    area VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS apis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    version VARCHAR(20) NOT NULL,
    fecha_registro DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    equipo_id INT NOT NULL,
    FOREIGN KEY (equipo_id) REFERENCES equipos(id)
);

CREATE TABLE IF NOT EXISTS endpoints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ruta VARCHAR(255) NOT NULL,
    metodo_http VARCHAR(10) NOT NULL,
    descripcion VARCHAR(255),
    api_id INT NOT NULL,
    FOREIGN KEY (api_id) REFERENCES apis(id)
);

INSERT INTO equipos (nombre, area) VALUES
('Alpha Team', 'Backend'),
('Beta Squad', 'Mobile'),
('Data Hawks', 'Data'),
('Cloud Nine', 'Infraestructura');

INSERT INTO apis (nombre, version, fecha_registro, estado, equipo_id) VALUES
('User Service API', 'v2.1.0', '2026-01-15', 'Activa', 1),
('Payment Gateway API', 'v1.3.2', '2025-11-20', 'Activa', 1),
('Mobile Auth API', 'v3.0.0', '2026-03-01', 'Activa', 2),
('Legacy Reporting API', 'v1.0.0', '2024-06-10', 'Deprecada', 3),
('Analytics Pipeline API', 'v2.0.0', '2026-02-28', 'Activa', 3),
('Cloud Config API', 'v1.1.0', '2025-09-05', 'Activa', 4);

INSERT INTO endpoints (ruta, metodo_http, descripcion, api_id) VALUES
('/users', 'GET', 'Obtener lista de usuarios', 1),
('/users/{id}', 'GET', 'Obtener usuario por ID', 1),
('/users', 'POST', 'Crear nuevo usuario', 1),
('/users/{id}', 'PUT', 'Actualizar usuario', 1),
('/payments', 'POST', 'Procesar un pago', 2),
('/payments/{id}', 'GET', 'Consultar estado de pago', 2),
('/payments/{id}/refund', 'POST', 'Solicitar reembolso', 2),
('/auth/login', 'POST', 'Iniciar sesion movil', 3),
('/auth/refresh', 'POST', 'Refrescar token', 3),
('/auth/logout', 'POST', 'Cerrar sesion', 3),
('/reports/monthly', 'GET', 'Generar reporte mensual', 4),
('/reports/annual', 'GET', 'Generar reporte anual', 4),
('/analytics/events', 'POST', 'Registrar evento', 5),
('/analytics/dashboard', 'GET', 'Obtener metricas del dashboard', 5),
('/config/services', 'GET', 'Listar configuraciones de servicios', 6),
('/config/services/{name}', 'PUT', 'Actualizar configuracion de servicio', 6);
