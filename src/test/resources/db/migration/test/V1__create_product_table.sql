CREATE TABLE tbproduct (
    idproduct UUID,
    productname VARCHAR(60) NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT,
    CONSTRAINT tbproduct_pk PRIMARY KEY (idproduct)
);
