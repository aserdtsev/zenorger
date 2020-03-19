INSERT INTO service_user (id, login, password)
VALUES ('70f112fd-71ba-4b85-a066-833ee2988ecd', 'andrey.serdtsev@gmail.com', '$2a$10$c.F710SsYjsiNGAI1k.Ug.UEInOP9tm5c5t3hfkpHV8/ZPNLeQm46');

INSERT INTO organizer (id, user_id, name)
VALUES ('640021fc-4093-4dd4-84f2-5792a6116cb7', '70f112fd-71ba-4b85-a066-833ee2988ecd', 'Default organizer');

INSERT INTO context (id, organizer_id, name)
VALUES ('8d6b92ca-7a1c-47e7-ae21-173bbb06f0d7', '640021fc-4093-4dd4-84f2-5792a6116cb7', 'Car');
INSERT INTO context (id, organizer_id, name)
VALUES ('3d468462-eca5-4cb0-866e-9a579a144c24', '640021fc-4093-4dd4-84f2-5792a6116cb7', 'Home');
INSERT INTO context (id, organizer_id, name)
VALUES ('85e9364e-156e-4f63-aa48-2da1468c7160', '640021fc-4093-4dd4-84f2-5792a6116cb7', 'Shop');
INSERT INTO context (id, organizer_id, name)
VALUES ('42f49207-2e82-4009-9aa8-0ed5c4717584', '640021fc-4093-4dd4-84f2-5792a6116cb7', 'Work');