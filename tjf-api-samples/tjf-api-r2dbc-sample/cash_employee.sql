-- Table: public.cash_employee

-- DROP TABLE public.cash_employee;

CREATE TABLE public.cash_employee
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name varchar(50),
    CONSTRAINT cash_employee_pkey PRIMARY KEY (id)
);

ALTER TABLE public.cash_employee
    OWNER to postgres;
	
CREATE INDEX cash_employee_id_uindex
    ON public.cash_employee(id);
    
    
