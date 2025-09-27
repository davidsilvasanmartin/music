CREATE TABLE IF NOT EXISTS auth_users
(
    -- SERIAL should make it impossible to reuse old deleted ids (we need this for audit logging)
    user_id    SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN   DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS auth_roles
(
    role_id     SERIAL PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS auth_permissions
(
    permission_id   SERIAL PRIMARY KEY,
    permission_name VARCHAR(50) NOT NULL UNIQUE,
    description     TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS auth_user_roles
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER,
    role_id    INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES auth_roles (role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS auth_role_permissions
(
    id            SERIAL PRIMARY KEY,
    role_id       INTEGER,
    permission_id INTEGER,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES auth_roles (role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES auth_permissions (permission_id) ON DELETE CASCADE
);

-- Indices for better performance
CREATE INDEX idx_users_email ON auth_users (email);
CREATE INDEX idx_users_username ON auth_users (username);
CREATE INDEX idx_roles_role_name ON auth_roles (role_name);
CREATE INDEX idx_permissions_permission_name ON auth_permissions (permission_name);
CREATE INDEX idx_user_roles_user_id ON auth_user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON auth_user_roles (role_id);
CREATE INDEX idx_role_permissions_role_id ON auth_role_permissions (role_id);
CREATE INDEX idx_role_permissions_permission_id ON auth_role_permissions (permission_id);

/**
    I'm adding the first admin user and its permissions here because I can't get the CommandLineRunner to work.
    Ideally this would be done in the application, so as to not duplicate permission names in this file as
    well as in application code
 */
INSERT INTO auth_users
    (username, email, password, is_enabled)
VALUES ('admin',
        'admin@email.test',
        '{bcrypt}$2a$10$xb7bRNhQ8ihvpMUiA5uzKOOakmd0YT7z/cQgCQ0bP7VKpu/.AxSTW',
        TRUE);

INSERT INTO auth_roles(role_name, description)
VALUES ('ADMIN', 'Administrator role with full system access');

INSERT INTO auth_permissions(permission_name, description)
VALUES ('READ', 'Permission to read data'),
       ('CREATE', 'Permission to create data'),
       ('UPDATE', 'Permission to update data'),
       ('DELETE', 'Permission to delete data'),
       ('ADMIN', 'Administrative access');

-- Note that the "NOT EXISTS" clauses are not needed here because the tables are empty. However, we leave
-- these queries here because they will be useful later when creating migrations for adding new roles
-- or permissions
INSERT INTO auth_role_permissions(role_id, permission_id)
SELECT (SELECT role_id FROM auth_roles WHERE role_name = 'ADMIN'),
       permission_id
FROM auth_permissions ap
WHERE NOT EXISTS (SELECT 1
                  FROM auth_role_permissions rp
                  WHERE rp.role_id = (SELECT role_id FROM auth_roles WHERE role_name = 'ADMIN')
                    AND rp.permission_id = ap.permission_id);

INSERT INTO auth_user_roles(user_id, role_id)
SELECT (SELECT user_id FROM auth_users WHERE username = 'admin'),
       (SELECT role_id FROM auth_roles WHERE role_name = 'ADMIN')
WHERE NOT EXISTS (SELECT 1
                  FROM auth_user_roles ur
                  WHERE ur.user_id = (SELECT user_id FROM auth_users WHERE username = 'admin')
                    AND ur.role_id = (SELECT role_id FROM auth_roles WHERE role_name = 'ADMIN'));