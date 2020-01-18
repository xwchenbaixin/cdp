package com.cbx.gp.platform.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.TestPojo;
import com.cbx.gp.platform.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
