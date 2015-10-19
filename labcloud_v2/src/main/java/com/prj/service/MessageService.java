package com.prj.service;

import com.prj.entity.Message;
import com.prj.entity.Message.MessageStatus;
import com.prj.entity.Message.MessageType;
import com.prj.util.DataWrapper;

public interface MessageService extends BaseService<Message, Long> {
  
  /**
   * Check if the message belongs to the user.
   */
  boolean isValid(long userId, long msgId);

  /**
   * Get all messages of the receiver.
   */
  DataWrapper getByReceiver(long receiverId);

  /**
   * Get messages of the receiver filtered by type and status. Both type and
   * status can be null.
   */
  DataWrapper getByReceiver(long receiverId, MessageType type,
      MessageStatus status);

  DataWrapper add(long receiverId, String title, String content,
      MessageType type, String external);

  DataWrapper updateStatus(long messageId, MessageStatus status);

}