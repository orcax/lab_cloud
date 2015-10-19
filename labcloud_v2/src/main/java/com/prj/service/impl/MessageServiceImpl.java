package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.dao.AccountDao;
import com.prj.dao.MessageDao;
import com.prj.entity.Message;
import com.prj.entity.Message.MessageStatus;
import com.prj.entity.Message.MessageType;
import com.prj.service.MessageService;
import com.prj.util.DataWrapper;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Message, Long>
    implements MessageService {

  MessageDao messageDao;

  @Autowired
  AccountDao accountDao;

  @Autowired
  public MessageServiceImpl(MessageDao messageDao) {
    super(Message.class, messageDao);
    this.messageDao = messageDao;
  }

  public boolean isValid(long userId, long msgId) {
    Message msg = messageDao.read(msgId);
    if(msg != null && msg.getReceiver().getId() == userId)
      return true;
    return false;
  }

  public DataWrapper getByReceiver(long receiverId) {
    List<Criterion> conditions = new ArrayList<Criterion>();
    conditions.add(Restrictions.eq("receiver", accountDao.read(receiverId)));
    return new DataWrapper(messageDao.getAllByConditions(conditions));
  }

  public DataWrapper getByReceiver(long receiverId, MessageType type,
      MessageStatus status) {
    List<Criterion> conditions = new ArrayList<Criterion>();
    conditions.add(Restrictions.eq("receiver", accountDao.read(receiverId)));
    if (type != null) {
      conditions.add(Restrictions.eq("type", type));
    }
    if (status != null) {
      conditions.add(Restrictions.eq("status", status));
    }
    return new DataWrapper(messageDao.getAllByConditions(conditions));
  }

  public DataWrapper create(Message msg) {
    return super.createEntity(msg);
  }

  public DataWrapper add(long receiverId, String title, String content,
      MessageType type, String external) {
    Message msg = new Message();
    msg.setReceiver(accountDao.read(receiverId));
    msg.setTitle(title);
    msg.setContent(content);
    msg.setType(type);
    msg.setStatus(MessageStatus.UNREAD);
    msg.setExternal(external);
    return new DataWrapper(messageDao.create(msg));
  }

  public DataWrapper updateStatus(long messageId, MessageStatus status) {
    messageDao.update(messageId, "status", status.toString());
    return DataWrapper.voidSuccessRet;
  }

}
