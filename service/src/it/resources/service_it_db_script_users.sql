insert into leaflet_users
    (id, date_created, is_enabled, date_last_modified, default_locale, email, date_last_login, password, role, username)
values
    (1, '2016-08-18T10:00:00Z', true, '2016-08-18T10:00:00Z', 'EN', 'lflt-it-5101@leaflet.dev', null, 'lflt1234', 'ADMIN', 'Administrator'),
    (2, '2016-08-18T10:01:00Z', true, '2016-08-18T10:01:00Z', 'EN', 'lflt-it-5102@leaflet.dev', '2016-08-18T17:00:00Z', 'lflt1234', 'USER', 'User Alpha'),
    (3, '2016-08-18T10:02:00Z', true, '2016-08-18T10:02:00Z', 'HU', 'lflt-it-5103@leaflet.dev', '2016-08-18T17:30:00Z', 'lflt1234', 'USER', 'User Beta'),
    (4, '2016-08-18T10:03:00Z', false, '2016-08-18T10:03:00Z', 'HU', 'lflt-it-5104@leaflet.dev', null, 'lflt1234', 'USER', 'User Gamma'),
    (5, '2016-08-18T10:04:00Z', true, '2016-08-18T10:04:00Z', 'EN', 'lflt-it-5105@leaflet.dev', null, 'lflt1234', 'USER', 'User Delta');