package com.scs.adapter.idempotent;

public class IdempotencyRecord {

    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    private final Status status;
    private final Object response;
    private final long expireAtMillis;

    public IdempotencyRecord(Status status, Object response, long expireAtMillis) {
        this.status = status;
        this.response = response;
        this.expireAtMillis = expireAtMillis;
    }

    public Status getStatus() {
        return status;
    }

    public Object getResponse() {
        return response;
    }

    public boolean expired(long nowMillis) {
        return nowMillis > expireAtMillis;
    }
}
