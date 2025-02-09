/**
Key features:
- **Users table**: Core user information
- **Roles table**: Defined roles (e.g., admin, user, moderator)
- **Permissions table**: Individual permissions (e.g., read, write, delete)
- **User_roles**: Maps users to roles (many-to-many)
- **Role_permissions**: Maps roles to permissions (many-to-many)
- **User_sessions**: Manages active sessions
- **Password_reset_tokens**: Handles password reset functionality
- **Login_attempts**: Tracks login attempts for security

Security features:
- Password hashes instead of plain passwords
- Session management
- Login attempt tracking
- Password reset functionality

Performance optimizations:
- Appropriate indices on frequently queried columns
- UNIQUE constraints where needed
- Proper foreign key constraints with CASCADE delete where appropriate

The schema supports:
- Role-based access control (RBAC)
- Permission-based access control
- Session management
- Security monitoring
- Password reset functionality

To use this schema effectively, you should also implement appropriate application-level logic for:
- Password hashing
- Session management
- Token generation
- Permission checking
- Role assignment
 */
-- Users table
CREATE TABLE IF NOT EXISTS auth_users
(
    user_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN   DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Roles table
CREATE TABLE IF NOT EXISTS auth_roles
(
    role_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Permissions table
CREATE TABLE IF NOT EXISTS auth_permissions
(
    permission_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    permission_name VARCHAR(50) NOT NULL UNIQUE,
    description     TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User-Role junction table (many-to-many)
CREATE TABLE IF NOT EXISTS auth_user_roles
(
    user_id     INTEGER,
    role_id     INTEGER,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES auth_roles (role_id) ON DELETE CASCADE
);

-- Role-Permission junction table (many-to-many)
CREATE TABLE IF NOT EXISTS auth_role_permissions
(
    role_id       INTEGER,
    permission_id INTEGER,
    assigned_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES auth_roles (role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES auth_permissions (permission_id) ON DELETE CASCADE
);

-- User sessions table
CREATE TABLE IF NOT EXISTS auth_user_sessions
(
    session_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id    INTEGER      NOT NULL,
    token      VARCHAR(255) NOT NULL UNIQUE,
    ip_address VARCHAR(45),
    user_agent TEXT,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
);

-- Password reset tokens
CREATE TABLE IF NOT EXISTS auth_password_reset_tokens
(
    token_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id    INTEGER      NOT NULL,
    token      VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
);

-- Login attempts tracking
CREATE TABLE IF NOT EXISTS log_auth_login_attempts
(
    attempt_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id      INTEGER,
    ip_address   VARCHAR(45) NOT NULL,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    success      BOOLEAN   DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE SET NULL
);

-- Indices for better performance
CREATE INDEX idx_users_email ON auth_users (email);
CREATE INDEX idx_users_username ON auth_users (username);
CREATE INDEX idx_user_roles_user_id ON auth_user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON auth_user_roles (role_id);
CREATE INDEX idx_role_permissions_role_id ON auth_role_permissions (role_id);
CREATE INDEX idx_role_permissions_permission_id ON auth_role_permissions (permission_id);
CREATE INDEX idx_user_sessions_user_id ON auth_user_sessions (user_id);
CREATE INDEX idx_user_sessions_token ON auth_user_sessions (token);
CREATE INDEX idx_password_reset_tokens_user_id ON auth_password_reset_tokens (user_id);
CREATE INDEX idx_password_reset_tokens_token ON auth_password_reset_tokens (token);
CREATE INDEX idx_login_attempts_user_id ON log_auth_login_attempts (user_id);
CREATE INDEX idx_login_attempts_ip ON log_auth_login_attempts (ip_address);

-- I'm adding the first admin use here because I can't get the CommandLineRunner to work.
-- TODO add roles and permissions for admin user
INSERT INTO auth_users
    (username, email, password, is_active)
VALUES ('admin',
        'admin@email.test',
        '{bcrypt}$2a$10$xb7bRNhQ8ihvpMUiA5uzKOOakmd0YT7z/cQgCQ0bP7VKpu/.AxSTW',
        TRUE);
