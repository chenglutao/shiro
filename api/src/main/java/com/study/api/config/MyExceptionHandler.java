package com.study.api.config;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenglutao
 * @date 2019-12-19 17:25
 */
public class MyExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        if (ex instanceof UnauthenticatedException) {
            attributes.put("key", "9999");
            attributes.put("msg", "认证失败");
        } else if (ex instanceof UnauthorizedException) {
            attributes.put("key", "9999");
            attributes.put("msg", "无权限");
        } else {
            attributes.put("key", "9999");
            attributes.put("msg", ex.getMessage());
        }
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
    }
}
