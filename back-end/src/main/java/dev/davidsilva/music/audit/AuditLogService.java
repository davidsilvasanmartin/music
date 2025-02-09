package dev.davidsilva.music.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String entityType, String entityId1,
                    String entityId2, String entityId3,
                    Integer userId, Object oldValue, Object newValue,
                    String description) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String oldValueJson = "";
        String newValueJson = "";
        try {
            oldValueJson = ow.writeValueAsString(oldValue);
            newValueJson = ow.writeValueAsString(newValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId1(entityId1);
        log.setEntityId2(entityId2);
        log.setEntityId3(entityId3);
        log.setUserId(userId);
        log.setOldValue(oldValueJson);
        log.setNewValue(newValueJson);
        log.setDescription(description);

        auditLogRepository.save(log);
    }
}
