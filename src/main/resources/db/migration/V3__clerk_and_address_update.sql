ALTER TABLE users
    ALTER COLUMN clerk_user_id TYPE VARCHAR(255) USING (clerk_user_id::VARCHAR(255));