package com.gaoyida.fly.common.util;

import com.gaoyida.fly.common.dt.ServerNode;
import com.gaoyida.fly.common.exception.InitExeception;
import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author gaoyida
 * @date 2019/10/18 上午11:00
 */
public class ServerUtil {

    private static final Logger logger = LoggerFactory.getLogger(ServerUtil.class);
    public static InetAddress localAddress;

    static {
        try {
            Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

            while (enu.hasMoreElements()) {
                NetworkInterface ni = enu.nextElement();
                if (ni.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress address = addressEnumeration.nextElement();

                    if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                        continue;
                    }

                    localAddress = address;
                    break;
                }
            }
        } catch (SocketException e) {
            throw new InitExeception("fail to get local ip.");
        }
    }

    public static ServerNode toServerNode(int workerId) {
        ServerNode serverNode = new ServerNode();
        serverNode.setId(workerId);
        serverNode.setIp(localAddress.toString());
        serverNode.setTimestamp(System.currentTimeMillis());
        return serverNode;
    }
}
