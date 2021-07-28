package com.changgou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Controller
@RequestMapping("/oauth")
public class LoginRedirect {

    @GetMapping("/login")
    public String login(@RequestParam(value = "FROM",required = false,defaultValue = "") String from, Model model)
    {
        model.addAttribute("from",from);
        return "login";
    }
}
