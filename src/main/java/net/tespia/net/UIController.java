/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.net;

import net.tespia.ui.LoginModel;
import net.tespia.wvs.Context;
import net.tespia.wvs.User;


public interface UIController {
    void setConnected(String accountID);

    void onConnectionOpened();

    void onConnectionClosed();

    void onConnectFailure();

    LoginModel getLoginModel();

    void log(String msg, Object... args);

    void updateContext(Context context);

    void migrateTo(byte[] ip, short port, Context context);

    void setInGame();

    User getSelectedUser();

}
