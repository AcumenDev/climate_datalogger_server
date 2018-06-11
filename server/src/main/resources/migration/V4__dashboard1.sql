CREATE TABLE public.dashboard (
 dashboard_id       BIGSERIAL PRIMARY KEY                                  NOT NULL,
 user_id       BIGINT REFERENCES public.user (id)                               NOT NULL,
  name    CHARACTER VARYING(250) COLLATE pg_catalog."default"    NOT NULL,
 
);
ALTER TABLE public.dashboard
  OWNER TO datalogger;






CREATE TABLE public.dashboard_item (
   dashboard_id   BIGINT REFERENCES public.dashboard (id)                               NOT NULL,
   data  character varying COLLATE pg_catalog."default"    NOT NULL,

);