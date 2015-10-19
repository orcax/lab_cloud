package com.prj.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.entity.Account;
import com.prj.entity.Lab;
import com.prj.entity.Machine;
import com.prj.util.DataWrapper;
import com.prj.util.RequiredRole;

@RestController
public class MachineController extends BaseController {
  @RequestMapping(value = "/machine", method = RequestMethod.POST)
  @RequiredRole(Account.Role.ADMINISTRATOR)
  DataWrapper add(@RequestParam("lab") long labId,
                  @RequestBody Machine machineProf) {
    Lab l = new Lab();
    l.setId(labId);
    machineProf.setLab(l);
    return ms.create(machineProf);
  }

  @RequestMapping(value = "/machine", method = RequestMethod.GET)
  DataWrapper getByMac(@RequestParam String mac) {
    return ms.getByMac(mac);
  }

  @RequestMapping(value = "/machine/{mac}", method = RequestMethod.POST)
  DataWrapper updateByMac(@RequestBody Machine machine, @PathVariable String mac){
    return ms.updateByMac(mac, machine);
  }

}
