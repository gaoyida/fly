package com.gaoyida.fly.gensrv.listen;

import java.util.List;

/**
 * @author gaoyida
 * @date 2019/10/18 上午9:36
 */
public interface ChildListener {

    void childChanged(String path, List<String> children) throws Exception;


}
