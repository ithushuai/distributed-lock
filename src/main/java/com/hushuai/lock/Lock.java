package com.hushuai.lock;

/**
 * created by it_hushuai
 * 2020/5/5 19:34
 */
public class Lock {
    private String lockId;
    private String path;
    private boolean active;

    public Lock(String lockId, String path) {
        this.lockId = lockId;
        this.path = path;
    }

    public Lock() {
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
