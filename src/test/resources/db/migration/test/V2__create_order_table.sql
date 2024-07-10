CREATE TABLE tborder (
     idorder UUID,
     orderdate DATE NOT NULL,
     status VARCHAR(255) NOT NULL,
     grossstotal NUMERIC(19,2),
     discount NUMERIC(19,2) CHECK (discount >= 0),
     nettotal NUMERIC(19,2),
     dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     version BIGINT,
     CONSTRAINT tborder_pk PRIMARY KEY (idorder)
);
