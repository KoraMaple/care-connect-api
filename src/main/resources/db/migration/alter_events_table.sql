-- Add missing columns for Spring Modulith event publication

ALTER TABLE event_publication
    ADD COLUMN listener_id TEXT NOT NULL DEFAULT 'unknown',
    ADD COLUMN event_type TEXT NOT NULL DEFAULT 'unknown',
    ADD COLUMN serialized_event TEXT NOT NULL DEFAULT '{}',
    ADD COLUMN publication_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW();

ALTER TABLE event_publication_archive
    ADD COLUMN listener_id TEXT NOT NULL DEFAULT 'unknown',
    ADD COLUMN event_type TEXT NOT NULL DEFAULT 'unknown',
    ADD COLUMN serialized_event TEXT NOT NULL DEFAULT '{}',
    ADD COLUMN publication_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW();

-- Remove default values after adding columns
ALTER TABLE event_publication
    ALTER COLUMN listener_id DROP DEFAULT,
    ALTER COLUMN event_type DROP DEFAULT,
    ALTER COLUMN serialized_event DROP DEFAULT,
    ALTER COLUMN publication_date DROP DEFAULT;

ALTER TABLE event_publication_archive
    ALTER COLUMN listener_id DROP DEFAULT,
    ALTER COLUMN event_type DROP DEFAULT,
    ALTER COLUMN serialized_event DROP DEFAULT,
    ALTER COLUMN publication_date DROP DEFAULT;