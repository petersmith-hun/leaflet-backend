-- constants
set @DEFAULT_DATE = '2016-08-18 10:00:00';
set @ENTRY_CONTENT = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tristique urna libero, et viverra metus tempus nec. Nulla varius quam arcu, ut auctor mauris egestas mollis. Phasellus nec ex nec augue rhoncus vulputate. Nulla tempor diam eget egestas ultrices. Sed dignissim diam posuere sem hendrerit efficitur. Quisque condimentum, arcu vel euismod accumsan, nisi mi volutpat turpis, a pharetra sapien erat ut libero. Integer luctus eu nibh eu volutpat. Proin pulvinar blandit ipsum, ut pretium lorem tincidunt nec.';
set @ENTRY_TITLE = 'Lorem ipsum dolor sit amet';
set @ENTRY_LINK = 'lorem-ipsum-dolor-sit-amet-20160818';
set @COMMON_PROLOGUE = 'Fusce nec tortor vitae lorem volutpat finibus. Fusce condimentum diam sit amet leo pretium tincidunt. Duis vitae interdum dui. Nulla facilisi.';
set @CATEGORY_ID = 1;
set @USER_ID = 2;
set @USER_ID_OTHER = 3;
set @ENTRY_ID = 1;
set @DEFAULT_LOCALE = 'EN';
set @STATUS_PUBLIC = 'PUBLIC';

-- add test user
insert into leaflet_users
    (id, date_created, is_enabled, date_last_modified, default_locale, email, date_last_login, password, role, username)
values
    (@USER_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_LOCALE, 'lflt-it-5101@leaflet.dev', null, 'lflt1234', 'EDITOR', 'IT Editor'),
    (@USER_ID_OTHER, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_LOCALE, 'lflt-it-5102@leaflet.dev', null, 'lflt1234', 'EDITOR', 'IT Editor 2');

-- add test category
insert into leaflet_categories
    (id, date_created, is_enabled, date_last_modified, description, title)
values
    (@CATEGORY_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Category for ITs', 'IT Test');

-- add test entry
insert into leaflet_entries
    (id, date_created, is_enabled, date_last_modified, content, link, locale, prologue, seo_description, seo_keywords, seo_title, title, category_id, user_id, status)
values
    (@ENTRY_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_CONTENT, @ENTRY_LINK, @DEFAULT_LOCALE, @COMMON_PROLOGUE, @ENTRY_TITLE, @ENTRY_TITLE, @ENTRY_TITLE, @ENTRY_TITLE, @CATEGORY_ID, @USER_ID, @STATUS_PUBLIC);

-- add some test comments
insert into leaflet_comments
    (id, date_created, is_enabled, is_deleted, date_last_modified, content, entry_id, user_id)
values
    (1, @DEFAULT_DATE, true, false, @DEFAULT_DATE, 'Test comment 1', @ENTRY_ID, @USER_ID),
    (2, @DEFAULT_DATE, true, false, @DEFAULT_DATE, 'Test comment 2', @ENTRY_ID, @USER_ID),
    (3, @DEFAULT_DATE, true, false, @DEFAULT_DATE, 'Test comment 3', @ENTRY_ID, @USER_ID_OTHER),
    (4, @DEFAULT_DATE, true, false, @DEFAULT_DATE, 'Test comment 4', @ENTRY_ID, @USER_ID),
    (5, @DEFAULT_DATE, false, false, @DEFAULT_DATE, 'Test comment 5', @ENTRY_ID, @USER_ID_OTHER),
    (6, @DEFAULT_DATE, true, false, @DEFAULT_DATE, 'Test comment 6', @ENTRY_ID, @USER_ID),
    (7, @DEFAULT_DATE, true, false, @DEFAULT_DATE, 'Test comment 7', @ENTRY_ID, @USER_ID),
    (8, @DEFAULT_DATE, false, false, @DEFAULT_DATE, 'Test comment 8', @ENTRY_ID, @USER_ID_OTHER),
    (9, @DEFAULT_DATE, false, false, @DEFAULT_DATE, 'Test comment 9', @ENTRY_ID, @USER_ID_OTHER),
    (10, @DEFAULT_DATE, true, true, @DEFAULT_DATE, 'Test comment 10', @ENTRY_ID, @USER_ID);
