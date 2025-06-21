CREATE TABLE addresses
(
    id            UUID       NOT NULL,
    street_line_1 TEXT       NOT NULL,
    street_line_2 TEXT,
    city          TEXT       NOT NULL,
    province      TEXT       NOT NULL,
    postal_code   VARCHAR(5) NOT NULL,
    country       TEXT       NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE allergies
(
    id          UUID NOT NULL,
    name        TEXT NOT NULL,
    description TEXT,
    severity    TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_allergies PRIMARY KEY (id)
);

CREATE TABLE attendance
(
    id          UUID NOT NULL,
    child_id    UUID NOT NULL,
    facility_id UUID,
    check_in    TIMESTAMP WITHOUT TIME ZONE,
    check_out   TIMESTAMP WITHOUT TIME ZONE,
    status      TEXT,
    notes       TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_attendance PRIMARY KEY (id)
);

CREATE TABLE billing
(
    id          UUID NOT NULL,
    guardian_id UUID NOT NULL,
    amount      DECIMAL(65, 30),
    due_date    TIMESTAMP WITHOUT TIME ZONE,
    status      TEXT,
    description TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_billing PRIMARY KEY (id)
);

CREATE TABLE child_allergies
(
    id         UUID NOT NULL,
    child_id   UUID NOT NULL,
    allergy_id UUID NOT NULL,
    notes      TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_child_allergies PRIMARY KEY (id)
);

CREATE TABLE child_guardians
(
    id               UUID                  NOT NULL,
    guardian_id      UUID                  NOT NULL,
    child_id         UUID                  NOT NULL,
    primary_guardian BOOLEAN DEFAULT FALSE NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_child_guardians PRIMARY KEY (id)
);

CREATE TABLE children
(
    id                UUID NOT NULL,
    first_name        TEXT NOT NULL,
    last_name         TEXT NOT NULL,
    dob               TIMESTAMP WITHOUT TIME ZONE,
    gender            TEXT,
    special_needs     TEXT,
    emergency_contact TEXT,
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at        TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    guardian_id       UUID,
    CONSTRAINT pk_children PRIMARY KEY (id)
);

CREATE TABLE enrollments
(
    id                      UUID NOT NULL,
    child_id                UUID NOT NULL,
    start_date              TIMESTAMP WITHOUT TIME ZONE,
    end_date                TIMESTAMP WITHOUT TIME ZONE,
    status                  TEXT,
    enrolled_by_guardian_id UUID,
    admin_sign_off_id       UUID,
    created_at              TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at              TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_enrollments PRIMARY KEY (id)
);

CREATE TABLE event_publication
(
    id              UUID NOT NULL,
    completion_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_event_publication PRIMARY KEY (id)
);

CREATE TABLE event_publication_archive
(
    id              UUID NOT NULL,
    completion_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_event_publication_archive PRIMARY KEY (id)
);

CREATE TABLE facilities
(
    id             UUID NOT NULL,
    name           TEXT NOT NULL,
    address_id     UUID,
    phone          TEXT,
    license_number TEXT,
    max_capacity   INTEGER,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_facilities PRIMARY KEY (id)
);

CREATE TABLE guardians
(
    id                UUID                  NOT NULL,
    user_id           UUID                  NOT NULL,
    relationship      TEXT                  NOT NULL,
    emergency_contact TEXT,
    pickup_authorized BOOLEAN DEFAULT FALSE NOT NULL,
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at        TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_guardians PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id           UUID                  NOT NULL,
    sender_id    UUID                  NOT NULL,
    recipient_id UUID                  NOT NULL,
    subject      TEXT,
    content      TEXT,
    read_status  BOOLEAN DEFAULT FALSE NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE payments
(
    id             UUID NOT NULL,
    billing_id     UUID NOT NULL,
    amount         DECIMAL(65, 30),
    payment_date   TIMESTAMP WITHOUT TIME ZONE,
    payment_method TEXT,
    transaction_id TEXT,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_payments PRIMARY KEY (id)
);

CREATE TABLE reports
(
    id          UUID         NOT NULL,
    report_type VARCHAR(255) NOT NULL,
    report_data TEXT         NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_reports PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            UUID NOT NULL,
    clerk_user_id TEXT NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE               NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE facilities
    ADD CONSTRAINT uc_facilities_address UNIQUE (address_id);

ALTER TABLE attendance
    ADD CONSTRAINT FK_ATTENDANCE_ON_CHILD FOREIGN KEY (child_id) REFERENCES children (id) ON DELETE CASCADE;

ALTER TABLE attendance
    ADD CONSTRAINT FK_ATTENDANCE_ON_FACILITY FOREIGN KEY (facility_id) REFERENCES facilities (id);

ALTER TABLE billing
    ADD CONSTRAINT FK_BILLING_ON_GUARDIAN FOREIGN KEY (guardian_id) REFERENCES guardians (id) ON DELETE CASCADE;

ALTER TABLE children
    ADD CONSTRAINT FK_CHILDREN_ON_GUARDIAN FOREIGN KEY (guardian_id) REFERENCES guardians (id) ON DELETE SET NULL;

ALTER TABLE child_allergies
    ADD CONSTRAINT FK_CHILD_ALLERGIES_ON_ALLERGY FOREIGN KEY (allergy_id) REFERENCES allergies (id) ON DELETE CASCADE;

ALTER TABLE child_allergies
    ADD CONSTRAINT FK_CHILD_ALLERGIES_ON_CHILD FOREIGN KEY (child_id) REFERENCES children (id) ON DELETE CASCADE;

ALTER TABLE child_guardians
    ADD CONSTRAINT FK_CHILD_GUARDIANS_ON_CHILD FOREIGN KEY (child_id) REFERENCES children (id) ON DELETE CASCADE;

ALTER TABLE child_guardians
    ADD CONSTRAINT FK_CHILD_GUARDIANS_ON_GUARDIAN FOREIGN KEY (guardian_id) REFERENCES guardians (id) ON DELETE CASCADE;

ALTER TABLE enrollments
    ADD CONSTRAINT FK_ENROLLMENTS_ON_ADMIN_SIGN_OFF FOREIGN KEY (admin_sign_off_id) REFERENCES users (id);

ALTER TABLE enrollments
    ADD CONSTRAINT FK_ENROLLMENTS_ON_CHILD FOREIGN KEY (child_id) REFERENCES children (id) ON DELETE CASCADE;

ALTER TABLE enrollments
    ADD CONSTRAINT FK_ENROLLMENTS_ON_ENROLLED_BY_GUARDIAN FOREIGN KEY (enrolled_by_guardian_id) REFERENCES child_guardians (id);

ALTER TABLE facilities
    ADD CONSTRAINT FK_FACILITIES_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES addresses (id);

ALTER TABLE guardians
    ADD CONSTRAINT FK_GUARDIANS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE payments
    ADD CONSTRAINT FK_PAYMENTS_ON_BILLING FOREIGN KEY (billing_id) REFERENCES billing (id) ON DELETE CASCADE;