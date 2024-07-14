CREATE TABLE IF NOT EXISTS tbcatalogitem (
   catalogitemid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   catalogitemname VARCHAR(60) NOT NULL,
   catalogitemdescription VARCHAR(60) NOT NULL,
   catalogitemnumber VARCHAR(60),
   price NUMERIC(19,2) NOT NULL CHECK (price > 0),
   type VARCHAR(255) NOT NULL,
   isactive BOOLEAN NOT NULL,
   dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   version BIGINT,
   CONSTRAINT tbcatalogitemname_not_blank CHECK (catalogitemname <> '')
);
