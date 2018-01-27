CREATE USER datalogger WITH PASSWORD 'datalogger';

CREATE DATABASE datalogger
WITH
OWNER = datalogger
ENCODING = 'UTF8'
  --LC_COLLATE = 'en_US.utf8'
  --LC_CTYPE = 'en_US.utf8'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;

SET search_path = datalogger;


CREATE TABLE public.user (
  id       BIGSERIAL PRIMARY KEY                                  NOT NULL,
  login    CHARACTER VARYING(250) COLLATE pg_catalog."default"    NOT NULL,
  password CHARACTER VARYING(250) COLLATE pg_catalog."default"    NOT NULL,
  state    BOOLEAN                                                NOT NULL DEFAULT FALSE
);
ALTER TABLE public.user
  OWNER TO datalogger;


CREATE TABLE public.sensor
(
  id                    BIGSERIAL PRIMARY KEY                                  NOT NULL,
  user_id               BIGINT REFERENCES public.user (id)                     NOT NULL,
  name                  CHARACTER VARYING(250) COLLATE pg_catalog."default"    NOT NULL,
  num                   INTEGER                                                NOT NULL,
  type                  INTEGER                                                NOT NULL,
  description           TEXT                                                   NULL,
  last_active_date_time TIMESTAMP WITHOUT TIME ZONE                            NULL,
  state                 BOOLEAN                                                NOT NULL,
  api_key               CHARACTER VARYING(250) COLLATE pg_catalog."default"    NOT NULL
);
ALTER TABLE public.sensor
  OWNER TO datalogger;


CREATE TABLE public.sensor_temperature_settings
(
  sensor_id    BIGINT PRIMARY KEY REFERENCES public.sensor (id)                             NOT NULL,
  target       REAL                                                                         NOT NULL,
  gisteris     REAL                                                                         NOT NULL,
  tuningSensor REAL                                                                         NOT NULL
);
ALTER TABLE public.sensor_temperature_settings
  OWNER TO datalogger;


CREATE TABLE public.sensor_temperature_readings
(
  user_id       BIGINT REFERENCES public.user (id)                               NOT NULL,
  sensor_id     BIGINT REFERENCES public.sensor (id)                             NOT NULL,
  value         REAL                                                             NOT NULL,
  heating_state BOOLEAN                                                          NOT NULL,
  cooling_state BOOLEAN                                                          NOT NULL,
  date_time     TIMESTAMP WITHOUT TIME ZONE                                      NOT NULL
);
ALTER TABLE public.sensor_temperature_readings
  OWNER TO datalogger;