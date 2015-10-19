package com.prj.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.entity.Account.Role;
import com.prj.entity.Message.MessageStatus;
import com.prj.entity.Message.MessageType;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCode;
import com.prj.util.RequiredRole;

@RestController
public class MessageController extends BaseController {

  /**
   * Get messages of current user.
   * @param type: Message type (regular, system). [Optional]
   * @param status: Message status (read, unread). [Optional]
   */
  @RequestMapping(value = "/message", method = RequestMethod.GET)
  DataWrapper list(@RequestParam(required = false) String type,
      @RequestParam(required = false) String status) {
    MessageType mt = (type == null) ? null : MessageType.valueOf(type
        .toUpperCase());
    MessageStatus ms = (status == null) ? null : MessageStatus.valueOf(status
        .toUpperCase());
    return mes.getByReceiver(currentAccount().getId(), mt, ms);
  }

  /**
   * Add a piece of new message to a specified user.
   * @param receiver: Receiver account ID.
   * @param title: Message title.
   * @param content: Message content.
   * @param type: Message type (regular, system).
   * @param external: Message external information. [Optional]
   */
  @RequestMapping(value = "/message", method = RequestMethod.POST)
  @RequiredRole(Role.ADMINISTRATOR)
  DataWrapper add(@RequestParam("receiver") long receiverId,
      @RequestParam String title, @RequestParam String content,
      @RequestParam String type, @RequestParam(required = false) String external) {
    MessageType mt = MessageType.valueOf(type.toUpperCase());
    return mes.add(receiverId, title, content, mt, external);
  }

  @RequestMapping(value = "/message/{id}/{status}", method = RequestMethod.PUT)
  DataWrapper updateStatus(@PathVariable long id, @PathVariable String status) {
    if(!mes.isValid(currentAccount().getId(), id)) {
      return new DataWrapper(ErrorCode.UNAUTHORIZED);
    }
    MessageStatus ms = MessageStatus.valueOf(status.toUpperCase());
    return mes.updateStatus(id, ms);
  }

  @RequestMapping(value = "/message/{id}", method = RequestMethod.DELETE)
  DataWrapper delete(@PathVariable long id) {
    if(!mes.isValid(currentAccount().getId(), id)) {
      return new DataWrapper(ErrorCode.UNAUTHORIZED);
    }
    return mes.delete(id);
  }
}
