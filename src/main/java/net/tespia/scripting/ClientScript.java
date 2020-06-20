/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.scripting;

import net.tespia.ui.FieldManager;
import net.tespia.wvs.Field;
import net.tespia.wvs.User;


public interface ClientScript {

    User getUser();

    FieldManager getFieldManager();

    Field getField();

    void sendPacket(String packet);


}
