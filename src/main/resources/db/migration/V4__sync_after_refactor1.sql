
ALTER TABLE users
    ALTER COLUMN clerk_user_id TYPE VARCHAR(255) USING (clerk_user_id::VARCHAR(255));

ALTER TABLE allergies
    ALTER COLUMN description TYPE VARCHAR(500) USING (description::VARCHAR(500));

ALTER TABLE children
    ALTER COLUMN dob SET NOT NULL;

ALTER TABLE children
    ALTER COLUMN emergency_contact TYPE VARCHAR(500) USING (emergency_contact::VARCHAR(500));

ALTER TABLE guardians
    ALTER COLUMN emergency_contact TYPE VARCHAR(500) USING (emergency_contact::VARCHAR(500));

ALTER TABLE children
    ALTER COLUMN first_name TYPE VARCHAR(100) USING (first_name::VARCHAR(100));

ALTER TABLE children
    ALTER COLUMN gender TYPE VARCHAR(10) USING (gender::VARCHAR(10));

ALTER TABLE children
    ALTER COLUMN last_name TYPE VARCHAR(100) USING (last_name::VARCHAR(100));

ALTER TABLE allergies
    ALTER COLUMN name TYPE VARCHAR(100) USING (name::VARCHAR(100));

ALTER TABLE guardians
    ALTER COLUMN relationship TYPE VARCHAR(50) USING (relationship::VARCHAR(50));

ALTER TABLE allergies
    ALTER COLUMN severity TYPE VARCHAR(20) USING (severity::VARCHAR(20));

ALTER TABLE children
    ALTER COLUMN special_needs TYPE VARCHAR(1000) USING (special_needs::VARCHAR(1000));