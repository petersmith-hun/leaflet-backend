insert into leaflet_users
    (id, date_created, is_enabled, date_last_modified, default_locale, email, date_last_login, password, role, username)
values
    (1, '2016-08-18 10:00:00', true, '2016-08-18 10:00:00', 'EN', 'lflt-it-5101@leaflet.dev', null, 'lflt1234', 'ADMIN', 'Administrator'),
    (2, '2016-08-18 10:01:00', true, '2016-08-18 10:01:00', 'EN', 'lflt-it-5102@leaflet.dev', '2016-08-18 17:00:00', 'lflt1234', 'USER', 'User Alpha'),
    (3, '2016-08-18 10:02:00', true, '2016-08-18 10:02:00', 'HU', 'lflt-it-5103@leaflet.dev', '2016-08-18 17:30:00', 'lflt1234', 'USER', 'User Beta'),
    (4, '2016-08-18 10:03:00', false, '2016-08-18 10:03:00', 'HU', 'lflt-it-5104@leaflet.dev', null, 'lflt1234', 'USER', 'User Gamma'),
    (5, '2016-08-18 10:04:00', true, '2016-08-18 10:04:00', 'EN', 'lflt-it-5105@leaflet.dev', null, 'lflt1234', 'USER', 'User Delta');