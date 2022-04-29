package com.imgpicker.main.defaults;

public class ResourceGiver {
  private ResourceGiver() {}
  public static String retrieveNewIMGIco() {
    try {
      return ResourceGiver.class.getClass().getResource("new_img_file_btn_ico.png").getFile();
    } catch (NullPointerException e) {
      return "resource/new_img_file_btn_ico.png";
    }
  }

  public static String retrieveCollectorIco() {
    try {
      return ResourceGiver.class.getClass().getResource("new_img_file_btn_ico.png").getFile();
    } catch (NullPointerException e) {
      return "resource/collector_rsc_ico.png";
    }
  }

  public static String retrieveLocal(String str) {
    try {
      return ResourceGiver.class.getClass().getResource(str).getFile();
    } catch (NullPointerException e) {
      return str;
    }
  }
}
