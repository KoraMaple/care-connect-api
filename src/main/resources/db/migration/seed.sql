-- Seed addresses
INSERT INTO addresses (street_line_1, city, province, postal_code, country, updated_at)
VALUES
    ('123 Main St', 'Toronto', 'Ontario', 'M5V2K', 'Canada', NOW()),
    ('456 Elm St', 'Vancouver', 'British Columbia', 'V6B3E', 'Canada', NOW()),
    ('789 Oak St', 'Calgary', 'Alberta', 'T2P4L', 'Canada', NOW());

-- Seed users first (needed for guardians)
INSERT INTO users (clerk_user_id, updated_at)
VALUES
    ('user_2yWOOaF3FbQcBl4NXLp1qrGqUCc', NOW()),
    ('clerk_usr_987654321', NOW()),
    ('clerk_usr_567891234', NOW()),
    ('clerk_usr_432198765', NOW()),
    ('clerk_usr_135792468', NOW());

-- Seed guardians (depends on users)
INSERT INTO guardians (user_id, relationship, emergency_contact, pickup_authorized, updated_at)
SELECT id, 'Parent', '555-123-4567', TRUE, NOW() FROM users WHERE clerk_user_id = 'user_2yWOOaF3FbQcBl4NXLp1qrGqUCc'
UNION ALL
SELECT id, 'Mother', '555-987-6543', TRUE, NOW() FROM users WHERE clerk_user_id = 'clerk_usr_987654321'
UNION ALL
SELECT id, 'Father', '555-321-7654', TRUE, NOW() FROM users WHERE clerk_user_id = 'clerk_usr_567891234'
UNION ALL
SELECT id, 'Grandparent', '555-876-5432', TRUE, NOW() FROM users WHERE clerk_user_id = 'clerk_usr_432198765';

-- Seed children
INSERT INTO children (first_name, last_name, dob, gender, updated_at)
VALUES
    ('Emma', 'Smith', '2019-03-15 00:00:00', 'Female', NOW()),
    ('Liam', 'Johnson', '2020-07-22 00:00:00', 'Male', NOW()),
    ('Olivia', 'Williams', '2018-11-05 00:00:00', 'Female', NOW()),
    ('Noah', 'Brown', '2021-01-30 00:00:00', 'Male', NOW());

-- Associate children with guardians
UPDATE children SET guardian_id = (SELECT id FROM guardians LIMIT 1) WHERE first_name = 'Emma';
UPDATE children SET guardian_id = (SELECT id FROM guardians LIMIT 1 OFFSET 1) WHERE first_name = 'Liam';
UPDATE children SET guardian_id = (SELECT id FROM guardians LIMIT 1 OFFSET 2) WHERE first_name = 'Olivia';
UPDATE children SET guardian_id = (SELECT id FROM guardians LIMIT 1 OFFSET 3) WHERE first_name = 'Noah';

-- Create child_guardians relationships
INSERT INTO child_guardians (guardian_id, child_id, primary_guardian, updated_at)
SELECT g.id, c.id, TRUE, NOW()
FROM guardians g, children c
WHERE c.guardian_id = g.id;

-- Add secondary guardians for some children
INSERT INTO child_guardians (guardian_id, child_id, primary_guardian, updated_at)
SELECT
    (SELECT id FROM guardians ORDER BY id LIMIT 1 OFFSET 1),
    (SELECT id FROM children WHERE first_name = 'Emma'),
    FALSE,
    NOW();

-- Seed allergies
INSERT INTO allergies (name, description, severity, updated_at)
VALUES
    ('Peanuts', 'Severe peanut allergy', 'High', NOW()),
    ('Dairy', 'Lactose intolerance', 'Medium', NOW()),
    ('Gluten', 'Celiac disease', 'High', NOW()),
    ('Eggs', 'Egg allergy', 'Medium', NOW()),
    ('Bee Stings', 'Allergic to bee venom', 'High', NOW());

-- Associate allergies with children
INSERT INTO child_allergies (child_id, allergy_id, notes, updated_at)
SELECT
    (SELECT id FROM children WHERE first_name = 'Emma'),
    (SELECT id FROM allergies WHERE name = 'Peanuts'),
    'Carries EpiPen at all times',
    NOW();

INSERT INTO child_allergies (child_id, allergy_id, notes, updated_at)
SELECT
    (SELECT id FROM children WHERE first_name = 'Liam'),
    (SELECT id FROM allergies WHERE name = 'Dairy'),
    'Mild symptoms - upset stomach',
    NOW();

INSERT INTO child_allergies (child_id, allergy_id, notes, updated_at)
SELECT
    (SELECT id FROM children WHERE first_name = 'Olivia'),
    (SELECT id FROM allergies WHERE name = 'Gluten'),
    'Requires strict gluten-free diet',
    NOW();

-- Seed facilities (depends on addresses)
INSERT INTO facilities (name, address_id, phone, license_number, max_capacity, updated_at)
SELECT
    'Sunshine Daycare',
    id,
    '555-123-9876',
    'LIC123456',
    50,
    NOW()
FROM addresses WHERE street_line_1 = '123 Main St';

INSERT INTO facilities (name, address_id, phone, license_number, max_capacity, updated_at)
SELECT
    'Little Explorers Center',
    id,
    '555-987-6543',
    'LIC789012',
    75,
    NOW()
FROM addresses WHERE street_line_1 = '456 Elm St';

-- Seed attendance records
INSERT INTO attendance (child_id, facility_id, check_in, check_out, status, notes, updated_at)
SELECT
    c.id,
    f.id,
    NOW() - INTERVAL '8 hours',
    NOW() - INTERVAL '2 hours',
    'Completed',
    'Normal day',
    NOW()
FROM children c, facilities f
WHERE c.first_name = 'Emma' AND f.name = 'Sunshine Daycare';

INSERT INTO attendance (child_id, facility_id, check_in, status, notes, updated_at)
SELECT
    c.id,
    f.id,
    NOW() - INTERVAL '6 hours',
    'Present',
    'Currently attending',
    NOW()
FROM children c, facilities f
WHERE c.first_name = 'Liam' AND f.name = 'Little Explorers Center';

-- Seed enrollments
INSERT INTO enrollments (child_id, start_date, end_date, status, enrolled_by_guardian_id, updated_at)
SELECT
    c.id,
    '2023-01-15 00:00:00',
    '2023-12-15 00:00:00',
    'Active',
    cg.id,
    NOW()
FROM children c
         JOIN child_guardians cg ON c.id = cg.child_id
WHERE c.first_name = 'Emma' AND cg.primary_guardian = TRUE;

INSERT INTO enrollments (child_id, start_date, status, enrolled_by_guardian_id, updated_at)
SELECT
    c.id,
    '2023-03-01 00:00:00',
    'Active',
    cg.id,
    NOW()
FROM children c
         JOIN child_guardians cg ON c.id = cg.child_id
WHERE c.first_name = 'Liam' AND cg.primary_guardian = TRUE;

-- Seed billing records
INSERT INTO billing (guardian_id, amount, due_date, status, description, updated_at)
SELECT
    id,
    500.00,
    NOW() + INTERVAL '15 days',
    'Pending',
    'Monthly childcare fee - January',
    NOW()
FROM guardians
LIMIT 1;

INSERT INTO billing (guardian_id, amount, due_date, status, description, updated_at)
SELECT
    id,
    450.00,
    NOW() + INTERVAL '15 days',
    'Pending',
    'Monthly childcare fee - January',
    NOW()
FROM guardians
ORDER BY id
LIMIT 1 OFFSET 1;

-- Seed payments
INSERT INTO payments (billing_id, amount, payment_date, payment_method, transaction_id, updated_at)
SELECT
    id,
    250.00,
    NOW() - INTERVAL '5 days',
    'Credit Card',
    'TXN-123456',
    NOW()
FROM billing
LIMIT 1;

-- Seed messages
INSERT INTO messages (sender_id, recipient_id, subject, content, read_status, updated_at)
SELECT
    u1.id,
    u2.id,
    'Child Pickup Schedule',
    'Please note that I will be picking Emma up early tomorrow at 2PM for her doctor appointment.',
    FALSE,
    NOW()
FROM users u1, users u2
WHERE u1.clerk_user_id = 'user_2yWOOaF3FbQcBl4NXLp1qrGqUCc' AND u2.clerk_user_id = 'clerk_usr_135792468';

-- Seed reports
INSERT INTO reports (report_type, report_data, updated_at)
VALUES
    ('ATTENDANCE', '{"month": "January", "total_days": 22, "children_count": 45, "average_attendance": 40}', NOW()),
    ('FINANCIAL', '{"month": "December", "revenue": 22500, "expenses": 18750, "profit": 3750}', NOW());