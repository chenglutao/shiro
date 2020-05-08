package com.study.api.controller;

import com.study.api.config.MyShiroRealm;
import com.study.common.util.SpringContextUtils;
import org.springframework.context.ApplicationContext;

/**
 * @author chenglutao
 */
public class BaseController {

    public void clearCache(){
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        MyShiroRealm myShiroRealm = applicationContext.getBean(MyShiroRealm.class);
        myShiroRealm.clearCache();
    }
}
