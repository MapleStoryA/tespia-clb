package net.tespia;

import net.tespia.net.ClientSocket;
import net.tespia.ui.FieldManager;
import net.tespia.ui.LoginModel;
import net.tespia.wvs.Context;

import java.util.HashMap;
import java.util.Map;

public class Client {

    private ClientSocket socket;
    private ClientController controller;
    private GameScripting scripting;
    private String host;
    private int port;

    public Client(String filename) {
        this.controller = new ClientController();
        this.scripting = new GameScripting(getContext(), filename);
        this.controller.setScripting(this.scripting);
        this.controller.setClient(this);
        this.controller.setFieldManager(new FieldManager(this.controller, this.scripting));
    }


    public void connect(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        this.socket = new ClientSocket(this.host, this.port, controller);
        this.socket.start();
        Thread.sleep(3000);
    }

    public void login(String userID, String userPwd, String pic, int chIdx, int charIdx) {
        LoginModel model = new LoginModel();
        model.setUserID(userID);
        model.setPassword(userPwd);
        model.setPin(pic);
        model.setWorldIndex(0);
        model.setChannel(chIdx);
        model.setCharIndex(charIdx);
        this.controller.setModel(model);
        this.socket.sendPasswordPacket(model);
    }


    public void migrateTo(String ip, short port, Context context) {
        this.socket.shutdown();
        this.socket = new ClientSocket(ip, port, this.controller);
        this.socket.setConnected(false);
        this.socket.setContext(context);
        this.socket.setInGame();
        this.socket.start();
    }


    private Map<String, Object> getContext() {
        final Map<String, Object> context = new HashMap<>();
        context.put("client", this);
        context.put("socket", this.socket);
        context.put("controller", this.controller);
        return context;
    }

    public static void main(String args[]) {
        new Client("enter_map.js");
    }


}
