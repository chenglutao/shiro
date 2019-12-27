package com.study.service.impl;


import com.study.common.util.IpUtils;
import com.study.repository.dao.generate.LoginLogMapper;
import com.study.repository.entity.generate.LoginLog;
import com.study.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chenglutao
 * @date 2019-12-27 17:27
 */
@Slf4j
@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void addLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        loginLog.setLocation(IpUtils.getCityInfo(loginLog.getIp()));
        loginLogMapper.insert(loginLog);
    }
}
