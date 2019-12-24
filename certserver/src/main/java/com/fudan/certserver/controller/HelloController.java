package com.fudan.certserver.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say(){
        System.out.println("222222222222222222");
        return "hello world";
    }

    @PostMapping(value = "/a")
    public int addUser(@RequestParam("uname") String uname){
        System.out.println("1111111111111111");
        return 111111;
    }

    @RequestMapping(value = "/cpabe", method = RequestMethod.GET)
    public String toIndex(){
        System.out.println("11111111111");
        return "index";
    }

    @RequestMapping(value = "/cert", method = RequestMethod.GET)
    public String tocert(){
        System.out.println("22222222222");
        return "cert";
    }

}
