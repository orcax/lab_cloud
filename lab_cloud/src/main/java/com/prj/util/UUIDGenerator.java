package com.prj.util;

import java.util.UUID;


public class UUIDGenerator {

  private UUIDGenerator(){}

  public static String getCode(String prefix) {
    String newCode = prefix
        + UUID.randomUUID().toString();
    return newCode;
  }

}
