package com.zhangbin.controller;

import com.zhangbin.dto.QuestionDTO;
import com.zhangbin.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Integer id,
                            Model model){
        //getId方法是SQL语句，将查询到的东西放到Model里展示
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
