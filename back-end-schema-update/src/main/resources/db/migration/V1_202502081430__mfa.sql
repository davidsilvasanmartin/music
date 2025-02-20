/**
# MFA (Multi-Factor Authentication) schema

Features:
- Supports multiple MFA methods simultaneously
- Stores backup codes for account recovery
- Tracks verification attempts
- Maintains audit trail
- Follows 3NF principles

Security considerations:
- Secret keys should be encrypted before storage (application-level)
- Backup codes should be hashed
- Phone numbers might need encryption depending on requirements
- Attempt tracking for rate limiting

Additional recommendations:
1. Implement rate limiting for MFA attempts
2. Encrypt sensitive data at the application level
3. Implement backup code generation and validation
4. Add application-level validation for phone numbers
5. Consider implementing automatic cleanup of old attempt records
6. Implement session handling that considers MFA status
 */

-- TODO review all
-- MFA Methods reference table
CREATE TABLE IF NOT EXISTS auth_mfa_methods
(
    method_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    method_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    is_active   BOOLEAN   DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User MFA settings (core configuration)
CREATE TABLE IF NOT EXISTS auth_mfa_user_settings
(
    mfa_user_setting_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id             INTEGER NOT NULL,
    method_id           INTEGER NOT NULL,
    is_enabled          BOOLEAN   DEFAULT FALSE,
    verified_at         TIMESTAMP,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (method_id) REFERENCES auth_mfa_methods (method_id) ON DELETE RESTRICT,
    UNIQUE (user_id, method_id)
);

-- TOTP-specific configurations
CREATE TABLE IF NOT EXISTS auth_mfa_user_totp
(
    totp_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    user_mfa_id INTEGER      NOT NULL UNIQUE,
    secret_key  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_mfa_id) REFERENCES auth_mfa_user_settings (mfa_user_setting_id) ON DELETE CASCADE
);

-- SMS-specific configurations
CREATE TABLE IF NOT EXISTS auth_mfa_user_sms
(
    sms_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    user_mfa_id  INTEGER     NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_mfa_id) REFERENCES auth_mfa_user_settings (mfa_user_setting_id) ON DELETE CASCADE
);

-- Backup codes
CREATE TABLE IF NOT EXISTS auth_mfa_user_backup_codes
(
    backup_code_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_mfa_id    INTEGER      NOT NULL,
    code_hash      VARCHAR(255) NOT NULL,
    is_used        BOOLEAN   DEFAULT FALSE,
    used_at        TIMESTAMP,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_mfa_id) REFERENCES auth_mfa_user_settings (mfa_user_setting_id) ON DELETE CASCADE
);

-- MFA Authentication attempts
CREATE TABLE IF NOT EXISTS log_auth_mfa_factor_attempts
(
    attempt_id             INTEGER PRIMARY KEY AUTOINCREMENT,
    user_mfa_id            INTEGER      NOT NULL,
    verification_code_hash VARCHAR(255) NOT NULL,
    ip_address             VARCHAR(45)  NOT NULL,
    user_agent             TEXT,
    success                BOOLEAN   DEFAULT FALSE,
    attempted_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_mfa_id) REFERENCES auth_mfa_user_settings (mfa_user_setting_id) ON DELETE CASCADE
);

-- Insert default MFA methods
INSERT INTO auth_mfa_methods (method_name, description)
VALUES ('TOTP', 'Time-based One-Time Password using authenticator apps'),
       ('SMS', 'SMS-based verification code'),
       ('EMAIL', 'Email-based verification code'),
       ('BACKUP_CODES', 'One-time use backup codes');

-- Indices for better performance
CREATE INDEX idx_auth_mfa_user_settings_user_id ON auth_mfa_user_settings (user_id);
CREATE INDEX idx_auth_mfa_user_settings_method_id ON auth_mfa_user_settings (method_id);
CREATE INDEX idx_log_auth_mfa_attempts_user_mfa_id ON log_auth_mfa_factor_attempts (user_mfa_id);
CREATE INDEX idx_auth_mfa_user_backup_codes_user_mfa_id ON auth_mfa_user_backup_codes (user_mfa_id);
CREATE INDEX idx_auth_mfa_user_backup_codes_hash ON auth_mfa_user_backup_codes (code_hash);

