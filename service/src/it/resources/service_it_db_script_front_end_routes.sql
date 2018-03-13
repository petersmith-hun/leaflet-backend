-- constants
set @DEFAULT_DATE = '2018-03-13T20:00:00Z';

-- add routes
insert into leaflet_front_end_routes
    (id, date_created, is_enabled, date_last_modified, name, route_id, url, sequence_number, type)
values
    (1, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 1', 'route-1', '/route/header/1', 1, 'HEADER_MENU'),
    (2, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 2', 'route-2', '/route/header/2', 2, 'HEADER_MENU'),
    (3, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Route 3', 'route-3', '/route/header/3', 3, 'HEADER_MENU'),
    (4, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 4', 'route-4', '/route/footer/1', 4, 'FOOTER_MENU'),
    (5, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 5', 'route-5', '/route/footer/2', 5, 'FOOTER_MENU'),
    (6, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Route 6', 'route-6', '/route/footer/3', 6, 'FOOTER_MENU'),
    (7, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 7', 'route-7', '/route/standalone/1', 7, 'STANDALONE'),
    (8, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 8', 'route-8', '/route/standalone/2', 8, 'STANDALONE'),
    (9, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Route 9', 'route-9', '/route/standalone/3', 9, 'STANDALONE'),
    (10, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 10', 'route-10', '/route/dynamic/entry-pattern-1/%s', 10, 'ENTRY_ROUTE_MASK'),
    (11, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 11', 'route-11', '/route/dynamic/category/%s', 11, 'CATEGORY_ROUTE_MASK'),
    (12, @DEFAULT_DATE, true, @DEFAULT_DATE, 'Route 12', 'route-12', '/route/dynamic/entry-pattern-2/%s', 12, 'ENTRY_ROUTE_MASK'),
    (13, @DEFAULT_DATE, false, @DEFAULT_DATE, 'Route 13', 'route-13', '/route/dynamic/entry-pattern-3/%s', 13, 'ENTRY_ROUTE_MASK');