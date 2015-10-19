package com.prj.dao.impl;

import org.springframework.stereotype.Repository;

import com.prj.dao.MessageDao;
import com.prj.entity.Message;

@Repository
public class MessageDaoImpl extends BaseDaoHibernateImpl<Message, Long>
    implements MessageDao {

  public MessageDaoImpl() {
    super(Message.class);
  }

}
