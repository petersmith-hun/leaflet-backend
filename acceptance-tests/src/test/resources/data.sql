-- set constants
set @CREATED_DATE = '2017-12-14 20:00:00.000';
set @CREATED_DATE_MINUS_1D = '2017-12-13 20:00:00.000';
set @CREATED_DATE_MINUS_2D = '2017-12-12 20:00:00.000';
set @CREATED_DATE_MINUS_3D = '2017-12-11 20:00:00.000';
set @MODIFIED_DATE = '2017-12-18 20:00:00.000';
set @TEST_PW = 'testpw01';
set @MIME = 'image/jpeg';

-- insert DCP defaults
insert into
  leaflet_dynamic_config_properties(dcp_key, dcp_value)
values
  ('PAGE_TITLE', 'Default page title'),
  ('META_TITLE', 'Default meta title'),
  ('META_DESCRIPTION', 'Default meta description'),
  ('META_KEYWORDS', 'Default meta keywords');

-- insert test users
insert into
  leaflet_users(id, date_created, is_enabled, default_locale, email, role, username, password)
values
  (1, @CREATED_DATE, true, 'EN', 'test-admin@ac-leaflet.local', 'ADMIN', 'Administrator', @TEST_PW),
  (2, @CREATED_DATE, true, 'EN', 'test-user-1@ac-leaflet.local', 'USER', 'Test User 1', @TEST_PW),
  (3, @CREATED_DATE, true, 'EN', 'test-editor-2@ac-leaflet.local', 'EDITOR', 'Test Editor 2', @TEST_PW),
  (4, @CREATED_DATE, true, 'HU', 'test-user-3@ac-leaflet.local', 'USER', 'Test User 3', @TEST_PW),
  (5, @CREATED_DATE, true, 'HU', 'test-editor-4@ac-leaflet.local', 'EDITOR', 'Test Editor 4', @TEST_PW),
  (6, @CREATED_DATE, true, 'EN', 'test-editor-5@ac-leaflet.local', 'EDITOR', 'Test Editor 5', @TEST_PW),
  (7, @CREATED_DATE, true, 'EN', 'test-user-6@ac-leaflet.local', 'USER', 'Test User 6', @TEST_PW),
  (8, @CREATED_DATE, true, 'EN', 'test-user-7@ac-leaflet.local', 'USER', 'Test User 7', @TEST_PW),
  (9, @CREATED_DATE, true, 'EN', 'test-editor-8@ac-leaflet.local', 'EDITOR', 'Test Editor 8', @TEST_PW);

-- insert test categories
insert into
  leaflet_categories(id, date_created, is_enabled, title, description)
values
  (1, @CREATED_DATE, true, 'Test category #1', 'Description for test category #1'),
  (2, @CREATED_DATE, true, 'Test category #2', 'Description for test category #2'),
  (3, @CREATED_DATE, false, 'Test category #3', 'Description for test category #3');

-- insert test documents
insert into 
  leaflet_documents(id, date_created, is_enabled, date_last_modified, raw_content, link, locale, seo_description, seo_keywords, seo_title, title, user_id)
values
  (1, @CREATED_DATE, true, @CREATED_DATE, '## content-doc-1', 'doc-1', 'EN', 'Doc #1 SEO Desc', 'Doc #1 SEO Keywords', 'Doc #1 SEO Title', 'Doc #1 title', 1),
  (2, @CREATED_DATE, true, @CREATED_DATE, '## content-doc-2', 'doc-2', 'EN', 'Doc #2 SEO Desc', 'Doc #2 SEO Keywords', 'Doc #2 SEO Title', 'Doc #2 title', 1),
  (3, @CREATED_DATE, false, @CREATED_DATE, '## content-doc-3', 'doc-3', 'EN', 'Doc #3 SEO Desc', 'Doc #3 SEO Keywords', 'Doc #3 SEO Title', 'Doc #3 title', 3),
  (4, @CREATED_DATE, true, @CREATED_DATE, '## content-doc-4', 'doc-4', 'EN', 'Doc #4 SEO Desc', 'Doc #4 SEO Keywords', 'Doc #4 SEO Title', 'Doc #4 title', 3);

-- insert test entries
insert into
  leaflet_entries(id, category_id, date_created, is_enabled, date_last_modified, raw_content, link, locale, seo_description, seo_keywords, seo_title, title, user_id, prologue, status)
values
  (1, 1, '2017-12-13 20:00:01.000', true, @MODIFIED_DATE, '## content-entry-1', 'entry-1', 'EN', 'Entry #1 SEO Desc', 'Entry #1 SEO Keywords', 'Entry #1 SEO Title', 'Entry #01 title', 1, 'Prologue #1', 'PUBLIC'),
  (2, 1,'2017-12-13 20:00:02.000', true, @MODIFIED_DATE, '## content-entry-2', 'entry-2', 'EN', 'Entry #2 SEO Desc', 'Entry #2 SEO Keywords', 'Entry #2 SEO Title', 'Entry #02 title', 1, 'Prologue #2', 'DRAFT'),
  (3, 1, '2017-12-13 20:00:03.000', false, @MODIFIED_DATE, '## content-entry-3', 'entry-3', 'EN', 'Entry #3 SEO Desc', 'Entry #3 SEO Keywords', 'Entry #3 SEO Title', 'Entry #03 title', 3, 'Prologue #3', 'PUBLIC'),
  (4, 1, '2017-12-13 20:00:04.000', true, @MODIFIED_DATE, '## content-entry-4', 'entry-4', 'EN', 'Entry #4 SEO Desc', 'Entry #4 SEO Keywords', 'Entry #4 SEO Title', 'Entry #04 title', 3, 'Prologue #4', 'REVIEW'),
  (5, 1, '2017-12-13 20:00:05.000', true, @MODIFIED_DATE, '## content-entry-5', 'entry-5', 'EN', 'Entry #5 SEO Desc', 'Entry #5 SEO Keywords', 'Entry #5 SEO Title', 'Entry #05 title', 3, 'Prologue #5', 'REVIEW'),
  (6, 1, '2017-12-13 20:00:06.000', true, @MODIFIED_DATE, '## content-entry-6', 'entry-6', 'EN', 'Entry #6 SEO Desc', 'Entry #6 SEO Keywords', 'Entry #6 SEO Title', 'Entry #06 title', 3, 'Prologue #6', 'PUBLIC'),
  (7, 1, '2017-12-13 20:00:07.000', true, @MODIFIED_DATE, '## content-entry-7', 'entry-7', 'EN', 'Entry #7 SEO Desc', 'Entry #7 SEO Keywords', 'Entry #7 SEO Title', 'Entry #07 title', 3, 'Prologue #7', 'PUBLIC'),
  (8, 1, '2017-12-13 20:00:08.000', true, @MODIFIED_DATE, '## content-entry-8', 'entry-8', 'EN', 'Entry #8 SEO Desc', 'Entry #8 SEO Keywords', 'Entry #8 SEO Title', 'Entry #08 title', 3, 'Prologue #8', 'PUBLIC'),
  (9, 1, '2017-12-13 20:00:09.000', true, @MODIFIED_DATE, '## content-entry-9', 'entry-9', 'EN', 'Entry #9 SEO Desc', 'Entry #9 SEO Keywords', 'Entry #9 SEO Title', 'Entry #09 title', 3, 'Prologue #9', 'PUBLIC'),
  (10, 1, '2017-12-13 20:00:10.000', true, @MODIFIED_DATE, '## content-entry-10', 'entry-10', 'EN', 'Entry #10 SEO Desc', 'Entry #10 SEO Keywords', 'Entry #10 SEO Title', 'Entry #10 title', 3, 'Prologue #10', 'PUBLIC'),
  (11, 1, '2017-12-13 20:00:11.000', true, @MODIFIED_DATE, '## content-entry-11', 'entry-11', 'EN', 'Entry #11 SEO Desc', 'Entry #11 SEO Keywords', 'Entry #11 SEO Title', 'Entry #11 title', 3, 'Prologue #11', 'PUBLIC'),
  (12, 1, '2017-12-13 20:00:12.000', true, @MODIFIED_DATE, '## content-entry-12', 'entry-12', 'EN', 'Entry #12 SEO Desc', 'Entry #12 SEO Keywords', 'Entry #12 SEO Title', 'Entry #12 title', 1, 'Prologue #12', 'PUBLIC'),
  (13, 2, '2017-12-13 20:00:13.000', true, @MODIFIED_DATE, '## content-entry-13', 'entry-13', 'EN', 'Entry #13 SEO Desc', 'Entry #13 SEO Keywords', 'Entry #13 SEO Title', 'Entry #13 title', 1, 'Prologue #13', 'PUBLIC'),
  (14, 2, '2017-12-13 20:00:14.000', true, @MODIFIED_DATE, '## content-entry-14', 'entry-14', 'EN', 'Entry #14 SEO Desc', 'Entry #14 SEO Keywords', 'Entry #14 SEO Title', 'Entry #14 title', 3, 'Prologue #14', 'PUBLIC'),
  (15, 2, '2017-12-13 20:00:15.000', true, @MODIFIED_DATE, '## content-entry-15', 'entry-15', 'EN', 'Entry #15 SEO Desc', 'Entry #15 SEO Keywords', 'Entry #15 SEO Title', 'Entry #15 title', 1, 'Prologue #15', 'PUBLIC'),
  (16, 2, '2017-12-13 20:00:16.000', true, @MODIFIED_DATE, '## content-entry-16', 'entry-16', 'EN', 'Entry #16 SEO Desc', 'Entry #16 SEO Keywords', 'Entry #16 SEO Title', 'Entry #16 title', 1, 'Prologue #16', 'PUBLIC'),
  (17, 2, '2017-12-13 20:00:17.000', true, @MODIFIED_DATE, '## content-entry-17', 'entry-17', 'EN', 'Entry #17 SEO Desc', 'Entry #17 SEO Keywords', 'Entry #17 SEO Title', 'Entry #17 title', 3, 'Prologue #17', 'PUBLIC'),
  (18, 2, '2017-12-13 20:00:18.000', true, @MODIFIED_DATE, '## content-entry-18', 'entry-18', 'EN', 'Entry #18 SEO Desc', 'Entry #18 SEO Keywords', 'Entry #18 SEO Title', 'Entry #18 title', 3, 'Prologue #18', 'PUBLIC'),
  (19, 2, '2017-12-13 20:00:19.000', true, @MODIFIED_DATE, '## content-entry-19', 'entry-19', 'EN', 'Entry #19 SEO Desc', 'Entry #19 SEO Keywords', 'Entry #19 SEO Title', 'Entry #19 title', 3, 'Prologue #19', 'DRAFT'),
  (20, 2, '2017-12-13 20:00:20.000', true, @MODIFIED_DATE, '## content-entry-20', 'entry-20', 'EN', 'Entry #20 SEO Desc', 'Entry #20 SEO Keywords', 'Entry #20 SEO Title', 'Entry #20 title', 3, 'Prologue #20', 'DRAFT'),
  (21, 2, '2017-12-13 20:00:21.000', true, @MODIFIED_DATE, '## content-entry-21', 'entry-21', 'EN', 'Entry #21 SEO Desc', 'Entry #21 SEO Keywords', 'Entry #21 SEO Title', 'Entry #21 title', 3, 'Prologue #21', 'PUBLIC'),
  (22, 2, '2017-12-13 20:00:22.000', true, @MODIFIED_DATE, '## content-entry-22', 'entry-22', 'EN', 'Entry #22 SEO Desc', 'Entry #22 SEO Keywords', 'Entry #22 SEO Title', 'Entry #22 title', 3, 'Prologue #22', 'PUBLIC'),
  (23, 2, '2017-12-13 20:00:23.000', true, @MODIFIED_DATE, '## content-entry-23', 'entry-23', 'EN', 'Entry #23 SEO Desc', 'Entry #23 SEO Keywords', 'Entry #23 SEO Title', 'Entry #23 title', 3, 'Prologue #23', 'PUBLIC'),
  (24, 2, '2017-12-13 20:00:24.000', true, @MODIFIED_DATE, '## content-entry-24', 'entry-24', 'EN', 'Entry #24 SEO Desc', 'Entry #24 SEO Keywords', 'Entry #24 SEO Title', 'Entry #24 title', 3, 'Prologue #24', 'PUBLIC'),
  (25, 2, '2017-12-13 20:00:25.000', true, @MODIFIED_DATE, '## content-entry-25', 'entry-25', 'EN', 'Entry #25 SEO Desc', 'Entry #25 SEO Keywords', 'Entry #25 SEO Title', 'Entry #25 title', 3, 'Prologue #25', 'PUBLIC');

-- insert test comments
insert into 
  leaflet_comments(id, date_created, is_enabled, is_deleted, date_last_modified, content, entry_id, user_id)
values
  (1, '2017-12-13 20:00:01.000', true, false, @MODIFIED_DATE, 'Test comment 1', 1, 2),
  (2, '2017-12-13 20:00:02.000', true, false, @MODIFIED_DATE, 'Test comment 2', 1, 2),
  (3, '2017-12-13 20:00:04.000', true, true, @MODIFIED_DATE, 'Test comment 3', 1, 4),
  (4, '2017-12-13 20:00:05.000', true, false, @MODIFIED_DATE, 'Test comment 4', 1, 2),
  (5, '2017-12-13 20:00:06.000', false, false, @MODIFIED_DATE, 'Test comment 5', 1, 4),
  (6, '2017-12-13 20:00:07.000', true, true, @MODIFIED_DATE, 'Test comment 6', 1, 2),
  (7, '2017-12-13 20:00:08.000', true, true, @MODIFIED_DATE, 'Test comment 7', 1, 2),
  (8, '2017-12-13 20:00:09.000', false, false, @MODIFIED_DATE, 'Test comment 8', 1, 4),
  (9, '2017-12-13 20:00:10.000', false, false, @MODIFIED_DATE, 'Test comment 9', 1, 4),
  (10, '2017-12-13 20:00:11.000', true, true, @MODIFIED_DATE, 'Test comment 10', 1, 2);

-- insert test tags
insert into leaflet_tags
  (id, date_created, is_enabled, date_last_modified, title)
values
  (1, @CREATED_DATE, true, @CREATED_DATE, 'Tag #1'),
  (2, @CREATED_DATE, true, @CREATED_DATE, 'Tag #2'),
  (3, @CREATED_DATE, false, @CREATED_DATE, 'Tag #3'),
  (4, @CREATED_DATE, false, @CREATED_DATE, 'Tag #4'),
  (5, @CREATED_DATE, true, @CREATED_DATE, 'Tag #5'),
  (6, @CREATED_DATE, false, @CREATED_DATE, 'Tag #6'),
  (7, @CREATED_DATE, true, @CREATED_DATE, 'Tag #7'),
  (8, @CREATED_DATE, true, @CREATED_DATE, 'Tag #8'),
  (9, @CREATED_DATE, true, @CREATED_DATE, 'Tag #9');

-- insert uploaded file information
insert into 
  leaflet_uploaded_files(id, date_created, is_enabled, date_last_modified, description, mime, original_filename, path, path_uuid, stored_filename)
values
  (1, @CREATED_DATE, true, null, 'Uploaded file #1', @MIME, 'original_filename_1.jpg', 'images/stored_filename_1.jpg', 'd4b1830d-f368-37a0-88f9-2faf7fa8ded6', 'stored_filename_1.jpg'),
  (2, @CREATED_DATE, true, null, 'Uploaded file #2', @MIME, 'original_filename_2.jpg', 'images/stored_filename_2.jpg', 'a167450b-e162-309d-bac4-fb5149d10512', 'stored_filename_2.jpg'),
  (3, @CREATED_DATE, true, null, 'Uploaded file #3', @MIME, 'original_filename_3.jpg', 'images/test_sub/stored_filename_3.jpg', '058c9d47-6ce4-3e48-9c44-35bb9c74b378', 'stored_filename_3.jpg');

-- join some files and entries (attachments)
insert into
  leaflet_entries_uploaded_files(entry_id, uploaded_file_id)
values
  (1, 1),
  (1, 2),
  (2, 3);

-- join some tags to entries
insert into
  leaflet_entries_tags(entry_id, tag_id)
values
  (1, 1),
  (1, 2),
  (1, 3),
  (2, 2),
  (2, 4),
  (3, 5),
  (3, 6),
  (3, 7),
  (4, 1),
  (4, 2),
  (4, 8),
  (5, 4);

-- insert test routes
insert into leaflet_front_end_routes
(id, date_created, is_enabled, date_last_modified, name, route_id, url, sequence_number, type, auth_requirement)
values
  (1, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 1', 'route-1', '/route/header/1', 1, 'HEADER_MENU', 'AUTHENTICATED'),
  (2, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 2', 'route-2', '/route/header/2', 2, 'HEADER_MENU', 'SHOW_ALWAYS'),
  (3, @CREATED_DATE, false, @MODIFIED_DATE, 'Route 3', 'route-3', '/route/header/3', 3, 'HEADER_MENU', 'SHOW_ALWAYS'),
  (4, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 4', 'route-4', '/route/footer/1', 4, 'FOOTER_MENU', 'ANONYMOUS'),
  (5, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 5', 'route-5', '/route/footer/2', 5, 'FOOTER_MENU', 'SHOW_ALWAYS'),
  (6, @CREATED_DATE, false, @MODIFIED_DATE, 'Route 6', 'route-6', '/route/footer/3', 6, 'FOOTER_MENU', 'SHOW_ALWAYS'),
  (7, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 7', 'route-7', '/route/standalone/1', 7, 'STANDALONE', 'SHOW_ALWAYS'),
  (8, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 8', 'route-8', '/route/standalone/2', 8, 'STANDALONE', 'SHOW_ALWAYS'),
  (9, @CREATED_DATE, false, @MODIFIED_DATE, 'Route 9', 'route-9', '/route/standalone/3', 9, 'STANDALONE', 'SHOW_ALWAYS'),
  (10, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 10', 'route-10', '/route/dynamic/entry-pattern-1', 10, 'ENTRY_ROUTE_MASK', 'SHOW_ALWAYS'),
  (11, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 11', 'route-11', '/route/dynamic/category', 11, 'CATEGORY_ROUTE_MASK', 'SHOW_ALWAYS'),
  (12, @CREATED_DATE, true, @MODIFIED_DATE, 'Route 12', 'route-12', '/route/dynamic/entry-pattern-2', 12, 'ENTRY_ROUTE_MASK', 'AUTHENTICATED'),
  (13, @CREATED_DATE, false, @MODIFIED_DATE, 'Route 13', 'route-13', '/route/dynamic/entry-pattern-3', 13, 'ENTRY_ROUTE_MASK', 'ANONYMOUS');
