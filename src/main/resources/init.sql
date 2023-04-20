-- Schema TP_Integrador

PRAGMA foreign_keys = ON;

-- Table Resultados

DROP TABLE IF EXISTS Resultados;

CREATE TABLE IF NOT EXISTS Resultados (
id_fase INTEGER NOT NULL,
id_ronda INTEGER NOT NULL,
equipo_1 TEXT NOT NULL,
goles_equipo_1 INTEGER NULL,
equipo_2 TEXT NOT NULL,
goles_equipo_2 INTEGER NULL,
PRIMARY KEY (id_fase, id_ronda, equipo_1, equipo_2)
);

-- Table Pronosticos

DROP TABLE IF EXISTS Pronosticos;

CREATE TABLE IF NOT EXISTS Pronosticos (
id_fase INTEGER NOT NULL,
id_ronda INTEGER NOT NULL,
participante TEXT NOT NULL,
equipo_1 TEXT NOT NULL,
equipo_2 TEXT NOT NULL,
resultado TEXT NULL,
PRIMARY KEY (id_fase, id_ronda, participante, equipo_1, equipo_2),
FOREIGN KEY (id_fase , id_ronda , equipo_1 , equipo_2)
REFERENCES Resultados (id_fase , id_ronda , equipo_1 , equipo_2)
ON DELETE RESTRICT
ON UPDATE CASCADE
);

/*Insertando datos en la tabla Resultados*/

INSERT INTO Resultados (id_fase, id_ronda, equipo_1, goles_equipo_1, goles_equipo_2, equipo_2) VALUES
(1, 1, 'Argentina', 1, 2, 'Arabia Saudita'),
(1, 1, 'Polonia', 0, 0 , 'México'),
(1, 2, 'Argentina', 2, 0, 'México'),
(1, 2, 'Arabia Saudita', 0, 2, 'Polonia'),
(1, 3, 'Argentina', 2, 0, 'Polonia'),
(1, 3, 'Arabia Saudita', 1, 2, 'Mexico'),
(2, 1, 'Argentina', 1, 2, 'Arabia Saudita'),
(2, 1, 'Polonia', 0, 0 , 'México'),
(2, 2, 'Argentina', 2, 0, 'México'),
(2, 2, 'Arabia Saudita', 0, 2, 'Polonia'),
(2, 3, 'Argentina', 2, 0, 'Polonia'),
(2, 3, 'Arabia Saudita', 1, 2, 'Mexico');


INSERT INTO Pronosticos (participante, id_fase, id_ronda, equipo_1, equipo_2, resultado) VALUES
('Mariana', 1, 1, 'Argentina', 'Arabia Saudita', 'Gana1'),
('Mariana', 1, 1, 'Polonia',  'México', 'Empata'), --1
('Mariana', 1, 2, 'Argentina', 'México', 'Gana1'), --1
('Mariana', 1, 2, 'Arabia Saudita', 'Polonia', 'Gana2'), --1
('Mariana', 1, 3, 'Argentina', 'Polonia', 'Empata'),
('Mariana', 1, 3, 'Arabia Saudita', 'Mexico', 'Empata'),
('Mariana', 2, 1, 'Argentina', 'Arabia Saudita', 'Gana1'),
('Mariana', 2, 1, 'Polonia', 'México', 'Empata'), --1
('Mariana', 2, 2, 'Argentina', 'México', 'Gana1'), --1
('Mariana', 2, 2, 'Arabia Saudita', 'Polonia', 'Gana2'), --1
('Mariana', 2, 3, 'Argentina', 'Polonia', 'Empata'),
('Mariana', 2, 3, 'Arabia Saudita', 'Mexico', 'Empata'),
('Pedro', 1, 1, 'Argentina', 'Arabia Saudita', 'Gana1'),
('Pedro', 1, 1, 'Polonia',  'México', 'Empata'), --1
('Pedro', 1, 2, 'Argentina', 'México', 'Gana2'),
('Pedro', 1, 2, 'Arabia Saudita', 'Polonia', 'Gana2'), --1
('Pedro', 1, 3, 'Argentina', 'Polonia', 'Empata'),
('Pedro', 1, 3, 'Arabia Saudita', 'Mexico', 'Gana1'),
('Pedro', 2, 1, 'Argentina', 'Arabia Saudita', 'Gana1'),
('Pedro', 2, 1, 'Polonia', 'México', 'Empata'), --1
('Pedro', 2, 2, 'Argentina', 'México', 'Gana1'), --1
('Pedro', 2, 2, 'Arabia Saudita', 'Polonia', 'Empata'),
('Pedro', 2, 3, 'Argentina', 'Polonia', 'Empata'),
('Pedro', 2, 3, 'Arabia Saudita', 'Mexico', 'Gana2'); --1


