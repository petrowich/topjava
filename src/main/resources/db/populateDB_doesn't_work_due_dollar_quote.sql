TRUNCATE TABLE user_roles;
TRUNCATE TABLE meals;
TRUNCATE TABLE users CASCADE;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

DO $$
  DECLARE v_user_id int; v_admin_id int;
  BEGIN

    select id into v_user_id from users where email = 'user@yandex.ru';
    select id into v_admin_id from users where email = 'admin@gmail.com';

    INSERT INTO user_roles (role, user_id) VALUES
      ('ROLE_USER', v_user_id),
      ('ROLE_ADMIN', v_admin_id);

    insert into meals (user_id, datetime, description, calories) values
    (v_user_id, '2015-05-30 10:00:00', 'Завтрак', 500)
    ,(v_user_id, '2015-05-30 13:00:00', 'Обед', 1000)
    ,(v_user_id, '2015-05-30 20:00:00', 'Ужин', 500)
    ,(v_user_id, '2015-05-31 10:00:00', 'Завтрак', 500)
    ,(v_user_id, '2015-05-31 13:00:00', 'Обед', 1000)
    ,(v_user_id, '2015-05-31 20:00:00', 'Ужин', 510)
    ,(v_admin_id, '2015-05-30 10:00:00', 'Завтрак админа', 1000)
    ,(v_admin_id, '2015-05-30 13:00:00', 'Обед админа', 2000)
    ,(v_admin_id, '2015-05-30 20:00:00', 'Ужин админа', 1000);
  END;
$$;