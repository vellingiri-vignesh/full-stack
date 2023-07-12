ALTER TABLE customer
    ADD COLUMN profile_image_id TEXT;

ALTER TABLE customer
ADD CONSTRAINT profile_image_id_unique
UNIQUE (profile_image_id)