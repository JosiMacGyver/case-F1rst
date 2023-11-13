CREATE SEQUENCE hibernate_sequence
  START WITH 1;

INSERT INTO product 
    (id, name, quantity, description, expiry_date) VALUES
    (nextval('hibernate_sequence'),'maça', 50, 'Branca de Neve', '2020-12-09'),
    (nextval('hibernate_sequence'),'banana', 90, 'Minion', '2023-12-12'),
    (nextval('hibernate_sequence'),'carambola', 30, 'Estrela', '2024-01-13');

  