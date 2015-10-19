package com.prj.entity;

import com.prj.util.CopyRequired;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "machine")
public class Machine extends BaseEntity {
  private String mac;
  private boolean active = true;

  private Lab lab;
  private String launchPath;
  private List<ListenDirectory> listenDirectories;


  @CopyRequired
  @Column(nullable = false, unique = true)
  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @CopyRequired
  @ManyToOne
  @JoinColumn(name = "lab_id")
  public Lab getLab() {
    return lab;
  }

  public void setLab(Lab lab) {
    this.lab = lab;
  }

  @CopyRequired
  public String getLaunchPath() {
    return launchPath;
  }

  public void setLaunchPath(String launchPath) {
    this.launchPath = launchPath;
  }

  @CopyRequired
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "machine_id")
  public List<ListenDirectory> getListenDirectories() {
    return listenDirectories;
  }

  public void setListenDirectories(List<ListenDirectory> listenDirectories) {
    this.listenDirectories = listenDirectories;
  }
}
