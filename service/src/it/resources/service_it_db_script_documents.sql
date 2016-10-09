-- constants
set @DEFAULT_DATE = '2016-08-18 10:00:00';
set @ENTRY_CONTENT_1 = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tristique urna libero, et viverra metus tempus nec. Nulla varius quam arcu, ut auctor mauris egestas mollis. Phasellus nec ex nec augue rhoncus vulputate. Nulla tempor diam eget egestas ultrices. Sed dignissim diam posuere sem hendrerit efficitur. Quisque condimentum, arcu vel euismod accumsan, nisi mi volutpat turpis, a pharetra sapien erat ut libero. Integer luctus eu nibh eu volutpat. Proin pulvinar blandit ipsum, ut pretium lorem tincidunt nec.';
set @ENTRY_CONTENT_2 = 'Duis commodo iaculis mi et efficitur. Sed at dolor tincidunt, convallis turpis a, vehicula sem. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In a velit et nisl varius cursus ut quis risus. Proin interdum nibh quis elit varius, ut faucibus massa lacinia. In hac habitasse platea dictumst. Ut sodales tellus in felis bibendum dignissim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Phasellus id tempor purus. Pellentesque malesuada laoreet fermentum. Nunc in quam vel massa venenatis pulvinar eu id leo. ';
set @ENTRY_CONTENT_3 = 'Morbi quis ex finibus, finibus felis vel, auctor lorem. Vestibulum gravida euismod sem, eu posuere lorem semper quis. Duis ac malesuada nunc, non ultrices ex. Etiam lectus sem, venenatis vel quam ac, faucibus suscipit dui. Maecenas varius sapien eu malesuada rhoncus. Integer mi risus, elementum sed mi ut, porta interdum ligula. Cras et feugiat odio, eu pellentesque velit. Proin blandit diam vestibulum, laoreet tellus quis, mattis lacus. In semper, arcu non aliquam fringilla, erat enim dictum justo, sed imperdiet arcu risus in turpis. Aliquam faucibus tortor elit, sit amet elementum est blandit sed. Maecenas condimentum ornare euismod. Ut pharetra dignissim semper. Vestibulum leo neque, consequat sit amet eros a, maximus pellentesque arcu. Aliquam tempus ornare nibh, at congue turpis. Vivamus eleifend vulputate pharetra. ';
set @ENTRY_TITLE_1 = 'Lorem ipsum dolor sit amet';
set @ENTRY_TITLE_2 = 'Duis commodo iaculis';
set @ENTRY_TITLE_3 = 'Morbi quis ex finibus';
set @ENTRY_TITLE_4 = 'Morbi quis ex finibus 2';
set @ENTRY_LINK_1 = 'lorem-ipsum-dolor-sit-amet-20160818';
set @ENTRY_LINK_2 = 'duis-commodo-iaculis-20160818';
set @ENTRY_LINK_3 = 'morbi-quis-ex-finibus-20160818';
set @ENTRY_LINK_4 = 'morbi-quis-ex-finibus-2-20161009';
set @USER_ID = 2;
set @DEFAULT_LOCALE = 'EN';

-- add test user
insert into leaflet_users
    (id, date_created, is_enabled, date_last_modified, default_locale, email, date_last_login, password, role, username)
values
    (@USER_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_LOCALE, 'lflt-it-5101@leaflet.dev', null, 'lflt1234', 'EDITOR', 'IT Editor');

-- add some entries
insert into leaflet_documents
    (id, date_created, is_enabled, date_last_modified, content, link, locale, seo_description, seo_keywords, seo_title, title, user_id)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_CONTENT_1, @ENTRY_LINK_1, @DEFAULT_LOCALE, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @USER_ID),
    (2, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_CONTENT_2, @ENTRY_LINK_2, @DEFAULT_LOCALE, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @USER_ID),
    (3, @DEFAULT_DATE, false, @DEFAULT_DATE, @ENTRY_CONTENT_3, @ENTRY_LINK_3, @DEFAULT_LOCALE, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @USER_ID),
    (4, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_CONTENT_3, @ENTRY_LINK_4, @DEFAULT_LOCALE, @ENTRY_TITLE_4, @ENTRY_TITLE_4, @ENTRY_TITLE_4, @ENTRY_TITLE_4, @USER_ID);