package net.tespia.provider;

import java.io.File;

public class ProviderInitializer {

  public static void main(String[] args) {
    MapleDataProvider etc = MapleDataProviderFactory.getDataProvider(new File("wz/Etc.wz"));
    MapleData data = etc.getData("MakeCharInfo.img");
    System.out.println(data.getChildByPath("Info")
        .getChildByPath("CharMale")
        .getChildByPath("0")
        .getChildren().size());
  }
}
