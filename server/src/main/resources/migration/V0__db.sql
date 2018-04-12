CREATE USER datalogger WITH PASSWORD 'datalogger';

CREATE DATABASE datalogger
WITH
OWNER = datalogger
ENCODING = 'UTF8'
  --LC_COLLATE = 'en_US.utf8'
  --LC_CTYPE = 'en_US.utf8'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;