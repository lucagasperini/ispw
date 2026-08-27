CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE "Restaurant" (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    crtime timestamp without time zone NOT NULL DEFAULT now(),
    name varchar(256) NOT NULL,
    phone varchar(16) NOT NULL,
    address varchar(256) NOT NULL,
    city varchar(256) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE "User" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    email CITEXT NOT NULL,
    password char(64) NOT NULL,
    firstname varchar(256) NOT NULL,
    lastname varchar(256) NOT NULL,
    type varchar(16) NOT NULL,
    fk_restaurant uuid,
    FOREIGN KEY (fk_restaurant) REFERENCES "Restaurant" (id),
    unique(email)
);

CREATE TABLE "Dish" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    type varchar(16) NOT NULL,
    name CITEXT NOT NULL,
    description varchar(4096) NOT NULL,
    fk_restaurant uuid NOT NULL,
    FOREIGN KEY (fk_restaurant) REFERENCES "Restaurant" (id),
    unique(name, fk_restaurant)
);

CREATE TABLE "Ingredient" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    name CITEXT NOT NULL,
    unique(name)
);

CREATE TABLE "Dish_Ingredient" (
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    fk_ingredient UUID NOT NULL,
    fk_dish UUID NOT NULL,
    cooked BOOLEAN NOT NULL DEFAULT false,
    optional BOOLEAN NOT NULL DEFAULT false,
    unique(fk_dish, fk_ingredient),
    FOREIGN KEY(fk_ingredient) REFERENCES "Ingredient"(id),
    FOREIGN KEY(fk_dish) REFERENCES "Dish"(id)
);

CREATE TABLE "ExcludedGroup" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    name CITEXT NOT NULL,
    unique(name)
);

CREATE TABLE "ExcludedGroup_Ingredient" (
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    fk_ingredient UUID NOT NULL,
    fk_excluded_group UUID NOT NULL,
    FOREIGN KEY(fk_ingredient) REFERENCES "Ingredient"(id),
    FOREIGN KEY(fk_excluded_group) REFERENCES "ExcludedGroup"(id),
    unique(fk_ingredient, fk_excluded_group)
);

CREATE TABLE "User_ExcludedIngredient" (
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    fk_user UUID NOT NULL,
    fk_ingredient UUID NOT NULL,
    FOREIGN KEY(fk_user) REFERENCES "User"(id),
    FOREIGN KEY(fk_ingredient) REFERENCES "Ingredient"(id),
    unique(fk_ingredient, fk_user)
);

CREATE TABLE "User_ExcludedGroup" (
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    fk_user UUID NOT NULL,
    fk_excluded_group UUID NOT NULL,
    FOREIGN KEY(fk_user) REFERENCES "User"(id),
    FOREIGN KEY(fk_excluded_group) REFERENCES "ExcludedGroup"(id),
    unique(fk_excluded_group, fk_user)
);

CREATE TABLE "Allergen" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    name CITEXT NOT NULL,
    unique(name)
);

CREATE TABLE "User_Allergen" (
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    fk_user UUID NOT NULL,
    fk_allergen UUID NOT NULL,
    FOREIGN KEY(fk_user) REFERENCES "User"(id),
    FOREIGN KEY(fk_allergen) REFERENCES "Allergen"(id),
    unique(fk_user, fk_allergen)
);

CREATE TABLE "Ingredient_Allergen" (
    crtime TIMESTAMP NOT NULL DEFAULT NOW(),
    fk_ingredient UUID NOT NULL,
    fk_allergen UUID NOT NULL,
    FOREIGN KEY(fk_ingredient) REFERENCES "Ingredient"(id),
    FOREIGN KEY(fk_allergen) REFERENCES "Allergen"(id),
    unique(fk_ingredient, fk_allergen)
);

-- ### UserRepository ###

CREATE OR REPLACE PROCEDURE userinfo(
IN _email varchar,
OUT _id varchar,
OUT _password varchar,
OUT _type varchar,
OUT _firstname varchar,
OUT _lastname varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT id::varchar, password, type, firstname, lastname
INTO _id, _password, _type, _firstname, _lastname
FROM "User" WHERE email = _email::CITEXT;
END;
$BODY$;

CREATE OR REPLACE PROCEDURE get_user_by_id(
    IN _id varchar,
    OUT _email varchar,
    OUT _password varchar,
    OUT _firstname varchar,
    OUT _lastname varchar,
    OUT _type varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT email, password, firstname, lastname, type
INTO _email, _password, _firstname, _lastname, _type
FROM "User" WHERE id = _id::UUID;
END;
$BODY$;


-- ### RestaurantRepository ###

CREATE OR REPLACE PROCEDURE get_restaurant_by_owner_id(
    IN _user_id varchar,
    OUT _restaurant_id varchar,
    OUT _name varchar,
    OUT _phone varchar,
    OUT _address varchar,
    OUT _city varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN

SELECT fk_restaurant::varchar, name, phone, address, city
INTO _restaurant_id, _name, _phone, _address, _city
FROM "User" AS u JOIN "Restaurant" AS r ON fk_restaurant=r.id
WHERE u.id=_user_id::uuid;

END;
$BODY$;

CREATE OR REPLACE PROCEDURE get_restaurant_by_id(
    IN _id varchar,
    OUT _name varchar,
    OUT _phone varchar,
    OUT _address varchar,
    OUT _city varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT name, phone, address, city
INTO _name,_phone,_address,_city
FROM "Restaurant" WHERE id = _id::uuid;
END;
$BODY$;

-- ### MenuRepository ###

CREATE OR REPLACE PROCEDURE get_dish_id_by_name(
    IN _restaurant_id varchar,
    IN _dish_name varchar,
    OUT _dish_id varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT id::varchar INTO _dish_id
    FROM "Dish" WHERE fk_restaurant = _restaurant_id::uuid AND name = _dish_name::CITEXT;
END;
$BODY$;

CREATE OR REPLACE PROCEDURE get_dish_by_id(
    IN _dish_id varchar,
    OUT _name varchar,
    OUT _description varchar,
    OUT _type varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT name, description, type
    INTO _name, _description, _type
    FROM "Dish" WHERE id = _dish_id::uuid;
END;
$BODY$;

CREATE OR REPLACE PROCEDURE get_dish_by_name(
    IN _restaurant_id varchar,
    IN _dish_id varchar,
    OUT _id varchar,
    OUT _name varchar,
    OUT _description varchar,
    OUT _type varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT id, name, description, type
    INTO _id, _name, _description, _type
    FROM "Dish"
    WHERE fk_restaurant = _restaurant_id::uuid AND name = _name::CITEXT;
END;
$BODY$;

-- ### IngredientRepository ###

CREATE OR REPLACE PROCEDURE get_ingredient_by_name(
    IN _name varchar,
    OUT _id varchar,
    OUT _name_out varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT id, name
    INTO _id, _name_out
    FROM "Ingredient" WHERE name = _name::CITEXT;

END;
$BODY$;


CREATE OR REPLACE PROCEDURE get_excluded_group_by_name(
    IN _name varchar,
    OUT _id varchar,
    OUT _name_out varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT id, name
    INTO _id, _name_out
    FROM "ExcludedGroup" WHERE name = _name::CITEXT;

END;
$BODY$;

CREATE OR REPLACE PROCEDURE get_allergen_by_name(
    IN _name varchar,
    OUT _id varchar,
    OUT _name_out varchar)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    SELECT id, name
    INTO _id, _name_out
    FROM "Allergen" WHERE name = _name::CITEXT;

END;
$BODY$;

INSERT INTO "Restaurant" (name, phone, address, city) VALUES
('La Trattoria del Nonno', '3331234567', 'Via Roma, 10', 'Roma'),
('Pizzeria Antica Forno', '3339876543', 'Piazza Navona, 5', 'Pisa'),
('Osteria del Sole', '3331122334', 'Corso Vittorio Emanuele II, 20', 'Milano'),
('Trattoria Toscana Saporita', '3335678901', 'Via dei Gigli, 45', 'Firenze'),
('Ristorante Mediterraneo', '3332233445', 'Lungarno Torrigiani, 12', 'Pisa'),
('Il Gusto Toscano', '3336677889', 'Via delle Magnolie, 8', 'Siena'),
('Hostaria del Pellegrino', '3337788990', 'Borgo San Lorenzo, 1', 'Firenze'),
('Cucina Etnica Giapponese', '3334567890', 'Via Fiori Chiari, 30', 'Torino'),
('La Dolce Vita Cafè', '3331020304', 'Piazzetta Centrale, 1', 'Venezia'),
('Ristorante del Golfo', '3335566778', 'Molo Vecchio, 5', 'Napoli'),
('Mangia Bene Milano', '3339080706', 'Via Commerciale, 15', 'Milano'),
('Cucina Romana Autentica', '3332345678', 'Viale delle Palme, 55', 'Roma'),
('Agricola di Bergamo', '3336090807', 'Piazza della Vittoria, 3', 'Bergamo'),
('Sushi Master Italia', '3331828384', 'Via San Gregorio, 12', 'Roma'),
('Ristorante del Lago', '3335192021', 'Riviera, 7', 'Como'),
('Antica Osteria di Firenze', '3336384756', 'Via dei Bardi, 9', 'Firenze'),
('Piccola Cucina Calabrese', '3337465890', 'Corso Umberto I, 2', 'Catanzaro'),
('The Tuscan Plate (Tavola Toscana)', '3331909101', 'Via San Frediano, 10', 'Firenze'),
('Ristorante Vesuvio', '3335765432', 'Via Partenope, 80', 'Napoli'),
('Cucina Piemontese', '3339988776', 'Alpeggio Alpino, 1', 'Asti');




INSERT INTO "ExcludedGroup" (name) VALUES
('PREGNANT'),
('VEGAN'),
('VEGETARIAN'),
('KOSHER'),
('HALAL'),
('PESCATARIAN'),
('CARNIVORE');


INSERT INTO "Allergen" (name) VALUES ('Glutine');
INSERT INTO "Allergen" (name) VALUES ('Latticini');
INSERT INTO "Allergen" (name) VALUES ('Uovo');
INSERT INTO "Allergen" (name) VALUES ('Soia');
INSERT INTO "Allergen" (name) VALUES ('Frumento');
INSERT INTO "Allergen" (name) VALUES ('Colture');
INSERT INTO "Allergen" (name) VALUES ('Pesce');
INSERT INTO "Allergen" (name) VALUES ('Crostacei');
INSERT INTO "Allergen" (name) VALUES ('Carne Rossa');
INSERT INTO "Allergen" (name) VALUES ('Leguminose');
INSERT INTO "Allergen" (name) VALUES ('Arachidi');
INSERT INTO "Allergen" (name) VALUES ('Latte');

INSERT INTO "Ingredient" (name) VALUES
('Pomodori'),
('Pasta di Semola'),
('Mozzarella Fresca'),
('Basilico'),
('Manzo Macinato'),
('Patate'),
('Olio Extra Vergine'),
('Sale Marino'),
('Peperoncino'),
('Cipolla Rossa'),
('Aglio'),
('Broccolini'),
('Riso Arborio'),
('Zucchine'),
('Fagioli Borlotti'),
('Tofu'),
('Pane Integrale'),
('Noci'),
('Latte Fresco'),
('Formaggio Grana Padano'),
('Guanciale');

-- PASSWORD: test
INSERT INTO "User" (email, password, firstname, lastname, type, fk_restaurant) VALUES
('mario.rossi@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mario', 'Rossi', 'REST', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('giulia.bianchi@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Giulia', 'Bianchi', 'PICKIE', null),
('luigi.martini@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Luigi', 'Martini', 'ADMIN', null),
('sofia.ferrari@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Sofia', 'Ferrari', 'REST', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('marco.conti@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Marco', 'Conti', 'REST', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('elena.galli@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Elena', 'Galli', 'REST', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('paolo.rizzi@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Paolo', 'Rizzi', 'REST', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('anna.bianchi@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Anna', 'Bianchi', 'PICKIE', null),
('davide.conti@mail.it', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Davide', 'Conti', 'PICKIE', null);
-- Per raggiungere almeno 20 utenti, si dovrebbero estendere queste righe replicando schemi validi.

-- Aggiunta di dischi per soddisfare il requisito minimo (un piatto per ogni tipo) in tutti i ristoranti esistenti.
INSERT INTO "Dish" (type, name, description, fk_restaurant) VALUES
-- DRINK: Beverage
('DRINK', 'Spritz al Campari', 'Bevanda frizzante italiana con prosecco e campari.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('DRINK', 'Chianti Classico', 'Vino rosso toscano robusto, perfetto per accompagnare i piatti regionali.', (SELECT id FROM "Restaurant" WHERE name='Pizzeria Antica Forno')),
('DRINK', 'Acqua Minerale Frizzante', 'Acqua naturale e frizzante.', (SELECT id FROM "Restaurant" WHERE name='Osteria del Sole')),
('DRINK', 'Prosecco DOCG', 'Vino spumante secco, ideale per aperitivo.', (SELECT id FROM "Restaurant" WHERE name='Trattoria Toscana Saporita')),
('DRINK', 'Sangria di Frutta Fresca', 'Bevanda analcolica e rinfrescante con frutta stagionale.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Mediterraneo')),
('DRINK', 'Limonata Artigianale', 'Succo fresco di limone, dolce e acidulo.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('DRINK', 'Birra Locale artigianale', 'Una selezione di birre prodotte localmente.', (SELECT id FROM "Restaurant" WHERE name='Hostaria del Pellegrino')),
('DRINK', 'Succo d''Arancia Estratto a Freddo', 'Succo naturale spremuto a freddo.', (SELECT id FROM "Restaurant" WHERE name='Cucina Etnica Giapponese')),
('DRINK', 'Chinotto Soda', 'Bibita italiana amara e frizzante.', (SELECT id FROM "Restaurant" WHERE name='La Dolce Vita Cafè')),
('DRINK', 'Amaro Nostrano', 'Digestivo tradizionale dopo i pasti.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('DRINK', 'Caffè Espresso Marocchino', 'Un espresso con schiuma e cacao dolce.', (SELECT id FROM "Restaurant" WHERE name='Mangia Bene Milano')),
('DRINK', 'Analcolico alle Erbe', 'Infuso digestivo non alcolico fatto in casa.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('DRINK', 'Acqua Panna Regionale', 'Acqua naturale della regione di Bergamo.', (SELECT id FROM "Restaurant" WHERE name='Agricola di Bergamo')),
('DRINK', 'Tè Verde Giapponese Hot/Iced', 'Tisana leggera e benefica, tipica della cucina asiatica.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('DRINK', 'Mojito Classico', 'Cocktail fresco con menta e rum.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Lago')),
('DRINK', 'Bianco di Montepulciano DOC', 'Vino bianco leggero per accompagnare antipasti freschi.', (SELECT id FROM "Restaurant" WHERE name='Antica Osteria di Firenze')),
('DRINK', 'Granita al Limone Siciliana', 'Dolce ghiacciato, tipico della cucina calabrese.', (SELECT id FROM "Restaurant" WHERE name='Piccola Cucina Calabrese')),
('DRINK', 'Franciacorta Brut DOCG', 'Spumante elegante prodotto in Lombardia.', (SELECT id FROM "Restaurant" WHERE name='The Tuscan Plate (Tavola Toscana)')),
('DRINK', 'Amaro Montenegro', 'Amaro italiano dal sapore equilibrato.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Vesuvio')),

-- APPETIZER: Antipasto
('APPETIZER', 'Caprese Fresca', 'Mozzarella di bufala, pomodori e basilico fresco con olio extra vergine d''oliva.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('APPETIZER', 'Carpaccio di Manzo', 'Fette sottili di manzo marezzato condite con limone, parmigiano e rucola.', (SELECT id FROM "Restaurant" WHERE name='Pizzeria Antica Forno')),
('APPETIZER', 'Arancini al Ragù', 'Palline di riso ripiene di ragù e mozzarella, fritte.', (SELECT id FROM "Restaurant" WHERE name='Osteria del Sole')),
('APPETIZER', 'Crostini ai Funghi Porcini', 'Fette di pane tostate con crema di funghi porcini.', (SELECT id FROM "Restaurant" WHERE name='Trattoria Toscana Saporita')),
('APPETIZER', 'Insalata di Polpo alla Griglia', 'Polpo grigliato servito in insalata con patate e prezzemolo.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Mediterraneo')),
('APPETIZER', 'Focaccia con Olive Taggiasche', 'Pane soffice toscano guarnito con olive.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('APPETIZER', 'Tagliere di Salumi Misti', 'Selezione di salumi regionali, perfetto per condividere.', (SELECT id FROM "Restaurant" WHERE name='Hostaria del Pellegrino')),
('APPETIZER', 'Edamame al Vapore e Sale Marina', 'Fagioli di soia cotti a vapore con sale marino.', (SELECT id FROM "Restaurant" WHERE name='Cucina Etnica Giapponese')),
('APPETIZER', 'Panzerotti Ripieni', 'Piccoli calzoni fritti, ripieni di ricotta e pomodoro.', (SELECT id FROM "Restaurant" WHERE name='La Dolce Vita Cafè')),
('APPETIZER', 'Olive Marinate con Erbe Aromatiche', 'Miscela di olive marinata in olio d''oliva aromatizzato.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('APPETIZER', 'Bruschette al Pomodoro Fresco', 'Fette di pane tostato con pomodori maturi e basilico.', (SELECT id FROM "Restaurant" WHERE name='Mangia Bene Milano')),
('APPETIZER', 'Carciofi alla Romana', 'Carciofi stufati secondo la tradizione romana.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('APPETIZER', 'Formaggio di Malga Fresco', 'Un pezzo di formaggio artigianale della regione.', (SELECT id FROM "Restaurant" WHERE name='Agricola di Bergamo')),
('APPETIZER', 'Gyoza Riso e Verdure', 'Ravioli giapponesi ripieni di riso e verdure miste, cotti al vapore.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('APPETIZER', 'Crostini con Paté di Funghi Porcini', 'Fette di pane con patè cremoso ai porcini.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Lago')),
('APPETIZER', 'Prosciutto Toscano e Melone', 'Accostamento dolce-salato, tipico toscano.', (SELECT id FROM "Restaurant" WHERE name='Antica Osteria di Firenze')),
('APPETIZER', 'Fiori di Zucca Fritti con Ricotta', 'Impasto croccante ripieno di ricotta fresca e fiori di zucca.', (SELECT id FROM "Restaurant" WHERE name='Piccola Cucina Calabrese')),
('APPETIZER', 'Tagliere di Formaggi Regionali', 'Selezione curata di formaggi italiani da tutta Italia.', (SELECT id FROM "Restaurant" WHERE name='The Tuscan Plate (Tavola Toscana)')),
('APPETIZER', 'Frittelle di Patate e Cipolle', 'Frittelle rustiche preparate con patate locali e cipolla dolce.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Vesuvio')),

-- FIRST: Primo piatto
('FIRST', 'Spaghetti alla Carbonara', 'Pasta con uova, pecorino e guanciale.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('FIRST', 'Risotto ai Funghi Porcini', 'Crema di riso con funghi freschi.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('FIRST', 'Penne alla Arrabbiata', 'Pasta con sugo piccante e pomodoro.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('FIRST', 'Lasagna Bolognese', 'Strati di pasta, ragù e besciamella.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('FIRST', 'Gnocchi di Patate al Ragù', 'Gnocchi fatti in casa conditi con ragù.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('FIRST', 'Pasta al Pesto Genovese', 'Penne condite con pesto di basilico.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('FIRST', 'Zuppa di Lenticchie', 'Crema calda e sostanziosa di lenticchie.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('FIRST', 'Ravioli Ripieni di Ricotta', 'Pasta ripiena con ricotta e spinaci.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('FIRST', 'Cacio e Pepe', 'Pasta condita con pecorino romano e pepe nero.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('FIRST', 'Pizza Marinara Classica', 'Impasto sottile con pomodoro, aglio e origano.', (SELECT id FROM "Restaurant" WHERE name='Pizzeria Antica Forno')),
('FIRST', 'Zuppa di Farro con Verdure Miste', 'Crema rustica di farro cotta con verdure di stagione.', (SELECT id FROM "Restaurant" WHERE name='Osteria del Sole')),
('FIRST', 'Pappa al Pomodoro Toscano', 'Pane raffermo cotto in un ricco brodo di pomodori.', (SELECT id FROM "Restaurant" WHERE name='Trattoria Toscana Saporita')),
('FIRST', 'Involtini di Pollo con Erbe', 'Fettine di pollo ripiene di spinaci e erbe aromatiche.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Mediterraneo')),
('FIRST', 'Ribollita Toscana Tradizionale', 'Zuppa densa e nutriente a base di pane raffermo e verdure.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('FIRST', 'Cacio e Pepe con Guanciale Croccante', 'Pasta condita con pecorino romano DOP e pepe nero macinato.', (SELECT id FROM "Restaurant" WHERE name='Hostaria del Pellegrino')),
('FIRST', 'Udon con Brodo di Dashi e Verdure', 'Spaghettoni giapponesi serviti in brodo umami.', (SELECT id FROM "Restaurant" WHERE name='Cucina Etnica Giapponese')),
('FIRST', 'Pasta al Ragù di Funghi Porcini', 'Penne condite con ricco ragù a base di funghi porcini.', (SELECT id FROM "Restaurant" WHERE name='La Dolce Vita Cafè')),
('FIRST', 'Linguine ai Frutti di Mare Freschi', 'Spaghettoni conditi con vongole, gamberi e cozze appena pescati.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('FIRST', 'Risotto alla Milanese con Ossobuco', 'Crema di riso allo zafferano accompagnata da ossobuco.', (SELECT id FROM "Restaurant" WHERE name='Mangia Bene Milano')),
('FIRST', 'Carbonara Perfetta con Guanciale', 'Pasta condita classicamente con uova, pecorino e guanciale.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('FIRST', 'Polenta con Ragù Misto', 'Crema di mais accompagnata da un ragù rustico e sostanzioso.', (SELECT id FROM "Restaurant" WHERE name='Agricola di Bergamo')),
('FIRST', 'Soba al Brodo Leggero', 'Spaghetti giapponesi sottili serviti in un brodo delicato.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('FIRST', 'Tajarin con Tartufo', 'Pasta all''uovo fresca condita con tartufo pregiato.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Lago')),
('FIRST', 'Pappa al Pomodoro e Fagioli', 'Zuppa toscana densa a base di pomodori e fagioli cannellini.', (SELECT id FROM "Restaurant" WHERE name='Antica Osteria di Firenze')),
('FIRST', 'Pasta con Ragù alla Norma', 'Pasta condita con salsa di pomodoro, melanzane fritte e ricotta salata.', (SELECT id FROM "Restaurant" WHERE name='Piccola Cucina Calabrese')),
('FIRST', 'Pappardelle al Ragù di Cinghiale Toscano', 'Larga pasta condita con un ricco ragù preparato con cinghiale.', (SELECT id FROM "Restaurant" WHERE name='The Tuscan Plate (Tavola Toscana)')),
('FIRST', 'Pasta e Patate con Ragù Napoletano', 'Primo piatto robusto a base di patate, pasta e ricco ragù napoletano.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Vesuvio')),
('FIRST', 'Tajarin al Tartufo Bianco d''Alba', 'Spaghetti sottilissimi conditi con tartufo bianco pregiato.', (SELECT id FROM "Restaurant" WHERE name='Cucina Piemontese')),

-- CONTOUR: Contorno/Side Dish
('CONTOUR', 'Verdure Grigliate Miste', 'Melanzane, peperoni e zucchine grigliati conditi con erbe aromatiche.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('CONTOUR', 'Patate al Forno Rosse', 'Cubetti di patata rossa cotti lentamente nel forno.', (SELECT id FROM "Restaurant" WHERE name='Pizzeria Antica Forno')),
('CONTOUR', 'Asparagi Saltati in Padella', 'Asparagi freschi saltati con aglio e olio d''oliva.', (SELECT id FROM "Restaurant" WHERE name='Osteria del Sole')),
('CONTOUR', 'Polenta Cremosa con Funghi', 'Polenta morbida servita con un ricco condimento di funghi porcini.', (SELECT id FROM "Restaurant" WHERE name='Trattoria Toscana Saporita')),
('CONTOUR', 'Risotto ai Gamberi', 'Riso cremoso mantecato con gamberi freschi e vino bianco.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Mediterraneo')),
('CONTOUR', 'Verdure di Stagione al Vapore', 'Mix di verdure fresche cotte a vapore, leggere e gustose.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('CONTOUR', 'Spinaci Saltati con Aglio', 'Spinaci freschi saltati in padella con aglio soffritto.', (SELECT id FROM "Restaurant" WHERE name='Hostaria del Pellegrino')),
('CONTOUR', 'Insalata di Riso Giapponese', 'Riso condito con edamame, alghe e verdure croccanti.', (SELECT id FROM "Restaurant" WHERE name='Cucina Etnica Giapponese')),
('CONTOUR', 'Verdura Ripiena al Forno', 'Peperoni ripieni di riso, pomodorini e formaggio gratinati.', (SELECT id FROM "Restaurant" WHERE name='La Dolce Vita Cafè')),
('CONTOUR', 'Patate Arrosto con Rosmarino', 'Cubetti di patata arrostiti con erbe aromatiche mediterranee.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('CONTOUR', 'Asparagi Gratinati al Formaggio', 'Asparagi freschi cotti e gratinati con formaggio campano.', (SELECT id FROM "Restaurant" WHERE name='Mangia Bene Milano')),
('CONTOUR', 'Contorno di Cicoria Ripassata', 'Cicoria saltata in padella con olio e aglio, tipico della cucina romana.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('CONTOUR', 'Verdure Miste Grigliate', 'Un assortimento di verdure regionali grigliate.', (SELECT id FROM "Restaurant" WHERE name='Agricola di Bergamo')),
('CONTOUR', 'Insalata Kyuri e Wasabi', 'Lattuga giapponese croccante con salsa al wasabi leggero.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('CONTOUR', 'Asparagi alla Griglia con Limone', 'Assaggio di asparagi freschissimi grigliati conditi solo con limone.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Lago')),
('CONTOUR', 'Fagioli all''Uccelletto Toscana', 'Fagioli cannellini stufati con pomodoro e salvia, stile toscano.', (SELECT id FROM "Restaurant" WHERE name='Antica Osteria di Firenze')),
('CONTOUR', 'Verdure Ripiene Calabresi al Forno', 'Peperoni ripieni e cotti lentamente secondo la tradizione calabrese.', (SELECT id FROM "Restaurant" WHERE name='Piccola Cucina Calabrese')),
('CONTOUR', 'Broccoli Misti Saltati con Mandorle', 'Cime di broccoli saltate in padella con mandorle tostate.', (SELECT id FROM "Restaurant" WHERE name='The Tuscan Plate (Tavola Toscana)')),
('CONTOUR', 'Cavolfiore al Limone e Burro', 'Fiori di cavolfiore cotti nel brodo e conditi con limone e burro.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Vesuvio')),

-- SECOND: Secondo piatto
('SECOND', 'Saltimbocca alla Romana', 'Fettine di vitello con prosciutto e salvia.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('SECOND', 'Pollo al Limone', 'Petto di pollo cotto in salsa al limone.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('SECOND', 'Filetto di Manzo al Pepe Verde', 'Tagliata sottile con salsa al pepe verde.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('SECOND', 'Pesce Spada alla Griglia', 'Filetto di pesce spada grigliato con limone.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('SECOND', 'Cotoletta alla Milanese', 'Fetta di vitello impanata e fritta.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('SECOND', 'Ossobuco alla Milanese', 'Presa bovina stufata con midollo.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('SECOND', 'Anticuchos Peruviani', 'Spiedini marinati tipici del Sud America.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('SECOND', 'Tagliata di Maiale Affumicato', 'Fette sottili di maiale affumicato.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('SECOND', 'Bistecca Fiorentina', 'Grande bistecca di Chianina alla griglia.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('SECOND', 'Melanzane alla Parmigiana', 'Strati di melanzane fritte, pomodoro e mozzarella al forno.', (SELECT id FROM "Restaurant" WHERE name='Pizzeria Antica Forno')),
('SECOND', 'Cotoletta di Pollo Sud Americana', 'Petto di pollo marinato e cotto secondo la tradizione giovanile.', (SELECT id FROM "Restaurant" WHERE name='Osteria del Sole')),
('SECOND', 'Bistecca ai Grassi della Terra', 'Tagliata spessa condita con grasso d''oca e erbe aromatiche.', (SELECT id FROM "Restaurant" WHERE name='Trattoria Toscana Saporita')),
('SECOND', 'Zuppa di Pesce Misto', 'Ricca zuppa preparata con diverse varietà di pesce fresco del mare.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Mediterraneo')),
('SECOND', 'Peposo in Umido', 'Presa cotta lentamente nel vino rosso con pepe nero, metodo tipico toscano.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('SECOND', 'Pollo alla Griglia Marinato', 'Petto di pollo marinato con spezie locali e cotto alla griglia.', (SELECT id FROM "Restaurant" WHERE name='Hostaria del Pellegrino')),
('SECOND', 'Teriyaki Chicken Skewers', 'Spiedini di pollo marinati nella salsa teriyaki, cotti alla griglia.', (SELECT id FROM "Restaurant" WHERE name='Cucina Etnica Giapponese')),
('SECOND', 'Polpette al Sugo di Pomodoro Fresco', 'Piccole polpette di carne cotte lentamente in salsa di pomodoro casalinga.', (SELECT id FROM "Restaurant" WHERE name='La Dolce Vita Cafè')),
('SECOND', 'Baccalà alla Napoletana', 'Filetto di baccalà cotto in salsa di pomodoro tipica della Campania.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('SECOND', 'Saltimbocca al Latte', 'Fettine di vitello marinate nel latte e poi saltate in padella con salvia.', (SELECT id FROM "Restaurant" WHERE name='Mangia Bene Milano')),
('SECOND', 'Saltimbocca di Tacchino alla Romana', 'Variante più leggera del saltimbocca, utilizzando carne di tacchino.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('SECOND', 'Scaloppine al Burro e Salvia', 'Fette sottili di vitello cotte nel burro con foglie di salvia.', (SELECT id FROM "Restaurant" WHERE name='Agricola di Bergamo')),
('SECOND', 'Chawanmushi con Funghi Shiitake', 'Budino caldo giapponese con ripieno di funghi e uova, tipico del ristorante.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('SECOND', 'Branzino al Forno con Patate Novelle', 'Filetto di branzino cotto lentamente nel forno con patate dolci e erbe.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Lago')),
('SECOND', 'Peposo di Manzo Tradizionale', 'Manzo cotto lentamente nel vino rosso con pepe, secondo la tradizione fiorentina.', (SELECT id FROM "Restaurant" WHERE name='Antica Osteria di Firenze')),
('SECOND', 'Salsiccia al Forno con Peperoncino', 'Salsicce artigianali cotte lentamente al forno con un tocco di peperoncino calabrese.', (SELECT id FROM "Restaurant" WHERE name='Piccola Cucina Calabrese')),
('SECOND', 'Tagliata di Manzo ai Sapori della Terra', 'Fette sottili di manzo servite con salse regionali come mostarda e miele.', (SELECT id FROM "Restaurant" WHERE name='The Tuscan Plate (Tavola Toscana)')),
('SECOND', 'Frittura Mista del Golfo', 'Selezione di pesce fritto misto (gamberi, calamari, piccoli pesci) tipico della zona vesuviana.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Vesuvio')),
('SECOND', 'Brasato al Barolo Classico', 'Manzo stufato lentamente nel vino Barolo, secondo la tradizione piemontese.', (SELECT id FROM "Restaurant" WHERE name='Cucina Piemontese')),

-- DESSERT: Dolce
('DESSERT', 'Panna Cotta ai Frutti di Bosco', 'Dolce cremoso alla vaniglia servito con una salsa acidula di frutti di bosco.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('DESSERT', 'Tiramisù Classico Ricreato', 'Ripetizione e miglioramento della ricetta classica al mascarpone, caffè e savoiardi.', (SELECT id FROM "Restaurant" WHERE name='Pizzeria Antica Forno')), -- Ripetuto per il tipo DESSERT
('DESSERT', 'Zuppa Inglese con Amarena', 'Dolce tradizionale italiano a strati di pan di Spagna bagnato al liquore.', (SELECT id FROM "Restaurant" WHERE name='Osteria del Sole')),
('DESSERT', 'Cantucci e Vin Santo', 'Biscotti secchi tostati serviti accompagnati da Vin Santo toscano.', (SELECT id FROM "Restaurant" WHERE name='Trattoria Toscana Saporita')),
('DESSERT', 'Sorbetto al Limone Siciliano', 'Gelato fresco e acido, ideale per concludere un pasto leggero.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Mediterraneo')),
('DESSERT', 'Cannoli Siciliani Ripieni di Ricotta', 'Cialde croccanti ripiene di ricotta dolce zuccherata.', (SELECT id FROM "Restaurant" WHERE name='Il Gusto Toscano')),
('DESSERT', 'Gelato Artigianale Misto', 'Selezione di gusti classici e innovativi del giorno.', (SELECT id FROM "Restaurant" WHERE name='Hostaria del Pellegrino')),
('DESSERT', 'Mochi al Matcha Giapponese', 'Dolci morbidi giapponesi con gusto matcha, perfetti dopo il pasto asiatico.', (SELECT id FROM "Restaurant" WHERE name='Cucina Etnica Giapponese')),
('DESSERT', 'Crema al Cioccolato Fondente', 'Crema densa e ricca preparata con cioccolati di alta qualità.', (SELECT id FROM "Restaurant" WHERE name='La Dolce Vita Cafè')),
('DESSERT', 'Cannoli Campani con Crema di Ricotta e Prugna', 'Variante calabrese del cannolo, ripieno speziato.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Golfo')),
('DESSERT', 'Panna Cotta al Caramello Salato', 'Crema vellutata con un tocco inaspettato di sale Maldon e caramello.', (SELECT id FROM "Restaurant" WHERE name='Mangia Bene Milano')),
('DESSERT', 'Crostata di Marmellata di Frutti Rossi', 'Torta rustica ripiena di marmellata artigianale fatta in casa.', (SELECT id FROM "Restaurant" WHERE name='Cucina Romana Autentica')),
('DESSERT', 'Bircher Muesli con Frutta Fresca', 'Un dolce sano e nutriente a base di avena, frutta secca e yogurt.', (SELECT id FROM "Restaurant" WHERE name='Agricola di Bergamo')),
('DESSERT', 'Parfait di Yogurt Greco e Frutti di Bosco', 'Strati di yogurt greco, granola croccante e frutti rossi freschi.', (SELECT id FROM "Restaurant" WHERE name='Sushi Master Italia')),
('DESSERT', 'Tiramisù Classico', 'Dolce a base di mascarpone, caffè e savoiardi.', (SELECT id FROM "Restaurant" WHERE name='La Trattoria del Nonno')),
('DESSERT', 'Torta della Nonna con Crema di Limone', 'Classica torta italiana con una generosa crema di limone.', (SELECT id FROM "Restaurant" WHERE name='Ristorante del Lago')),
('DESSERT', 'Biscotti al Rosmarino e Miele Toscano', 'Dolcetti rustici accompagnati da miele locale della Toscana.', (SELECT id FROM "Restaurant" WHERE name='Antica Osteria di Firenze')),
('DESSERT', 'Babà al Rum con Crema pasticcera', 'Il classico babà bagnato nel rum, servito con una ricca crema.', (SELECT id FROM "Restaurant" WHERE name='Piccola Cucina Calabrese')),
('DESSERT', 'Tiramisù al Caffè d''Orzo', 'Versione più delicata del tiramisù preparata con caffè d''orzo.', (SELECT id FROM "Restaurant" WHERE name='The Tuscan Plate (Tavola Toscana)')),
('DESSERT', 'Sfera di Cioccolato Fondente Fritta', 'Una sfera di cioccolato calda servita con gelato alla vaniglia.', (SELECT id FROM "Restaurant" WHERE name='Ristorante Vesuvio')),
('DESSERT', 'Torta di Castagne e Pere ', 'Dolce autunnale preparato con castagne e pere dolci della regione Piemonte.', (SELECT id FROM "Restaurant" WHERE name='Cucina Piemontese'));

INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Cipolla Rossa'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Funghi Porcini'), TRUE, FALSE),
((SELECT id FROM "Ingredient" WHERE name = 'Broccolini'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Funghi Porcini'), TRUE, FALSE);

-- Questi insert dovrebbero essere ripetuti per tutti i piatti e gli ingredienti collegati.

INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "Dish" WHERE name = 'Ossobuco alla Milanese'), TRUE, FALSE);

INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Peperoncino'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Funghi Porcini'), TRUE, FALSE),
((SELECT id FROM "Ingredient" WHERE name = 'Cipolla Rossa'), (SELECT id FROM "Dish" WHERE name = 'Ossobuco alla Milanese'), TRUE, FALSE);

-- Generazione degli ingredienti mancanti e dei collegamenti Dish_Ingredient per ogni piatto

-- PRIMI PIATTI (FIRST)
-- Spaghetti alla Carbonara - Ingredienti: Uova, Pecorino Romano
INSERT INTO "Ingredient" (name) VALUES ('Uova');
INSERT INTO "Ingredient" (name) VALUES ('Pecorino Romano');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "Dish" WHERE name = 'Spaghetti alla Carbonara'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Uova'), (SELECT id FROM "Dish" WHERE name = 'Spaghetti alla Carbonara'), FALSE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pecorino Romano'), (SELECT id FROM "Dish" WHERE name = 'Spaghetti alla Carbonara'), FALSE, FALSE);

-- Risotto ai Funghi Porcini - Ingredienti: Riso Arborio, Fungo Porcino
INSERT INTO "Ingredient" (name) VALUES ('Fungo Porcino');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Riso Arborio'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Funghi Porcini'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fungo Porcino'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Funghi Porcini'), TRUE, FALSE);

-- Penne alla Arrabbiata - Ingredienti: Pomodoro Piccante
INSERT INTO "Ingredient" (name) VALUES ('Pomodoro Piccante');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Dish" WHERE name = 'Penne alla Arrabbiata'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pomodoro Piccante'), (SELECT id FROM "Dish" WHERE name = 'Penne alla Arrabbiata'), TRUE, FALSE);

-- Lasagna Bolognese - Ingredienti: Ragù di Carne, Besciamella
INSERT INTO "Ingredient" (name) VALUES ('Ragù di Carne');
INSERT INTO "Ingredient" (name) VALUES ('Besciamella');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Dish" WHERE name = 'Lasagna Bolognese'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "Dish" WHERE name = 'Lasagna Bolognese'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Besciamella'), (SELECT id FROM "Dish" WHERE name = 'Lasagna Bolognese'), FALSE, FALSE);

-- Gnocchi di Patate al Ragù - Ingredienti: Patate
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Patate'), (SELECT id FROM "Dish" WHERE name = 'Gnocchi di Patate al Ragù'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "Dish" WHERE name = 'Gnocchi di Patate al Ragù'), TRUE, FALSE);

-- Pasta al Pesto Genovese - Ingredienti: Basilico
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Dish" WHERE name = 'Pasta al Pesto Genovese'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Basilico'), (SELECT id FROM "Dish" WHERE name = 'Pasta al Pesto Genovese'), FALSE, FALSE);

-- Zuppa di Lenticchie - Ingredienti: Lenticchie
INSERT INTO "Ingredient" (name) VALUES ('Lenticchie');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Lenticchie'), (SELECT id FROM "Dish" WHERE name = 'Zuppa di Lenticchie'), TRUE, FALSE);

-- Ravioli Ripieni di Ricotta - Ingredienti: Ricotta
INSERT INTO "Ingredient" (name) VALUES ('Ricotta');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Dish" WHERE name = 'Ravioli Ripieni di Ricotta'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ricotta'), (SELECT id FROM "Dish" WHERE name = 'Ravioli Ripieni di Ricotta'), FALSE, FALSE);

-- Cacio e Pepe - Ingredienti: Pecorino Romano, Pepe Nero
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Dish" WHERE name = 'Cacio e Pepe'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pecorino Romano'), (SELECT id FROM "Dish" WHERE name = 'Cacio e Pepe'), FALSE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Peperoncino'), (SELECT id FROM "Dish" WHERE name = 'Cacio e Pepe'), TRUE, FALSE);

-- Pizza Marinara Classica - Ingredienti: Pomodoro, Aglio, Origano
INSERT INTO "Ingredient" (name) VALUES ('Pomodoro'); -- Assumiamo che ci sia un ingredienti base per pomodori non specificato finora.
INSERT INTO "Ingredient" (name) VALUES ('Origano'); -- Assumiamo che ci sia un ingredienti base per pomodori non specificato finora.
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Dish" WHERE name = 'Pizza Marinara Classica'), TRUE, FALSE); -- Assumiamo che la base sia un tipo di pasta/impasto
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pomodoro'), (SELECT id FROM "Dish" WHERE name = 'Pizza Marinara Classica'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Aglio'), (SELECT id FROM "Dish" WHERE name = 'Pizza Marinara Classica'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Origano'), (SELECT id FROM "Dish" WHERE name = 'Pizza Marinara Classica'), FALSE, FALSE);

-- Zuppa di Farro con Verdure Miste - Ingredienti: Farro, Verdura
INSERT INTO "Ingredient" (name) VALUES ('Farro');
INSERT INTO "Ingredient" (name) VALUES ('Verdure Regionali'); -- Generico per verdure miste
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Farro'), (SELECT id FROM "Dish" WHERE name = 'Zuppa di Farro con Verdure Miste'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Verdure Regionali'), (SELECT id FROM "Dish" WHERE name = 'Zuppa di Farro con Verdure Miste'), TRUE, FALSE);

-- Pappa al Pomodoro Toscano - Ingredienti: Pane Raffermo, Pomodori
INSERT INTO "Ingredient" (name) VALUES ('Pane Raffermo');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pane Raffermo'), (SELECT id FROM "Dish" WHERE name = 'Pappa al Pomodoro Toscano'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pomodoro'), (SELECT id FROM "Dish" WHERE name = 'Pappa al Pomodoro Toscano'), TRUE, FALSE);

-- Involtini di Pollo con Erbe - Ingredienti: Petto di Pollo
INSERT INTO "Ingredient" (name) VALUES ('Petto di Pollo');
INSERT INTO "Ingredient" (name) VALUES ('Erbe Aromatiche');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Petto di Pollo'), (SELECT id FROM "Dish" WHERE name = 'Involtini di Pollo con Erbe'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Erbe Aromatiche'), (SELECT id FROM "Dish" WHERE name = 'Involtini di Pollo con Erbe'), FALSE, FALSE);


-- SECONDI PIATTI (SECOND)
-- Saltimbocca alla Romana - Ingredienti: Vitello, Prosciutto, Salvia
INSERT INTO "Ingredient" (name) VALUES ('Vitello');
INSERT INTO "Ingredient" (name) VALUES ('Prosciutto'); -- Già presente in [1] come "Prosciutto Toscano", ma usiamo il nome specifico per chiarezza.
INSERT INTO "Ingredient" (name) VALUES ('Salvia'); -- Già presente in [1] come "Prosciutto Toscano", ma usiamo il nome specifico per chiarezza.
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Vitello'), (SELECT id FROM "Dish" WHERE name = 'Saltimbocca alla Romana'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Prosciutto'), (SELECT id FROM "Dish" WHERE name = 'Saltimbocca alla Romana'), FALSE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Salvia'), (SELECT id FROM "Dish" WHERE name = 'Saltimbocca alla Romana'), FALSE, FALSE);

-- Pollo al Limone - Ingredienti: Petto di Pollo
INSERT INTO "Ingredient" (name) VALUES ('Limone');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Petto di Pollo'), (SELECT id FROM "Dish" WHERE name = 'Pollo al Limone'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Limone'), (SELECT id FROM "Dish" WHERE name = 'Pollo al Limone'), FALSE, FALSE);

-- Filetto di Manzo al Pepe Verde - Ingredienti: Manzo
INSERT INTO "Ingredient" (name) VALUES ('Manzo');
INSERT INTO "Ingredient" (name) VALUES ('Pepe Verde');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "Dish" WHERE name = 'Filetto di Manzo al Pepe Verde'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pepe Verde'), (SELECT id FROM "Dish" WHERE name = 'Filetto di Manzo al Pepe Verde'), TRUE, FALSE);

-- Pesce Spada alla Griglia - Ingredienti: Pesce Spada
INSERT INTO "Ingredient" (name) VALUES ('Pesce Spada');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pesce Spada'), (SELECT id FROM "Dish" WHERE name = 'Pesce Spada alla Griglia'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Limone'), (SELECT id FROM "Dish" WHERE name = 'Pesce Spada alla Griglia'), FALSE, FALSE);

-- Ossobuco alla Milanese - Ingredienti: Presa Bovina
INSERT INTO "Ingredient" (name) VALUES ('Presa Bovina'); -- Gia presente in [1] come manzo macinato o non specificato. Usiamo il termine più specifico per l'ossobuco.
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Presa Bovina'), (SELECT id FROM "Dish" WHERE name = 'Ossobuco alla Milanese'), TRUE, FALSE);

-- Anticuchos Peruviani - Ingredienti: Spiedini, Carne Marinata
INSERT INTO "Ingredient" (name) VALUES ('Spiedino');
INSERT INTO "Ingredient" (name) VALUES ('Carne Marinata');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Carne Marinata'), (SELECT id FROM "Dish" WHERE name = 'Anticuchos Peruviani'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Spiedino'), (SELECT id FROM "Dish" WHERE name = 'Anticuchos Peruviani'), TRUE, FALSE);

-- Bistecca Fiorentina - Ingredienti: Carne di Chianina
INSERT INTO "Ingredient" (name) VALUES ('Carne di Chianina');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Carne di Chianina'), (SELECT id FROM "Dish" WHERE name = 'Bistecca Fiorentina'), TRUE, FALSE);

-- Melanzane alla Parmigiana - Ingredienti: Melanzana, Mozzarella
INSERT INTO "Ingredient" (name) VALUES ('Melanzane');

INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Melanzane'), (SELECT id FROM "Dish" WHERE name = 'Melanzane alla Parmigiana'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Mozzarella Fresca'), (SELECT id FROM "Dish" WHERE name = 'Melanzane alla Parmigiana'), TRUE, FALSE);

-- Cotoletta di Pollo Sud Americana - Ingredienti: Petto di Pollo
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Petto di Pollo'), (SELECT id FROM "Dish" WHERE name = 'Cotoletta di Pollo Sud Americana'), TRUE, FALSE);

-- Peposo in Umido - Ingredienti: Presa
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Presa Bovina'), (SELECT id FROM "Dish" WHERE name = 'Peposo in Umido'), TRUE, FALSE);

-- Tagliata di Manzo ai Sapori della Terra - Ingredienti: Manzo
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "Dish" WHERE name = 'Tagliata di Manzo ai Sapori della Terra'), TRUE, FALSE);

-- Brasato al Barolo Classico - Ingredienti: Manzo
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "Dish" WHERE name = 'Brasato al Barolo Classico'), TRUE, FALSE);


-- ANTIPASTI (APPETIZER)
-- Caprese Fresca - Ingredienti: Mozzarella di Bufala, Pomodoro, Basilico
INSERT INTO "Ingredient" (name) VALUES ('Mozzarella di Bufala'); -- Distinguendola dalla mozzarella fresca generica se necessario.
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Mozzarella di Bufala'), (SELECT id FROM "Dish" WHERE name = 'Caprese Fresca'), FALSE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pomodoro'), (SELECT id FROM "Dish" WHERE name = 'Caprese Fresca'), FALSE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Basilico'), (SELECT id FROM "Dish" WHERE name = 'Caprese Fresca'), FALSE, FALSE);

-- Carpaccio di Manzo - Ingredienti: Manzo
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "Dish" WHERE name = 'Carpaccio di Manzo'), TRUE, FALSE);

-- Arancini al Ragù - Ingredienti: Riso, Ragù
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Riso Arborio'), (SELECT id FROM "Dish" WHERE name = 'Arancini al Ragù'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "Dish" WHERE name = 'Arancini al Ragù'), TRUE, FALSE);

-- Crostini ai Funghi Porcini - Ingredienti: Pane, Fungo Porcino
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pane Integrale'), (SELECT id FROM "Dish" WHERE name = 'Crostini ai Funghi Porcini'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fungo Porcino'), (SELECT id FROM "Dish" WHERE name = 'Crostini ai Funghi Porcini'), FALSE, FALSE);

-- Tagliere di Salumi Misti - Ingredienti: Selezione Salumi
INSERT INTO "Ingredient" (name) VALUES ('Selezione di Salumi Regionali');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Selezione di Salumi Regionali'), (SELECT id FROM "Dish" WHERE name = 'Tagliere di Salumi Misti'), FALSE, FALSE);

-- Fiori di Zucca Fritti con Ricotta - Ingredienti: Fiore di Zucca
INSERT INTO "Ingredient" (name) VALUES ('Fiore di Zucca');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fiore di Zucca'), (SELECT id FROM "Dish" WHERE name = 'Fiori di Zucca Fritti con Ricotta'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ricotta'), (SELECT id FROM "Dish" WHERE name = 'Fiori di Zucca Fritti con Ricotta'), FALSE, FALSE);


-- CONTOUR/CONTORNO (CONTOUR)
-- Verdure Grigliate Miste - Ingredienti: Melanzane, Peperoni, Zucchine
INSERT INTO "Ingredient" (name) VALUES ('Peperoni');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Melanzane'), (SELECT id FROM "Dish" WHERE name = 'Verdure Grigliate Miste'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Peperoni'), (SELECT id FROM "Dish" WHERE name = 'Verdure Grigliate Miste'), TRUE, FALSE); -- Presupponiamo che Peperoni sia già in Ingredient
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Zucchine'), (SELECT id FROM "Dish" WHERE name = 'Verdure Grigliate Miste'), TRUE, FALSE);

-- Asparagi Saltati in Padella - Ingredienti: Asparagi
INSERT INTO "Ingredient" (name) VALUES ('Asparagi');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Asparagi'), (SELECT id FROM "Dish" WHERE name = 'Asparagi Saltati in Padella'), TRUE, FALSE);

-- Polenta Cremosa con Funghi - Ingredienti: Polenta, Fungo Porcino
INSERT INTO "Ingredient" (name) VALUES ('Polenta');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Polenta'), (SELECT id FROM "Dish" WHERE name = 'Polenta Cremosa con Funghi'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fungo Porcino'), (SELECT id FROM "Dish" WHERE name = 'Polenta Cremosa con Funghi'), TRUE, FALSE);

-- Risotto ai Gamberi - Ingredienti: Riso Arborio, Gamberi
INSERT INTO "Ingredient" (name) VALUES ('Gamberi');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Riso Arborio'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Gamberi'), TRUE, FALSE);
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Gamberi'), (SELECT id FROM "Dish" WHERE name = 'Risotto ai Gamberi'), TRUE, FALSE);

-- Verdura Ripiena al Forno - Ingredienti: Peperoni
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Peperoni'), (SELECT id FROM "Dish" WHERE name = 'Verdura Ripiena al Forno'), TRUE, FALSE);

-- Fagioli all'Uccelletto Toscana - Ingredienti: Fagioli Cannellini
INSERT INTO "Ingredient" (name) VALUES ('Fagioli Cannellini');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fagioli Cannellini'), (SELECT id FROM "Dish" WHERE name = 'Fagioli all''Uccelletto Toscana'), TRUE, FALSE);

-- Broccoli Misti Saltati con Mandorle - Ingredienti: Broccoli
INSERT INTO "Ingredient" (name) VALUES ('Broccoli');
INSERT INTO "Dish_Ingredient" (fk_ingredient, fk_dish, cooked, optional) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Broccoli'), (SELECT id FROM "Dish" WHERE name = 'Broccoli Misti Saltati con Mandorle'), TRUE, FALSE);

-- BLOCCO INSERIMENTO PER INGREDIENTI A BASE DI LATTICINI E DERIVATI:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Mozzarella Fresca'), (SELECT id FROM "Allergen" WHERE name = 'Latticini'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Formaggio Grana Padano'), (SELECT id FROM "Allergen" WHERE name = 'Colture'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Latte Fresco'), (SELECT id FROM "Allergen" WHERE name = 'Latticini'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pecorino Romano'), (SELECT id FROM "Allergen" WHERE name = 'Latticini'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ricotta'), (SELECT id FROM "Allergen" WHERE name = 'Latticini'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Besciamella'), (SELECT id FROM "Allergen" WHERE name = 'Latticini'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Mozzarella di Bufala'), (SELECT id FROM "Allergen" WHERE name = 'Latticini'));

-- BLOCCO INSERIMENTO PER INGREDIENTI A BASE DI GRANO/SEMOLE E FRUMENTO:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "Allergen" WHERE name = 'Glutine'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pane Integrale'), (SELECT id FROM "Allergen" WHERE name = 'Frumento'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pane Raffermo'), (SELECT id FROM "Allergen" WHERE name = 'Glutine'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Farro'), (SELECT id FROM "Allergen" WHERE name = 'Glutine'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Besciamella'), (SELECT id FROM "Allergen" WHERE name = 'Frumento')); -- Potrebbe contenere farina

-- BLOCCO INSERIMENTO PER PRODOTTI OTTICI:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Uova'), (SELECT id FROM "Allergen" WHERE name = 'Uovo'));

-- BLOCCO INSERIMENTO PER CARNE E DERIVATI:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Manzo Macinato'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Vitello'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Prosciutto'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Presa Bovina'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "Allergen" WHERE name = 'Carne Rossa'));

-- BLOCCO INSERIMENTO PER FRUTTA DI MARE E PESCE:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Gamberi'), (SELECT id FROM "Allergen" WHERE name = 'Crostacei'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Pesce Spada'), (SELECT id FROM "Allergen" WHERE name = 'Pesce'));

-- BLOCCO INSERIMENTO PER LEGUMINOSE:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fagioli Borlotti'), (SELECT id FROM "Allergen" WHERE name = 'Leguminose'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Lenticchie'), (SELECT id FROM "Allergen" WHERE name = 'Leguminose'));
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Fagioli Cannellini'), (SELECT id FROM "Allergen" WHERE name = 'Leguminose'));

-- BLOCCO INSERIMENTO PER ALTRE RISCHI COMUNI:
INSERT INTO "Ingredient_Allergen" (fk_ingredient, fk_allergen) VALUES ((SELECT id FROM "Ingredient" WHERE name = 'Tofu'), (SELECT id FROM "Allergen" WHERE name = 'Soia'));


INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pasta di Semola'), (SELECT id FROM "ExcludedGroup" WHERE name = 'HALAL'));

INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Formaggio Grana Padano'), (SELECT id FROM "ExcludedGroup" WHERE name = 'KOSHER'));

INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Latte Fresco'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Formaggio Grana Padano'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Uova'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pecorino Romano'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Besciamella'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Ricotta'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Petto di Pollo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Vitello'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Prosciutto'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pesce Spada'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Presa Bovina'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Spiedino'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Carne Marinata'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Carne di Chianina'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Gamberi'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Manzo Macinato'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Uova'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pecorino Romano'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Petto di Pollo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Vitello'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Prosciutto'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pesce Spada'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Presa Bovina'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Spiedino'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Carne Marinata'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Carne di Chianina'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Gamberi'), (SELECT id FROM "ExcludedGroup" WHERE name = 'VEGETARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Manzo Macinato'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Ragù di Carne'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Petto di Pollo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Vitello'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Prosciutto'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Manzo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Presa Bovina'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Spiedino'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Carne Marinata'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Carne di Chianina'), (SELECT id FROM "ExcludedGroup" WHERE name = 'PESCATARIAN'));

-- Halal restrictions (assuming general prohibition on certain processed/non-certified meats for broad application)
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "ExcludedGroup" WHERE name = 'HALAL'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Selezione di Salumi Regionali'), (SELECT id FROM "ExcludedGroup" WHERE name = 'HALAL'));

-- Kosher restrictions (assuming pork products are excluded and some cured meats/non-kosher sourcing for broader safety)
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Guanciale'), (SELECT id FROM "ExcludedGroup" WHERE name = 'KOSHER'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Prosciutto'), (SELECT id FROM "ExcludedGroup" WHERE name = 'KOSHER'));

-- Carnivore restrictions (excluding all plant matter/grains/fats not originating from animals)
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pomodori'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Basilico'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Patate'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Cipolla Rossa'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Aglio'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Broccolini'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Zucchine'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Fagioli Borlotti'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pane Integrale'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Noci'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Formaggio Grana Padano'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Fungo Porcino'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pomodoro Piccante'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Besciamella'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Ricotta'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pomodoro'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Origano'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Farro'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Verdure Regionali'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pane Raffermo'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Erbe Aromatiche'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Limone'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));
INSERT INTO "ExcludedGroup_Ingredient" (fk_ingredient, fk_excluded_group) VALUES
((SELECT id FROM "Ingredient" WHERE name = 'Pepe Verde'), (SELECT id FROM "ExcludedGroup" WHERE name = 'CARNIVORE'));


-- Questi insert dovrebbero essere ripetuti per tutti gli ingredienti sensibili.
