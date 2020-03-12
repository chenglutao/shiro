package com.study.service;

import com.study.common.entity.RespEntity;
import com.study.repository.entity.generate.User;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-19 16:41
 */
public interface UserService {

    User getName(String userName);

    List<User> list();

    RespEntity add(User user, String roleIds);

    void update(User user, String roleIds);

    User detail(String userId);

    void delete(String userIds);

    void passwordUpdate(Integer userId, String newPassword);

    void passwordReset(String userName, String password);
}
