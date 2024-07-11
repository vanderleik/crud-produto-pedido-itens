CREATE TABLE IF NOT EXISTS tborderitem (
    idorderitem UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idorder UUID NOT NULL,
    idproduct UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    total NUMERIC(19,2),
    dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT,
    CONSTRAINT fk_order FOREIGN KEY (idorder) REFERENCES tborder(idorder),
    CONSTRAINT fk_product FOREIGN KEY (idproduct) REFERENCES tbproduct(idproduct)
);
