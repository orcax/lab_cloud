package com.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "message")
public class Message extends BaseEntity {

  public enum MessageStatus {
    READ, UNREAD
  }

  public enum MessageType {
    REGULAR, SYSTEM
  }

  private String title;
  private String content;
  private MessageStatus status;
  private MessageType type;
  private String external;
  private Account receiver;

  public Message() {
  }

  public Message(long id) {
    this.id = id;
  }

  @CopyRequired
  @Column(nullable = false)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @CopyRequired
  @Column(nullable = false)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  public MessageStatus getStatus() {
    return status;
  }

  public void setStatus(MessageStatus status) {
    this.status = status;
  }

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  @CopyRequired
  public String getExternal() {
    return external;
  }

  public void setExternal(String external) {
    this.external = external;
  }

  @CopyRequired
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "receiver_id")
  public Account getReceiver() {
    return receiver;
  }

  public void setReceiver(Account receiver) {
    this.receiver = receiver;
  }

}