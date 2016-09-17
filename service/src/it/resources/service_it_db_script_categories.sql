-- constants
set @DEFAULT_DATE = '2016-08-18 10:00:00';
set @DEFAULT_DESC = 'Control category object JSON descriptor';

-- add categories
insert into leaflet_categories
    (id, date_created, is_enabled, date_last_modified, description, title)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_DESC, 'IT category'),
    (2, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_DESC, 'IT category 2'),
    (3, @DEFAULT_DATE, true, @DEFAULT_DATE, @DEFAULT_DESC, 'IT category 3');