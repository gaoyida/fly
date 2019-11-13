package com.gaoyida.fly.gensrv.listen;

/**
 * @author gaoyida
 * @date 2019/10/24 下午11:14
 * 内部事件listener
 */
public interface ZKEventListener {

    void listen(ZKEvent event, Object attachement) throws Exception;

}
