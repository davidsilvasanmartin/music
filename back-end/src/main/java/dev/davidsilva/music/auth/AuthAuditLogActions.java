package dev.davidsilva.music.auth;

public enum AuthAuditLogActions {
    // Authentication actions
    AUTH_LOGIN_SUCCESS("auth.login.success"),
    AUTH_LOGIN_FAILURE("auth.login.failure"),
    AUTH_LOGOUT("auth.logout"),
    AUTH_PASSWORD_RESET_REQUESTED("auth.password.reset.requested"),
    AUTH_PASSWORD_RESET_COMPLETED("auth.password.reset.completed"),

    // User management
    AUTH_USER_CREATED("auth.user.created"),
    AUTH_USER_UPDATED("auth.user.updated"),
    AUTH_USER_DELETED("auth.user.deleted"),
    AUTH_USER_ACTIVATED("auth.user.activated"),
    AUTH_USER_DEACTIVATED("auth.user.deactivated"),

    // Role management
    AUTH_ROLE_ASSIGNED("auth.role.assigned"),
    AUTH_ROLE_REMOVED("auth.role.removed"),
    AUTH_ROLE_CREATED("auth.role.created"),
    AUTH_ROLE_UPDATED("auth.role.updated"),
    AUTH_ROLE_DELETED("auth.role.deleted"),

    // Permission management
    AUTH_PERMISSION_GRANTED("auth.permission.granted"),
    AUTH_PERMISSION_REVOKED("auth.permission.revoked"),
    AUTH_PERMISSION_CREATED("auth.permission.created"),
    AUTH_PERMISSION_UPDATED("auth.permission.updated"),
    AUTH_PERMISSION_DELETED("auth.permission.deleted"),

    // Session management
    AUTH_SESSION_CREATED("auth.session.created"),
    AUTH_SESSION_EXPIRED("auth.session.expired"),
    AUTH_SESSION_TERMINATED("auth.session.terminated"),

    // Security events
    AUTH_SECURITY_ACCOUNT_LOCKED("auth.security.account.locked"),
    AUTH_SECURITY_ACCOUNT_UNLOCKED("auth.security.account.unlocked"),
    AUTH_SECURITY_SUSPICIOUS_ACTIVITY("auth.security.suspicious.activity");

    private final String value;

    AuthAuditLogActions(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}