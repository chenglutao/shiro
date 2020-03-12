package com.study.service;

import com.study.repository.entity.generate.Role;
import com.study.repository.entity.generate.User;

import java.util.List;

/**
 * @author chenglutao
 * @date 2019-12-19 16:41
 */
public interface UserService {

    User getName(String userName);

    List<User> list();

    void add(User user, List<Integer> roleIds);

    void update(User user);
}
