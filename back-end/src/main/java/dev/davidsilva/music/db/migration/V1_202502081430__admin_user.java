package dev.davidsilva.music.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import java.util.Map;

public class V1_202502081430__admin_user extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        // Wrap Flyway's connection so JdbcTemplate uses the same transaction/connection
        var dataSource = new SingleConnectionDataSource(context.getConnection(), true);
        var jdbc = new JdbcTemplate(dataSource);

        // NOTE: the password encoder we select here has to be one of the encoders
        // we configure in the app in PasswordEncoderConfiguration.java
        var encoder = new DelegatingPasswordEncoder("bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));

        String username = System.getenv().getOrDefault("ADMIN_USERNAME", "admin");
        String email = System.getenv().getOrDefault("ADMIN_EMAIL", "admin@admin.test");
        String rawPassword = System.getenv().getOrDefault("ADMIN_PASSWORD", "admin");
        String encoded = encoder.encode(rawPassword);

        // Insert user if not exists
        jdbc.update(
                "INSERT INTO auth_users(username, email, password, is_enabled) " +
                        "SELECT ?, ?, ?, TRUE WHERE NOT EXISTS (SELECT 1 FROM auth_users WHERE username = ?)",
                ps -> {
                    ps.setString(1, username);
                    ps.setString(2, email);
                    ps.setString(3, encoded);
                    ps.setString(4, username);
                }
        );

        // Link user to ADMIN role if not already linked
        jdbc.update(
                "INSERT INTO auth_user_roles(user_id, role_id) " +
                        "SELECT u.user_id, r.role_id " +
                        "FROM auth_users u, auth_roles r " +
                        "WHERE u.username = ? AND r.role_name = 'ADMIN' " +
                        "AND NOT EXISTS (SELECT 1 FROM auth_user_roles ur WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id)",
                ps -> ps.setString(1, username)
        );
    }
}