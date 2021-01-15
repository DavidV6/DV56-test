COPY users FROM '/data/allusers_pipe.txt' WITH CSV DELIMITER '|';
COPY venue FROM '/data/venue_pipe.txt' WITH CSV DELIMITER '|';
COPY category FROM '/data/category_pipe.txt' WITH CSV DELIMITER '|';
COPY date FROM '/data/date2008_pipe.txt' WITH CSV DELIMITER '|';
COPY event FROM '/data/allevents_pipe.txt' WITH CSV DELIMITER '|';
COPY listing FROM '/data/listings_pipe.txt' WITH CSV DELIMITER '|';
COPY sales FROM '/data/sales_tab.txt' WITH CSV DELIMITER '|';
