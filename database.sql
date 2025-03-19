drop database EntreBicisDB;
create database EntreBicisDB;
use EntreBicisDB;

create table User (
	email varchar (100) primary key,
	nom varchar (30),
	cognoms varchar (80),
	poblacio varchar (30),
	mobil varchar (15),
	rol enum ("ADMIN", "USUARI"),
    saldo double,
    image longblob,
    observacions mediumblob
);

create table Ruta (
  id_ruta BIGINT primary key,
  email_user varchar(100),
  data_inici datetime,
  data_fi datetime,
  distància_km double,
  velocitat_mitjana double,
  temps_total time,
  validada BOOLEAN,
  saldo_generat double,
  estat enum ("iniciada", "finalitzada"),
	  foreign key (email_user) references User(email)
);

create table Posicio_GPS (
  id_punt BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_ruta BIGINT,
  latitud DECIMAL(10, 6) NOT NULL,
  longitud DECIMAL(10, 6) NOT NULL,
  timestamp DATETIME NOT NULL,
  FOREIGN KEY (id_ruta) REFERENCES Ruta(id_ruta) ON DELETE CASCADE
);

create table Recompensa (
	id_recompensa BIGINT PRIMARY KEY AUTO_INCREMENT,
	email_user varchar (100),
    preu double,
    nom varchar (50),
    direccio varchar (150),
    descripcio varchar (255),
    data_solicitud DATETIME,
    data_recollida DATETIME,
    estat ENUM("disponible", "solicitada", "reservada", "assignada", "recollida"),
	FOREIGN KEY (email_user) REFERENCES User(email) ON DELETE CASCADE
);

create table Recompensa_images (
	id_image BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_recompensa BIGINT not null,
    data longblob,
    FOREIGN KEY (id_recompensa) REFERENCES Recompensa(id_recompensa)
);

create table System_parameters (
	name_config varchar (20) default "default" primary key,
    mitja_velocitat_valid int default 25,
    max_inactivitat int default 300,
    punts_per_kilometre int default 100,
    temps_recollida int default 72
);


