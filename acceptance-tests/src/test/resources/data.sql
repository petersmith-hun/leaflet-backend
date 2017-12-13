insert into
  leaflet_users(id, date_created, is_enabled, default_locale, email, role, username)
values
  (2, now(), true, 'EN', 'test-user-1@ac-leaflet.local', 'USER', 'Test User 1'),
  (3, now(), true, 'EN', 'test-user-2@ac-leaflet.local', 'USER', 'Test User 2'),
  (4, now(), true, 'HU', 'test-user-3@ac-leaflet.local', 'USER', 'Test User 3');

insert into
  leaflet_categories(date_created, is_enabled, title)
values
  (now(), true, 'Test category');