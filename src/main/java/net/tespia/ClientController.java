package net.tespia;

import net.tespia.net.UIController;
import net.tespia.ui.FieldManager;
import net.tespia.ui.LoginModel;
import net.tespia.wvs.Context;
import net.tespia.wvs.Field;
import net.tespia.wvs.User;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientController implements UIController {

    private GameScripting scripting;
    private LoginModel model = new LoginModel();
    private Client client;
    private Context context;
    private FieldManager fieldManager;

    public void setFieldManager(FieldManager fieldManager) {
        this.fieldManager = fieldManager;
    }

    public void setScripting(GameScripting scripting) {
        this.scripting = scripting;
    }

    public LoginModel getModel() {
        return model;
    }

    public void setModel(LoginModel model) {
        this.model = model;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void setConnected(String accountID) {
        System.out.println("Account " + accountID + " connected.");
    }

    @Override
    public void onConnectionOpened() {
        System.out.println("Connection Opened.");
    }

    @Override
    public void onConnectionClosed() {
        System.out.println("Connection Closed.");
    }

    @Override
    public void onConnectFailure() {
    }

    @Override
    public LoginModel getLoginModel() {
        return model;
    }

    @Override
    public void log(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    @Override
    public void updateContext(Context context) {
        this.context = context;
    }

    @Override
    public void migrateTo(byte[] ip, short port, Context context) {
        log("Trying to migrate to port: %d", port);
        try {
            String strIp = InetAddress.getByAddress(ip).getHostAddress();
            log("Migrating to new HOST %s:%d", strIp, port);
            this.client.migrateTo(strIp, port, context);
        } catch (UnknownHostException ex) {
            log("Could not parse ip to connect.");
        }

    }

    @Override
    public void setInGame() {
        User user = context.getUser(model.getCharIndex());
        fieldManager.setField(user.getStat().getFieldID());
        Field field = fieldManager.getField();
        user.setField(field);
        scripting.addBinding("field", field);
        scripting.addBinding("user", getSelectedUser());
        scripting.callEvent("inGame");
    }

    @Override
    public User getSelectedUser() {
        return context.getUser(model.getCharIndex());
    }


    public FieldManager getFieldManager() {
        return fieldManager;
    }
}
