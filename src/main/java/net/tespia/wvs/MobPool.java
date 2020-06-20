/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import net.tespia.common.Position;
import net.tespia.net.InPacket;
import net.tespia.net.OutPacket;
import net.tespia.packets.SendOps;
import net.tespia.ui.FieldManager;

/**
 * @author sin
 */
public class MobPool extends EntityPool<Mob> {

    private final Field field;
    private final FieldManager manager;


    MobPool(FieldManager manager, Field field) {
        this.manager = manager;
        this.field = field;
    }

    void OnMobEnterField(User user, InPacket in) {
        Mob mob = new Mob();
        mob.decodeInitData(in);
        manager.getController().log("Spawn of mob [%d] at [%s] with id [%d].",
                mob.getTemplateID(),
                mob.getPos(),
                mob.getId());
        this.insert(mob.getId(), mob);
    }

    private OutPacket moveMobToPosition(Mob mob, Position position) {
        OutPacket out = new OutPacket(SendOps.MOVE_MOB);
        out.encodeInt(mob.getId());
        out.encodeShort(104);
        out.encodeByte(0); // use skill
        out.encodeByte(0); // skill
        out.encodePosition(position);
        out.encodeByte(0);
        out.encodeInt(0);
        out.encodePosition(mob.getPos());
        out.encodeByte(3);
        for (int i = 0; i < 3; i++) {
            int start_x = position.getX();
            int stance = 6;
            int wobble_y = 0;
            int duration = 400;
            int fh_id = 0;
            if (i > 1) {
                stance = 4;
            }
            if (i == 0) {
                duration = 90;
                wobble_y = 0;
            }
            if (i == 1) {
                duration = 20;
                wobble_y = 0;
            }
            if (i == 2) {
                wobble_y = 0;
                fh_id = 0;
            }

            out.encodeByte(0); // Fall action
            out.encodePosition(new Position(start_x, position.getY()));
            out.encodePosition(new Position(0, wobble_y));
            out.encodeShort(fh_id);  // FH

            out.encodeByte(stance); // Stance
            out.encodeShort(duration);
        }
        out.encodeByte(0);
        out.encodePosition(position);
        out.encodePosition(position);
        return out;
    }

    void OnMobLeaveField(User user, InPacket in) {
        int mobID = in.decodeInt();
        manager.getController().log("Removed mob with id [%d].", mobID);
        this.remove(mobID);
    }
}
