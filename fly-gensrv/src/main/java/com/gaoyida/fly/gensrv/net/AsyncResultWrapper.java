package com.gaoyida.fly.gensrv.net;

/**
 * @author gaoyida
 * @date 2019/10/24 下午4:08
 */
public class AsyncResultWrapper {
    private boolean ok;
    private Throwable throwable;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
