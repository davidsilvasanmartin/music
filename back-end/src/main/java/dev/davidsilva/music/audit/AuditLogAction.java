package dev.davidsilva.music.audit;

public enum AuditLogAction {
    CREATE("CREATE"), UPDATE("UPDATE"), DELETE("DELETE"), READ("READ");

    private final String value;

    AuditLogAction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
