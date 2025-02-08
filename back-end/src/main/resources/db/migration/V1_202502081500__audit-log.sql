CREATE TABLE log_audit
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
--     TODO think whether we want to have only 1 column for action and remove entity_type
    action      VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    entity_id   VARCHAR(255),
    user_id     INTEGER, -- TODO NOT NULL
    old_value   TEXT,
    new_value   TEXT,
    timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     TODO think about removing "description", if the actions are granular we don't really need it
    description VARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id)
);

CREATE INDEX idx_log_audit_user_id ON log_audit (user_id);
CREATE INDEX idx_log_audit_timestamp ON log_audit (timestamp);
CREATE INDEX idx_log_audit_entity_type_id ON log_audit (entity_type, entity_id);
