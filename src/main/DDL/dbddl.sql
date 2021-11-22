--MySQL dialect

CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `username` varchar(256) NOT NULL,
    `password` varchar(256) NOT NULL,
    `name` varchar(256) DEFAULT NULL,
    `surname` varchar(256) DEFAULT NULL,
    `age` int DEFAULT NULL,
    `sex` varchar(32) DEFAULT NULL,
    `interests` varchar(2048) DEFAULT NULL,
    `city` varchar(128) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `friends` (
                           `id` int NOT NULL,
                           `friendid` int NOT NULL,
                           PRIMARY KEY (`id`,`friendid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Postgres dialect
CREATE TABLE public.friends
(
    id integer NOT NULL,
    friendid integer NOT NULL,
    CONSTRAINT friends_pkey PRIMARY KEY (id, friendid)
);

CREATE TABLE public.users
(
    id serial NOT NULL,
    username character varying(256) COLLATE pg_catalog."default" NOT NULL,
    password character varying(256) COLLATE pg_catalog."default" NOT NULL,
    name character varying(256) COLLATE pg_catalog."default",
    surname character varying(256) COLLATE pg_catalog."default",
    age integer,
    sex character varying(32) COLLATE pg_catalog."default",
    interests character varying(2048) COLLATE pg_catalog."default",
    city character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
);
