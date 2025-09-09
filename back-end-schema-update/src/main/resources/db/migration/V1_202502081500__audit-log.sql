CREATE TABLE log_audit
(
    -- TODO add fields: outcome (SUCCESS/FAILURE), reason/error_code, trace_id/span_id, request_id, tenant_id (if multi-tenant), source_ip/user_agent, resource_owner (if distinct from actor)
    -- The request metadata can be stored in a JSONB field I guess
    id          SERIAL PRIMARY KEY,
    action      VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    entity_id   VARCHAR(255),
    -- We have to allow user_id to be null if we want to log authentication attempts (where there is no user logged in)
    -- TODO see if such attempts can/should be logged in a different table
    user_id     INTEGER,
    -- TODO JSONB for old/new values
    old_value   TEXT,
    new_value   TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id)
        -- TODO: the following line affects existing audit log entries that belong to a user that we delete. These entries
        --  will be updated with NULL for the user id. Instead of doing this, it's better if we don't allow user deletion
        --  (only the capability to disable them). Or, alternatively, save the username as a string and not as a foreign
        --  key
        ON DELETE SET NULL
);

CREATE INDEX idx_log_audit_user_id ON log_audit (user_id);
CREATE INDEX idx_log_audit_timestamp ON log_audit (created_at);
CREATE INDEX idx_log_audit_entity_type_id ON log_audit (entity_type, entity_id);