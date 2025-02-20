CREATE TABLE log_audit
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    action      VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    entity_id   VARCHAR(255),
    user_id     INTEGER      NOT NULL,
    old_value   TEXT,
    new_value   TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id)
);

CREATE INDEX idx_log_audit_user_id ON log_audit (user_id);
CREATE INDEX idx_log_audit_timestamp ON log_audit (created_at);
CREATE INDEX idx_log_audit_entity_type_id ON log_audit (entity_type, entity_id);
