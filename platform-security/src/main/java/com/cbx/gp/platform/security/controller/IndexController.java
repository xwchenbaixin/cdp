package com.cbx.gp.platform.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping("/student")
    @ResponseBody
    @PreAuthorize(value = "hasRole('ROLE_STUDENT')")
    public String student(){
        System.out.println("wellcome Student");
        return "ROLE_STUDENT";
    }

    @RequestMapping("/teacher")
    @ResponseBody
    @PreAuthorize(value = "hasRole('ROLE_TEACHER')")
    public String teacher(){
        System.out.println("wellcome Teacher");
        return "ROLE_TEACHER";
    }

    @RequestMapping("/login")
    public String login() {
        return "login.html";
    }



}
