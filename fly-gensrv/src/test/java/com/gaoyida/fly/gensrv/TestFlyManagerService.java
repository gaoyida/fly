package com.gaoyida.fly.gensrv;

import com.gaoyida.fly.gensrv.core.service.ZKManageService;
import com.gaoyida.fly.gensrv.zk.ZKConfig;
import org.junit.Test;

/**
 * @author gaoyida
 * @date 2019/10/18 上午11:27
 */
public class TestFlyManagerService {

    @Test
    public void zk() throws Exception {
        System.out.println("-----------------test start------------------");
        ZKManageService zkManagerService = new ZKManageService();
        ZKConfig zkConfig = new ZKConfig();
        zkConfig.setConnectUrl("");
        zkManagerService.setZc(zkConfig);
        zkManagerService.startService();
        String workerId = zkManagerService.zkClient.createSequential("/workerId/ID", "", false);
        System.out.println(Integer.valueOf(workerId.substring(12, workerId.length())));

    }


}
