-- constants
set @DEFAULT_DATE = '2017-05-13T15:00:00Z';
set @MIME = 'image/jpeg';

-- add uploaded files
insert into leaflet_uploaded_files
    (id, date_created, is_enabled, date_last_modified, description, mime, original_filename, path, path_uuid, stored_filename)
values
    (1, @DEFAULT_DATE, true, null, 'Uploaded file #1', @MIME, 'original_filename_1.jpg', 'images/stored_filename_1.jpg', 'd4b1830d-f368-37a0-88f9-2faf7fa8ded6', 'stored_filename_1.jpg'),
    (2, @DEFAULT_DATE, true, null, 'Uploaded file #2', @MIME, 'original_filename_2.jpg', 'images/stored_filename_2.jpg', 'a167450b-e162-309d-bac4-fb5149d10512', 'stored_filename_2.jpg'),
    (3, @DEFAULT_DATE, true, null, 'Uploaded file #3', @MIME, 'original_filename_3.jpg', 'images/stored_filename_3.jpg', '058c9d47-6ce4-3e48-9c44-35bb9c74b378', 'stored_filename_3.jpg');

-- add connection to existing entries (requires service_it_db_script_entries.sql!)
insert into leaflet_entries_uploaded_files
    (entry_id, uploaded_file_id)
values
    (1, 3);