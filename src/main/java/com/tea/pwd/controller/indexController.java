package com.tea.pwd.controller;


import com.tea.pwd.service.PwdGeneratorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class indexController {
    @Resource
    private PwdGeneratorService pwdGeneratorService;

    @ResponseBody
    @RequestMapping("/generator")
    public String generator(@RequestParam(required = true) String pwd,
                        @RequestParam(required = true) String pwdContent){
        return pwdGeneratorService.generator(pwdContent,pwd);
    }

    @ResponseBody
    @RequestMapping("/generatorContent")
    public String generatorContent(@RequestParam(required = true) int num){
        return pwdGeneratorService.generatorContent(num);
    }

    @ResponseBody
    @RequestMapping("/getPassword")
    public String getpwd(@RequestParam(required = true) String pwd,
    @RequestParam(required = true) String pwdContent){
        return pwdGeneratorService.getPwd(pwdContent,pwd);
    }

    @RequestMapping("/")
    public String index(){
        return "/html/index.html";
    }
}
