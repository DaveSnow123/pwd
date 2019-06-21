package com.tea.pwd.service.impl;

import com.tea.pwd.service.PwdGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

@Service("pwdGeneratorService")
public class PwdGeneratorServiceImpl implements PwdGeneratorService {
    private static String pwdContent = "";

    static {
        String path = null;
        Reader fis = null;
        BufferedReader reader = null;
        try{
            path = ResourceUtils.getURL("classpath:").getPath();
            fis = new FileReader(path+"/static/res/pwdContent.txt");
            reader = new BufferedReader(fis);
            String lineTime = null;
            while ((lineTime = reader.readLine()) != null){
                pwdContent += lineTime;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 随机取值
     * @return
     */
    @Override
    public String generator(String pwdContent,String password) {
        StringBuilder sb = new StringBuilder();
        Map<String,String> map = this.getPwdContent(pwdContent);

        for(int i = 0;i<password.length() ; i++){
            char at = password.charAt(i);
            sb.append(map.get(at+""));
        }
       return sb.toString();
    }

    /**
     * 生成密码本
     * @param num
     * @return
     */
    public String generatorContent(int num){
        StringBuilder sb = new StringBuilder();
        String[] pwd = pwdContent.split(",");
        pwd[0] = pwd[0].substring(1);
        pwd[pwd.length-1] = pwd[pwd.length-1].substring(0,pwd[pwd.length-1].length()-1);
        ArrayList<String> cone = new ArrayList<>();
        for(String p : pwd){
            while(true){
                String current = this.pwdDivid(num);
                if(!checkWorld(current,cone)){
                    cone.add(p+current);
                    break;
                }
            }

        }


        for(String s : cone){
            sb.append(s);
        }
        sb.append(num);
        Base64.Encoder encoder = Base64.getEncoder();
        String res = null;
        try {
            res = encoder.encodeToString(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }



    /**
     * 检查是否有重复
     * @param world
     * @param arrayList
     * @return
     */
    public boolean checkWorld(String world,ArrayList<String> arrayList){
        boolean b = false;
        for(int i = 0 ; i<arrayList.size(); i++){
            if(world.equals(arrayList.get(i).substring(1))){
                return true;
            }
        }
        return b;
    }

    /**
     * 随机获取多个密码
     * @param num
     * @return
     */
    public String pwdDivid(int num){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i <num ; i++){
            sb.append(pwdOne());
        }
        return sb.toString();
    }

    /**
     * 随机获取一个密码
     * @return
     */
    public String pwdOne(){
        String[] pwd = pwdContent.split(",");
        pwd[0] = pwd[0].substring(1);
        pwd[pwd.length-1] = pwd[pwd.length-1].substring(0,pwd[pwd.length-1].length()-1);
        int count = (int) Math.ceil(Math.random()*pwd.length - 1);
        return pwd[count];
    }

    /**
     * 将密码本转化对应关系
     * @param pwdContent
     * @return
     */
    public Map<String,String> getPwdContent(String pwdContent){
        Map<String,String> map = new TreeMap<>();

        try {
            Base64.Decoder decoder = Base64.getDecoder();
            String realContent = new String(decoder.decode(pwdContent),"UTF-8");
            int num = Integer.valueOf(realContent.substring(realContent.length() - 1));
            String world = "";
            for(int i = 1 ; i<realContent.length();i++){
                world += realContent.charAt(i-1);
                if(i%(num +1 ) == 0 && i != 0){
                    map.put(world.substring(0,1),world.substring(1));
                    world = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public String getPwd(String pwdContent,String pwd){
        String psd = "";
        try{
            Map<String,String> pwdContmMap = this.getPwdContent(pwdContent);
            Map<String,String> pwdMap = new TreeMap<>();
            for(Map.Entry<String,String> entry : pwdContmMap.entrySet()){
                pwdMap.put(entry.getValue(),entry.getKey());
            }
            Base64.Decoder decoder = Base64.getDecoder();
            String realContent = new String(decoder.decode(pwdContent),"UTF-8");
            int num = Integer.valueOf(realContent.substring(realContent.length() - 1));
            String key = "";
            for(int i = 1; i <= pwd.length() ; i++){
                key += pwd.charAt(i-1);
                if(i%num == 0 &&( i != 0 || num == 1 )){
                    String value = pwdMap.get(key);
                    psd += value;
                    key = "";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return psd;

    }

}
