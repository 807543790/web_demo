package com.zhangbin.interceptor;

import com.zhangbin.mapper.UserMapper;
import com.zhangbin.model.User;
import com.zhangbin.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    //预处理回调方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取前端cookie数据
        Cookie[] cookies = request.getCookies();
        //判断cookies不为空
        if (cookies != null && cookies.length != 0)
            //循环取出token数据
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    //将得到的数据去数据库中查询，如果拥有的话给cookie中Set数据user
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                    }
                    break;
                }
            }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
