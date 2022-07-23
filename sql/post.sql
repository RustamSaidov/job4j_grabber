CREATE TABLE public.post
(
   id serial primary key,
   name character varying(255),
   text character varying,
   link character varying UNIQUE,
   created timestamptz
);