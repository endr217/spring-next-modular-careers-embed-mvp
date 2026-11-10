CREATE TABLE users (
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    surname           VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    email_verified_at TIMESTAMP(6) WITH TIME ZONE,
    password          VARCHAR(255) NOT NULL,
    status            SMALLINT     NOT NULL DEFAULT 1,
    stripe_id         VARCHAR(255),
    created_at        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at        TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_users_stripe_id ON users (stripe_id);

CREATE TABLE password_reset_tokens (
    email      VARCHAR(255) PRIMARY KEY,
    token      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

CREATE TABLE companies (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    slug       VARCHAR(255) NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    logo_path  VARCHAR(255),
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

CREATE TABLE company_user (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    company_id BIGINT      NOT NULL REFERENCES companies (id) ON DELETE CASCADE,
    role       VARCHAR(16) NOT NULL DEFAULT 'owner' CHECK (role IN ('owner', 'admin', 'viewer')),
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    UNIQUE (user_id, company_id)
);

CREATE TABLE websites (
    id             BIGSERIAL PRIMARY KEY,
    company_id     BIGINT       NOT NULL REFERENCES companies (id) ON DELETE CASCADE,
    name           VARCHAR(255) NOT NULL,
    domain         VARCHAR(255) NOT NULL,
    slug           VARCHAR(255) NOT NULL UNIQUE,
    logo_path      VARCHAR(255),
    branding_color VARCHAR(255),
    font_family    VARCHAR(255),
    theme          VARCHAR(16)  NOT NULL DEFAULT 'light' CHECK (theme IN ('light', 'dark')),
    created_at     TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at     TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_websites_company_id ON websites (company_id);

CREATE TABLE job_postings (
    id           BIGSERIAL PRIMARY KEY,
    website_id   BIGINT       NOT NULL REFERENCES websites (id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL,
    location     VARCHAR(255) NOT NULL,
    job_type     VARCHAR(16)  NOT NULL CHECK (job_type IN ('full-time', 'part-time', 'freelance')),
    salary_from  INTEGER,
    salary_to    INTEGER,
    description  TEXT         NOT NULL,
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    slug         VARCHAR(255) NOT NULL UNIQUE,
    published_at TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at   TIMESTAMP(6) WITH TIME ZONE,
    created_at   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_job_postings_website_id ON job_postings (website_id);

CREATE TABLE applications (
    id              BIGSERIAL PRIMARY KEY,
    job_posting_id  BIGINT       NOT NULL REFERENCES job_postings (id) ON DELETE CASCADE,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    motivation_text TEXT,
    cv_file_path    VARCHAR(255) NOT NULL,
    status          VARCHAR(16)  NOT NULL DEFAULT 'new'
        CHECK (status IN ('new', 'reviewed', 'accepted', 'rejected')),
    submitted_at    TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at      TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_applications_job_posting_id ON applications (job_posting_id);
CREATE INDEX idx_applications_status ON applications (status);

CREATE TABLE subscriptions (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    type          VARCHAR(255) NOT NULL,
    stripe_id     VARCHAR(255) NOT NULL UNIQUE,
    stripe_status VARCHAR(255) NOT NULL,
    stripe_price  VARCHAR(255),
    quantity      BIGINT,
    ends_at       TIMESTAMP(6) WITH TIME ZONE,
    created_at    TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_subscriptions_user_status ON subscriptions (user_id, stripe_status);
