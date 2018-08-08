-- constants
set @DEFAULT_DATE = '2016-08-18 10:00:00';
set @ENTRY_RAW_CONTENT_1 = 'markdown code entry 1';
set @ENTRY_RAW_CONTENT_2 = 'markdown code entry 2';
set @ENTRY_RAW_CONTENT_3 = 'markdown code entry 3';
set @ENTRY_TITLE_1 = 'Lorem ipsum dolor sit amet';
set @ENTRY_TITLE_2 = 'Duis commodo iaculis';
set @ENTRY_TITLE_3 = 'Morbi quis ex finibus';
set @ENTRY_LINK_1 = 'lorem-ipsum-dolor-sit-amet-20160818';
set @ENTRY_LINK_2 = 'duis-commodo-iaculis-20160818';
set @ENTRY_LINK_3 = 'morbi-quis-ex-finibus-20160818';
set @COMMON_PROLOGUE = 'Fusce nec tortor vitae lorem volutpat finibus. Fusce condimentum diam sit amet leo pretium tincidunt. Duis vitae interdum dui. Nulla facilisi.';
set @CATEGORY_ID = 1;
set @USER_ID = 2;
set @DEFAULT_LOCALE = 'EN';
set @STATUS_DRAFT = 'DRAFT';
set @STATUS_REVIEW = 'REVIEW';
set @STATUS_PUBLIC = 'PUBLIC';

-- add test user
insert into leaflet_users
    (id, date_created, is_enabled, date_last_modified, default_locale, email, date_last_login, password, role, username)
values
    (@USER_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_LOCALE, 'lflt-it-5101@leaflet.dev', null, 'lflt1234', 'EDITOR', 'IT Editor');

-- add test category
insert into leaflet_categories
    (id, date_created, is_enabled, date_last_modified, description, title)
values
    (@CATEGORY_ID, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Category for ITs', 'IT Test');

-- add some entries
insert into leaflet_entries
    (id, date_created, is_enabled, date_last_modified, raw_content, link, locale, prologue, seo_description, seo_keywords, seo_title, title, category_id, user_id, status)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_RAW_CONTENT_1, @ENTRY_LINK_1, @DEFAULT_LOCALE, @COMMON_PROLOGUE, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @CATEGORY_ID, @USER_ID, @STATUS_PUBLIC),
    (2, @DEFAULT_DATE, false, @DEFAULT_DATE, @ENTRY_RAW_CONTENT_2, @ENTRY_LINK_2, @DEFAULT_LOCALE, @COMMON_PROLOGUE, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @CATEGORY_ID, @USER_ID, @STATUS_REVIEW),
    (3, @DEFAULT_DATE, true, @DEFAULT_DATE, @ENTRY_RAW_CONTENT_3, @ENTRY_LINK_3, @DEFAULT_LOCALE, @COMMON_PROLOGUE, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @CATEGORY_ID, @USER_ID, @STATUS_DRAFT);