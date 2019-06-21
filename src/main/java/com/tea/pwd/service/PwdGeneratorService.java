package com.tea.pwd.service;

public interface PwdGeneratorService {
    public String generator(String pwdContent,String password);

    public String generatorContent(int num);

    public String getPwd(String pwdContent,String pwd);
}
