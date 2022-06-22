DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS faculty CASCADE;
DROP TABLE IF EXISTS application CASCADE;
DROP TABLE IF EXISTS subject CASCADE;
DROP TABLE IF EXISTS required_subject CASCADE;
DROP TABLE IF EXISTS subject_user_grades CASCADE;
DROP TABLE IF EXISTS application_state CASCADE;
DROP TABLE IF EXISTS recruitment CASCADE;
DROP TABLE IF EXISTS recruitment_state CASCADE;

CREATE TABLE roles
(
    id          SERIAL      NOT NULL,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(50),
    PRIMARY KEY (id)
);


CREATE TABLE "user"
(
    id          SERIAL      NOT NULL,
    username    VARCHAR(50) NOT NULL,
    email       varchar(50) NOT NULL,
    password    VARCHAR     NOT NULL,
    first_name  VARCHAR(30) NOT NULL,
    second_name VARCHAR(30) NOT NULL,
    patronymic  VARCHAR(30),
    city        VARCHAR(30) NOT NULL,
    region      VARCHAR(30) NOT NULL,
    institution VARCHAR(64) NOT NULL,
    blocked     bool        NOT NULL DEFAULT FALSE,
    role_id     INT         NOT NULL
        REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);



CREATE TABLE faculty
(
    id               SERIAL      NOT NULL,
    name             VARCHAR(50) NOT NULL,
    budget_capacity  INT         NOT NULL,
    general_capacity INT         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE application_state
(
    id    SERIAL  NOT NULL,
    state VARCHAR NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE recruitment
(
    id               SERIAL  NOT NULL,
    name             VARCHAR NOT NULL,
    budget_capacity  INT     NOT NULL,
    general_capacity INT     NOT NULL,
    start_date       date    NOT NULL,
    end_date         date    NOT NULL,
    closed           bool DEFAULT FALSE,
    faculty_id       INT     NOT NULL
        REFERENCES faculty (id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);



CREATE TABLE application
(
    id                   SERIAL NOT NULL,
    user_id              INT    NOT NULL
        REFERENCES "user" (id) ON DELETE CASCADE,
    application_state_id INT    NOT NULL
        REFERENCES application_state (id) ON DELETE
            CASCADE,
    recruitment_id       INT    NOT NULL
        REFERENCES recruitment (id) ON DELETE
            CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE subject
(
    id      SERIAL      NOT NULL,
    name_en VARCHAR(50) NOT NULL,
    name_ru VARCHAR(50) NOT NULL,
    name_uk VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE required_subject
(
    id         SERIAL NOT NULL,
    faculty_id INT    NOT NULL
        REFERENCES faculty (id) ON DELETE CASCADE,
    subject_id INT    NOT NULL
        REFERENCES subject (id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE subject_user_grades
(
    id             SERIAL NOT NULL,
    application_id INT    NOT NULL
        REFERENCES application (id) ON DELETE CASCADE,
    subject_id     INT    NOT NULL
        REFERENCES subject (id) ON DELETE CASCADE,
    grade          INT    NOT NULL,
    PRIMARY KEY (id)
);


INSERT INTO roles
values (default, 'CLIENT');
INSERT INTO roles
values (default, 'ADMIN');


INSERT INTO application_state
values (default, 'REGISTERED');
INSERT INTO application_state
values (default, 'CANCELED');
INSERT INTO application_state
values (default, 'REJECTED');
INSERT INTO application_state
values (default, 'ACCEPTED_FOR_CONTRACT');
INSERT INTO application_state
values (default, 'ACCEPTED_FOR_BUDGET');


INSERT INTO subject
values (default, 'Grade Point Average', 'Средний балл аттестата', 'Середній бал атестату');
INSERT INTO subject
values (default, 'Mathematics', 'Математика', 'Математика');
INSERT INTO subject
values (default, 'Physics', 'Физика', 'Фізика');
INSERT INTO subject
values (default, 'History', 'История', 'Історія');
INSERT INTO subject
values (default, 'English', 'Английский', 'Англійська');
INSERT INTO subject
values (default, 'Biology', 'Биология', 'Біологія');
