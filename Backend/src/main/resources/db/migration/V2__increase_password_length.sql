-- Augmentation de la longueur de la colonne "password" dans la table "app_user"
ALTER TABLE app_user
ALTER COLUMN password TYPE VARCHAR(255);