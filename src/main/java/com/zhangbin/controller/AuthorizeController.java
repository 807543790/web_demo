package com.zhangbin.controller;

import com.zhangbin.dto.AccessTokenDTO;
import com.zhangbin.dto.GithubUser;
import com.zhangbin.model.User;
import com.zhangbin.provider.GithubProvider;
import com.zhangbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//ctrl+alt+n
//shift加回车可以直接换行，不用挪动光标
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //通过value注解实现从application.properties中取设置好的值
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code" )String code,
                           @RequestParam(name = "state" )String state,
                            HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubPUser = githubProvider.getUser(accessToken);
        //GitHubUser不等于NULL的时候，就表示成功了
        if(githubPUser != null && githubPUser.getId() != null){
            //创建user对象
            User user = new User();
            //传入从GitHub获取到的数据
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubPUser.getName());
            user.setAccountId(String.valueOf(githubPUser.getId()));
            user.setAvatarurl(githubPUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //登录成功，写cookie和session
            response.addCookie(new Cookie("token",token));
            //重新跳转到index页面 /符合表示根目录
            System.out.println("欢迎登陆:"+user.getName());
            return "redirect:/";
        }else{
            System.out.println("失败");
            //登录失败，重新登录
            return "redirect:/";
        }
    }
    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //删除session数据
        request.getSession().removeAttribute("user");
        //删除cookie数据
        Cookie cookie = new Cookie("token" ,null );
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}