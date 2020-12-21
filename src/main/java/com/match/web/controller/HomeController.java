package com.match.web.controller;

import com.match.base.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    /**
     * 测试自定义thymeleaf
     * @return
     */
    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        return "index";
    }

    @GetMapping("/404")
    public String notFoundPage(){
        return "404";
    }
    @GetMapping("/403")
    public String accessError(){
        return "403";
    }
    @GetMapping("/500")
    public String internalError(){
        return "500";
    }
    @GetMapping("/logout/page")
    public String logoutPage(){
        return "logout";
    }

}
