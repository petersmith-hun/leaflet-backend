-- constants
set @DEFAULT_DATE = '2016-08-18 10:00:00';
set @ENTRY_CONTENT = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tristique urna libero, et viverra metus tempus nec. Nulla varius quam arcu, ut auctor mauris egestas mollis. Phasellus nec ex nec augue rhoncus vulputate. Nulla tempor diam eget egestas ultrices. Sed dignissim diam posuere sem hendrerit efficitur. Quisque condimentum, arcu vel euismod accumsan, nisi mi volutpat turpis, a pharetra sapien erat ut libero. Integer luctus eu nibh eu volutpat. Proin pulvinar blandit ipsum, ut pretium lorem tincidunt nec.';
set @ENTRY_TITLE = 'Lorem ipsum dolor sit amet';
set @ENTRY_LINK = 'lorem-ipsum-dolor-sit-amet-20160818';
set @ENTRY_LINK_OTHER = 'lorem-ipsum-dolor-sit-amet-2-20160818';
set @COMMON_PROLOGUE = 'Fusce nec tortor vitae lorem volutpat finibus. Fusce condimentum diam sit amet leo pretium tincidunt. Duis vitae interdum dui. Nulla facilisi.';
set @CATEGORY_ID = 1;
set @USER_ID = 2;
set @USER_ID_OTHER = 3;
set @ENTRY_ID = 1;
set @ENTRY_ID_OTHER = 2;
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
    (@ENTRY_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_CONTENT, @ENTRY_LINK, @DEFAULT_LOCALE, @COMMON_PROLOGUE, @ENTRY_TITLE, @ENTRY_TITLE, @ENTRY_TITLE, @ENTRY_TITLE, @CATEGORY_ID, @USER_ID, @STATUS_PUBLIC),
    (@ENTRY_ID_OTHER, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_CONTENT, @ENTRY_LINK_OTHER, @DEFAULT_LOCALE, @COMMON_PROLOGUE, @ENTRY_TITLE, @ENTRY_TITLE, @ENTRY_TITLE, @ENTRY_TITLE, @CATEGORY_ID, @USER_ID, @STATUS_PUBLIC);

-- add test attachments
insert into leaflet_attachments
    (id, date_created, is_enabled, date_last_modified, description, filename, is_protected, title, type, entry_id)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Test attachment 1', 'attachment_1.jar', false, 'Attachment 1', 'application/java-archive', @ENTRY_ID),
    (2, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Test attachment 2', 'attachment_2.jar', false, 'Attachment 2', 'application/java-archive', @ENTRY_ID),
    (3, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Test attachment 3', 'attachment_3.jar', false, 'Attachment 3', 'application/java-archive', @ENTRY_ID_OTHER),
    (4, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Test attachment 4', 'attachment_4.jar', false, 'Attachment 4', 'application/java-archive', @ENTRY_ID),
    (5, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Test attachment 5', 'attachment_5.jar', false, 'Attachment 5', 'application/java-archive', @ENTRY_ID),
    (6, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Test attachment 6', 'attachment_6.jar', false, 'Attachment 6', 'application/java-archive', @ENTRY_ID_OTHER),
    (7, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Test attachment 7', 'attachment_7.jar', false, 'Attachment 7', 'application/java-archive', @ENTRY_ID_OTHER),
    (8, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Test attachment 8', 'attachment_8.jar', false, 'Attachment 8', 'application/java-archive', @ENTRY_ID),
    (9, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Test attachment 9', 'attachment_9.jar', false, 'Attachment 9', 'application/java-archive', @ENTRY_ID),
    (10, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Test attachment 10', 'attachment_10.jar', false, 'Attachment 10', 'application/java-archive', @ENTRY_ID);