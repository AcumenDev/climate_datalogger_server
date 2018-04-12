INSERT INTO public."user" VALUES (1, 'akum', '$2a$05$IUkJwD49e7CcSW.8JwbdHOqlo5Ape9njHYHtgMPgezv1Df4Dmg.J6', true);

SELECT pg_catalog.setval('public.user_id_seq', 1, true);

INSERT INTO public.sensor VALUES (1, 1, 'температура в зале', 1, 1, NULL, '2018-02-23 17:52:30.075', true, '53ae8ff6-3d8c-11e8-b467-0ed5f89f718b', '2018-02-04 16:58:40.136');

SELECT pg_catalog.setval('public.sensor_id_seq', 1, true);


--INSERT INTO sensor_temperature_settings (a)
