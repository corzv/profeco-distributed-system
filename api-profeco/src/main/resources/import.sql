INSERT INTO tiendas (nombre, direccion, cadena, tipoestablecimiento) VALUES ('Walmart Centro', 'Calle 200 #100, Cd. Obregón', 'Walmart', 'SUPERMERCADO');
INSERT INTO tiendas (nombre, direccion, cadena, tipoestablecimiento) VALUES ('Soriana Hiper Norte', 'Av. Miguel Alemán 500, Cd. Obregón', 'Soriana', 'SUPERMERCADO');
INSERT INTO tiendas (nombre, direccion, cadena, tipoestablecimiento) VALUES ('Mercado Municipal', 'Calle Guerrero y Allende, Cd. Obregón', NULL, 'MERCADO_POPULAR');

INSERT INTO productos (nombre, marca, categoria, presentacion, codigobarras) VALUES ('Leche entera', 'Lala', 'Lácteos', '1 L', '7501020515343');
INSERT INTO productos (nombre, marca, categoria, presentacion, codigobarras) VALUES ('Huevo blanco', 'Bachoco', 'Huevo', '18 piezas', '7501010714015');
INSERT INTO productos (nombre, marca, categoria, presentacion, codigobarras) VALUES ('Tortilla de maíz', 'Sin marca', 'Tortillería', '1 kg', '0000000000017');
INSERT INTO productos (nombre, marca, categoria, presentacion, codigobarras) VALUES ('Leche deslactosada', 'Alpura', 'Lácteos', '1 L', '7501055300846');

INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (27.50, NOW() - INTERVAL '10 days', 1, 1);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (28.90, NOW() - INTERVAL '1 day', 1, 1);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (29.50, NOW() - INTERVAL '2 days', 2, 1);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (26.00, NOW() - INTERVAL '3 days', 3, 1);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (64.90, NOW() - INTERVAL '1 day', 1, 2);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (61.50, NOW() - INTERVAL '1 day', 2, 2);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (24.00, NOW() - INTERVAL '1 day', 3, 3);
INSERT INTO precios (monto, fecharegistro, tienda_id, producto_id) VALUES (32.90, NOW() - INTERVAL '1 day', 1, 4);

INSERT INTO ofertas (descripcion, preciooferta, fechainicio, fechafin, tienda_id, producto_id) VALUES ('Huevo 18 pzas a precio especial', 55.00, CURRENT_DATE - 2, CURRENT_DATE + 5, 2, 2);
INSERT INTO ofertas (descripcion, preciooferta, fechainicio, fechafin, tienda_id, producto_id) VALUES ('Leche Lala 2x1 (vencida)', 28.90, CURRENT_DATE - 20, CURRENT_DATE - 10, 1, 1);