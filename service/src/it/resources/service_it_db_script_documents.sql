-- constants
set @DEFAULT_DATE = '2016-08-18 10:00:00';
set @DOCUMENT_RAW_CONTENT_1 = 'markdown code document 1';
set @DOCUMENT_RAW_CONTENT_2 = 'markdown code document 2';
set @DOCUMENT_RAW_CONTENT_3 = 'markdown code document 3';
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
    (id, date_created, is_enabled, date_last_modified, raw_content, link, locale, seo_description, seo_keywords, seo_title, title, user_id)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, @DOCUMENT_RAW_CONTENT_1, @ENTRY_LINK_1, @DEFAULT_LOCALE, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @ENTRY_TITLE_1, @USER_ID),
    (2, @DEFAULT_DATE, true, @DEFAULT_DATE, @DOCUMENT_RAW_CONTENT_2, @ENTRY_LINK_2, @DEFAULT_LOCALE, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @ENTRY_TITLE_2, @USER_ID),
    (3, @DEFAULT_DATE, false, @DEFAULT_DATE, @DOCUMENT_RAW_CONTENT_3, @ENTRY_LINK_3, @DEFAULT_LOCALE, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @ENTRY_TITLE_3, @USER_ID),
    (4, @DEFAULT_DATE, true, @DEFAULT_DATE, @DOCUMENT_RAW_CONTENT_3, @ENTRY_LINK_4, @DEFAULT_LOCALE, @ENTRY_TITLE_4, @ENTRY_TITLE_4, @ENTRY_TITLE_4, @ENTRY_TITLE_4, @USER_ID);