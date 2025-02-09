CREATE TABLE log_audit
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    action      VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    -- Several id columns are provided for the case where ids are composite. When building queries
    -- to fetch data from this table we need to take into account the order in which these ids were
    -- set when creating a row
    entity_id_1 VARCHAR(255),
    entity_id_2 VARCHAR(255),
    entity_id_3 VARCHAR(255),
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
CREATE INDEX idx_log_audit_entity_type_id ON log_audit (entity_type, entity_id_1, entity_id_2, entity_id_3);
