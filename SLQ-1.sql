-- Tabla CompraComida
CREATE TABLE CompraComida (
    IdUnico INT AUTO_INCREMENT PRIMARY KEY,
    NombreProducto VARCHAR(255) NOT NULL,
    Descripcion VARCHAR(455) NOT NULL,
    Foto BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si tiene foto (SI/NO)
    NumeroUnicoFoto INT NULL, -- Número único si Foto es SI
    Cantidad INT NOT NULL,
    Realizado BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si la compra fue realizada
    CONSTRAINT chk_Foto_Comida CHECK (Foto = 0 OR NumeroUnicoFoto IS NOT NULL) -- Validación lógica
);

-- Tabla CompraLimpieza
CREATE TABLE CompraLimpieza (
    IdUnico INT AUTO_INCREMENT PRIMARY KEY,
    NombreProducto VARCHAR(255) NOT NULL,
    Descripcion VARCHAR(455) NOT NULL,
    Foto BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si tiene foto (SI/NO)
    NumeroUnicoFoto INT NULL, -- Número único si Foto es SI
    Cantidad INT NOT NULL,
    Realizado BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si la compra fue realizada
    CONSTRAINT chk_Foto_Limpieza CHECK (Foto = 0 OR NumeroUnicoFoto IS NOT NULL) -- Validación lógica
);

-- Tabla CompraVarios
CREATE TABLE CompraVarios (
    IdUnico INT AUTO_INCREMENT PRIMARY KEY,
    NombreProducto VARCHAR(255) NOT NULL,
    Descripcion VARCHAR(455) NOT NULL,
    Foto BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si tiene foto (SI/NO)
    NumeroUnicoFoto INT NULL, -- Número único si Foto es SI
    Cantidad INT NOT NULL,
    Realizado BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si la compra fue realizada
    CONSTRAINT chk_Foto_Varios CHECK (Foto = 0 OR NumeroUnicoFoto IS NOT NULL) -- Validación lógica
);

-- Tabla Tarea para las relaciones
CREATE TABLE Tarea (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    IDCompraComida INT NULL,
    IDCompraLimpieza INT NULL,
    IDCompraVarios INT NULL,
    FOREIGN KEY (IDCompraComida) REFERENCES CompraComida(IdUnico) ON DELETE CASCADE,
    FOREIGN KEY (IDCompraLimpieza) REFERENCES CompraLimpieza(IdUnico) ON DELETE CASCADE,
    FOREIGN KEY (IDCompraVarios) REFERENCES CompraVarios(IdUnico) ON DELETE CASCADE
);
