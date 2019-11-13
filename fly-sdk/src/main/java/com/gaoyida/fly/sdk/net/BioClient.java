package com.gaoyida.fly.sdk.net;

import com.gaoyida.fly.sdk.log.SDKLoggerFactory;
import com.gaoyida.fly.sdk.protocol.RequestBase;
import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.gaoyida.fly.common.Constant.UTF8;
import static com.gaoyida.fly.sdk.net.AbstractInvokeService.MSGID;

/**
 * @author gaoyida
 * @date 2019/10/30 下午9:27
 */
public class BioClient implements Client {
    public static final Logger logger = SDKLoggerFactory.getLogger(BioClient.class);

    private Socket socket;

    private DataInputStream in;

    private DataOutputStream out;

    public BioClient(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.socket = socket;
        this.in = dataInputStream;
        this.out = dataOutputStream;
    }

    public static BioClient connect(String ip, int port) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), 3000);
        socket.setKeepAlive(true);
        socket.setSoTimeout(3000);
        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        return new BioClient(socket, in, out);
    }

    public boolean check() {

        if (socket.isClosed() || socket.isInputShutdown() || socket.isOutputShutdown()) {
            return false;
        }
        return true;
    }

    @Override
    public <T> T send(RequestBase request, Class<T> responseType) throws Exception {
        try {
            String reqStr = new Gson().toJson(request);
            byte[] bytes = reqStr.getBytes(UTF8);

            out.writeShort(request.getMessageType().getType());
            out.writeInt(bytes.length + 8);
            out.writeLong(MSGID.getAndIncrement());
            out.write(bytes);
            out.flush();

            in.readShort();
            int length = in.readInt() - 8;

            in.readLong();
            byte[] resp = new byte[length];
            in.readFully(resp);

            T response = new Gson().fromJson(new String(resp, "utf-8"), responseType);
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "send request occur error:" + e.getMessage());
            close();
            throw e;
        }
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "close socket-in occur error ", e);
        }

        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "close socket-out occur error ", e);
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "close socket occur error ", e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }
}
