-- One-time setup: creates the app's database and a login role that owns it.
-- Run as the postgres superuser, choosing your own password:
--   psql -U postgres -v app_password='pick-a-password' -f server/db/setup.sql
-- Then give the server the same password via the DB_PASSWORD environment variable.

CREATE ROLE music_directory LOGIN PASSWORD :'app_password';
CREATE DATABASE music_directory OWNER music_directory;
