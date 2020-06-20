/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.data.inventory;

public final class DBChar {

    public static final int CHARACTER = 0x1;
    public static final int MONEY = 0x2;
    public static final int ITEMSLOTEQUIP = 0x4;
    public static final int ITEMSLOTCONSUME = 0x8;
    public static final int ITEMSLOTINSTALL = 0x10;
    public static final int ITEMSLOTETC = 0x20;
    public static final int ITEMSLOTCASH = 0x40;
    public static final int ITEMSLOT = 0x7;
    public static final int INVENTORYSIZE = 0x80;
    public static final int SKILLRECORD = 0x100;
    public static final int QUESTRECORD = 0x200;
    public static final int MINIGAMERECORD = 0x400;
    public static final int COUPLERECORD = 0x800;
    public static final int MAPTRANSFER = 0x1000;
    public static final int AVATAR = 0x2000;
    public static final int QUESTCOMPLETE = 0x4000;
    public static final int SKILLCOOLTIME = 0x8000;
    public static final int ALL = 0xFFFF;
}
