-- DROP TABLE IF EXISTS user_groups;
DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS user_seq;
-- DROP TYPE IF EXISTS group_flag;
DROP TYPE IF EXISTS user_flag;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
-- CREATE TYPE group_flag AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE cities (
  id          INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  name        TEXT          NOT NULL,
  code        TEXT          NOT NULL
);
CREATE UNIQUE INDEX city_idx ON cities (code);

CREATE TABLE projects (
  id          INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  name        TEXT          NOT NULL,
  description TEXT          NOT NULL
);
CREATE UNIQUE INDEX project_idx ON projects (name);

-- CREATE TABLE groups (
-- id          INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
-- project_id  INTEGER       NOT NULL,
-- name        TEXT          NOT NULL,
-- flag        group_flag    NOT NULL,
-- FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
-- );
-- CREATE UNIQUE INDEX group_idx ON groups (name);

CREATE TABLE users (
  id          INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
--   city_id     INTEGER       NOT NULL,
  full_name   TEXT          NOT NULL,
  email       TEXT          NOT  NULL,
  flag        user_flag     NOT NULL
--   FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX email_idx ON users (email);

-- CREATE TABLE user_groups (
--   user_id     INTEGER       NOT NULL,
--   group_id    INTEGER       NOT NULL,
--   FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
--   FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
-- );