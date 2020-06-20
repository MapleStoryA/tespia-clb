/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.ui;

import net.tespia.GameScripting;
import net.tespia.net.UIController;
import net.tespia.wvs.Field;

public class FieldManager {

    private Field field;
    private UIController controller;
    private GameScripting scripting;

    public FieldManager(UIController controller, GameScripting scripting) {
        this.controller = controller;
        this.scripting = scripting;
    }

    public void setField(int id) {
        this.field = new Field(this, id);
    }

    public Field getField() {
        return this.field;
    }

    void resetField() {
        this.field = null;
    }

    public synchronized void onEvent(String event, Object context) {
        this.scripting.callEvent(event, context);
    }

    public UIController getController() {
        return controller;
    }
}
