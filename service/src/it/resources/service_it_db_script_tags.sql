-- constants
set @DEFAULT_DATE = '2016-08-18T10:00:00Z';

-- add some tags
insert into leaflet_tags
    (id, date_created, is_enabled, date_last_modified, title)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #1'),
    (2, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #2'),
    (3, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #3'),
    (4, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #4'),
    (5, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #5'),
    (6, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Tag #6'),
    (7, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #7'),
    (8, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #8'),
    (9, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #9'),
    (10, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #10'),
    (11, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Tag #11'),
    (12, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Tag #12'),
    (13, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #13'),
    (14, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #14'),
    (15, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #15'),
    (16, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #16'),
    (17, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Tag #17'),
    (18, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Tag #18'),
    (19, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Tag #19'),
    (20, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Tag #20');

insert into leaflet_entries_tags
    (entry_id, tag_id)
values
    (1, 20);