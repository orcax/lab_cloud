package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "listen_directory")
public class ListenDirectory extends BaseEntity {
  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
