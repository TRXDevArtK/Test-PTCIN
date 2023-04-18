package com.refaldi.testptcin.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.refaldi.testptcin.services.PenggajianService;

@RestController
public class UserController {

  @RequestMapping(value = "/hitunggaji", method = { RequestMethod.POST, RequestMethod.GET }, headers = "Accept=application/json")
  public String helloWorld(@RequestBody String payload){
    //wrap always with try catch for easy debugging
    try {
      PenggajianService penggajianService = new PenggajianService(payload);
      return penggajianService.getTaxResult();
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}