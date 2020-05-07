package com.cbx.gp.platform.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.bean.TestPojo;
import com.cbx.gp.platform.service.interfaces.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

    @Reference
    private TestService testService;

    @RequestMapping("/getUserName")
    @ResponseBody
    public TestPojo getUserName(){
        return testService.getName();
    }

}
