CREATE TABLE IF NOT EXISTS tborderitem (
    idorderitem UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    orderid UUID NOT NULL,
    productid UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    total NUMERIC(19,2),
    dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT,
    CONSTRAINT fk_order FOREIGN KEY (orderid) REFERENCES tborder(idorder),
    CONSTRAINT fk_product FOREIGN KEY (productid) REFERENCES tbproduct(idproduct)
);
