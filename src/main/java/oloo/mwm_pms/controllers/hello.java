package oloo.mwm_pms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class hello {
    @GetMapping
    @ResponseBody
    public String sayHello() {
        return "hello_world";
    }
}
