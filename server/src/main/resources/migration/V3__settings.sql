ALTER TABLE public.sensor_temperature_settings
  ADD date_time timestamp WITHOUT TIME ZONE DEFAULT now() NOT NULL;