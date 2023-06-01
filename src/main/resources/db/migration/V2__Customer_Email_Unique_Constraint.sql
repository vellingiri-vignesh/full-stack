ALTER TABLE customer
    ADD CONSTRAINT email_unique_constraint UNIQUE (email);
