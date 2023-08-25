package ru.bulletin_board.bulletin_board.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

//    @GetMapping("/info")
//    public String postInfo() {
//        return "post-info";
//    }
//
//    @GetMapping("/add")
//    public String add() {
//        return "add";
//    }
//
    @GetMapping("/messenger")
    public String messenger() {
        return "messenger";
    }
//
//    @GetMapping("/dialog-box")
//    public String dialogBox() {
//        return "dialog-box";
//    }
//
//    @GetMapping("/profile")
//    public String profile() {
//        return "profile";
//    }
}
