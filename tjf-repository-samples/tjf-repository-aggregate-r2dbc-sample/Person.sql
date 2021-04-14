-- Table: public.person

-- DROP TABLE public.person;

CREATE TABLE public.person
(
    id character varying(36) NOT NULL,
    data jsonb NOT NULL,
    metadata jsonb NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

ALTER TABLE public.person
    OWNER to postgres;
