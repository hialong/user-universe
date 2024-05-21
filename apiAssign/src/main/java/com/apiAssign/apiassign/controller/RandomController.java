package com.apiAssign.apiassign.controller;


import com.decade.apiassignclientsdk.model.Someting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalTime;


/**
 * 给外部提供调用的接口API，模拟 随机数、中午吃什么、要不要去做
 * todo 这些接口是不能暴露的接口，需要检测header里面的token之类的，后面有时间搞，用网关染色请求
 *
 * @author hailong
 */
@RestController
@RequestMapping("/random")
public class RandomController {

    @GetMapping("/get")
    public String getRandomNum(String des, HttpServletRequest request){
        return "完成"+des+"的概率是"+Math.random()*100+"%";
    }

    @PostMapping("/food")
    public String eat(String food, HttpServletRequest request){
        if(Math.random()>0.5){
            return "吃夜宵"+food;
        }else {
            return "不要吃"+food;
        }
    }

    @PostMapping("/PostDo")
    public String doSomething(@RequestBody Someting thing, HttpServletRequest request){
        //请求头到时候检测一下token这里假装检测了
        String nonce = request.getHeader("nonce");
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("没有权限");
        }
        if(Math.random()>0.5){
            return thing.getMyname()+"在"+ LocalTime.now() +"的时间要去做"+thing.getDes();
        }else {
            return thing.getMyname()+"在"+LocalTime.now() +"的时间不要去做"+thing.getDes();
        }
    }

}
