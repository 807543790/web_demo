package com.zhangbin.controller;

import com.zhangbin.dto.PaginationDTO;
import com.zhangbin.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController{

        @Autowired
        private QuestionService questionService;

        @GetMapping("/")
        public String index(Model model,
                            //默认第一页
                            @RequestParam(name = "page",defaultValue = "1") Integer page,
                            //每页默认展示的条数数据
                            @RequestParam(name = "size",defaultValue = "5") Integer size){
            PaginationDTO pagination =questionService.list(page,size);
            model.addAttribute("pagination",pagination);
                return "index";

        }

}




