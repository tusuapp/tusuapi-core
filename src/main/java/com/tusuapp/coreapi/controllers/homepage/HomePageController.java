package com.tusuapp.coreapi.controllers.homepage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * HomePageController created by Rithik S(coderithik@gmail.com)
 **/
@Controller
@RequestMapping("/home")
public class HomePageController {

    @GetMapping
    public String respone(){
        return "<h1>helo</h1>";
    }

}
