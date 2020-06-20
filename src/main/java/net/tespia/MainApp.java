package net.tespia;


import net.tespia.common.AutoJCE;
import net.tespia.data.FieldInfoFactory;

import java.io.File;


public class MainApp {
    public static void main(String args[]) {
        AutoJCE.removeCryptographyRestrictions();
        FieldInfoFactory.getInstance().load(new File("wz/Map.wz/Map"));
        Client.main(args);
    }
}
