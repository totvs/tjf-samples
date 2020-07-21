CREATE TABLE person
(
   id VARCHAR (36) NOT NULL,
   data JSONB NOT NULL,
   metadata JSONB NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE familytree
(
   id VARCHAR (36) NOT NULL,
   data JSONB NOT NULL,
   metadata JSONB NOT NULL,
   PRIMARY KEY (id)
);
