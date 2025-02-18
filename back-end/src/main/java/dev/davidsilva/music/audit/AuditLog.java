package dev.davidsilva.music.audit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "log_audit")
@NoArgsConstructor
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String action;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id_1")
    private String entityId1;

    @Column(name = "entity_id_2")
    private String entityId2;

    @Column(name = "entity_id_3")
    private String entityId3;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    // This column has insertable=false to allow the default value (defined on
    // table creation) to be set by the database
    @Column(name = "timestamp", insertable = false)
    private Instant timestamp;

    @Column(name = "description", length = 500)
    private String description;
}
