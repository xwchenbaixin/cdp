package com.cbx.gp.platform.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cbx.gp.platform.dao.TestDao;
import com.cbx.gp.platform.pojo.TestPojo;
import com.cbx.gp.platform.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDao testDao;
    @Override
    public TestPojo getName() {
        System.out.println("hahahah");
        return testDao.getName();
    }
}
