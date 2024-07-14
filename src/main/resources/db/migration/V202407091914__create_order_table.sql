CREATE TABLE IF NOT EXISTS tborder (
     idorder UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     ordernumber VARCHAR(30) NOT NULL,
     orderdate DATE NOT NULL,
     status VARCHAR(255) NOT NULL,
     grosstotal NUMERIC(19,2),
     discount NUMERIC(19,2) CHECK (discount >= 0),
     nettotal NUMERIC(19,2),
     dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     version BIGINT
);
