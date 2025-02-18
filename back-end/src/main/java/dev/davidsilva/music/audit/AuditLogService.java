package dev.davidsilva.music.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    // Spring provides an object mapper that has some modules we need (for example the one
    // to support Java 8 types such as LocalDateTime) already configured
    private final ObjectMapper objectMapper;

    @Transactional
    public void log(String action, String entityType, String entityId1,
                    String entityId2, String entityId3,
                    Integer userId, Object oldValue, Object newValue,
                    String description) {
        ObjectWriter ow = this.objectMapper.writer().withDefaultPrettyPrinter();
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
