package dev.davidsilva.music.audit;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String entityType, String entityId,
                    Integer userId, String oldValue, String newValue,
                    String description) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setUserId(userId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setDescription(description);

        auditLogRepository.save(log);
    }
}
