package com.zhangbin.controller;

import com.zhangbin.dto.QuestionDTO;
import com.zhangbin.model.Question;
import com.zhangbin.model.User;
import com.zhangbin.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    //GET请求
    @GetMapping("/publish")
    public String publish(){

        return  "publish";
    }

    //编辑当前问题的请求
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        //传入ID参数，查询出当前ID下的问题内容
        QuestionDTO question = questionService.getById(id);
        //传入model可以直接在页面上获取到
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",id);
        return  "publish";
    }

    //post请求
    @PostMapping("/publish")
    public String doPublish(
           @RequestParam(value = "title",required = false) String title,
           @RequestParam(value = "description",required = false) String description,
           @RequestParam(value = "tag",required = false) String tag,
           @RequestParam(value = "id",required = false) Integer id,
            HttpServletRequest request,
            Model model){
        //传入model可以直接在页面上获取到
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        //后端校验前端数据是否为NULL
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        //判断如果用户没有登录就发布问题所作出的提示
        if(user == null){
            model.addAttribute("error","用户没有登录");
            return "publish";
        }
        //传入前端数据
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
