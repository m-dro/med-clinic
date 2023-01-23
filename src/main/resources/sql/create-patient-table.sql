CREATE TABLE IF NOT EXISTS patient
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(50)        NOT NULL,
    last_name     VARCHAR(50)        NOT NULL,
    gender        INT                NOT NULL,
    date_of_birth DATE               NOT NULL,
    pesel         VARCHAR(11) UNIQUE NOT NULL
);

ALTER TABLE patient
    ADD CONSTRAINT fk_gender FOREIGN KEY (gender) REFERENCES gender_dict (gender_id) ON UPDATE CASCADE,
    ADD CONSTRAINT last_name_length_check CHECK (LENGTH(last_name) > 1),
    ADD CONSTRAINT first_name_length_check CHECK (LENGTH(first_name) > 2),
    ADD CONSTRAINT pesel_length_check CHECK (LENGTH(pesel) = 11);