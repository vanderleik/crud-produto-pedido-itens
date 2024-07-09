CREATE TABLE tbproduct (
   idproduct UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   productname VARCHAR(60) NOT NULL,
   price NUMERIC(19,2) NOT NULL CHECK (price > 0),
   type VARCHAR(255) NOT NULL,
   active BOOLEAN NOT NULL,
   dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   version BIGINT,
   CONSTRAINT productname_not_blank CHECK (productname <> '')
);
