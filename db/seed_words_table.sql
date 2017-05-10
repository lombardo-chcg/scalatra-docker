
\connect api;
copy words(word, canonical_word, points) from '/dict.csv' DELIMITER ',' CSV;