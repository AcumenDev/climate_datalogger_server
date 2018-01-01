CREATE USER datalogger;

CREATE DATABASE datalogger
WITH
OWNER = datalogger
ENCODING = 'UTF8'
LC_COLLATE = 'en_US.utf8'
LC_CTYPE = 'en_US.utf8'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;

CREATE SEQUENCE public.sensor_readings_id_seq
  INCREMENT 1
  START 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  CACHE 1;
ALTER TABLE public.sensor_readings_id_seq
  OWNER TO datalogger;

CREATE TABLE public.sensor_readings
(
  id        BIGINT                                              NOT NULL DEFAULT nextval(
      'sensor_readings_id_seq' :: REGCLASS),
  login     CHARACTER VARYING(250) COLLATE pg_catalog."default" NOT NULL,
  room      INTEGER                                             NOT NULL,
  num       INTEGER                                             NOT NULL,
  type      INTEGER                                             NOT NULL,
  value     DOUBLE PRECISION                                    NOT NULL,
  date_time TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
  CONSTRAINT sensor_readings_pkey PRIMARY KEY (id)
);
ALTER TABLE public.sensor_readings
  OWNER TO datalogger;

CREATE TABLE public.sensor
(
  login                 CHARACTER VARYING(250) COLLATE pg_catalog."default" NOT NULL,
  room                  INTEGER                                             NOT NULL,
  num                   INTEGER                                             NOT NULL,
  type                  INTEGER                                             NOT NULL,
  last_active_date_time TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
  CONSTRAINT sensor_pkey PRIMARY KEY (login, room, num, type)
);
ALTER TABLE public.sensor
  OWNER TO datalogger;