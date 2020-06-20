/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import net.tespia.ui.FieldManager;

/**
 *
 * @author sin
 */
public class UserPool extends EntityPool<UserRemote>{
    
    private final Field field;
    private final FieldManager manager;

    

    UserPool(FieldManager manager, Field field) {
       this.manager = manager;
       this.field = field;
    }

}
