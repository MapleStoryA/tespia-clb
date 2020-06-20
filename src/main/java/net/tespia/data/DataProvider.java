package net.tespia.data;

import java.io.File;

public class DataProvider {

 
  public DataProvider() {
  }

  public void init() {
    FieldInfoFactory.getInstance().load(new File("wz/Map.wz/Map"));
  }



  public static void main(String[] args) {
    new DataProvider().init();
    FieldInfoFactory.getInstance().getInfo(20000);
  }
}
