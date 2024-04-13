package ru.bulletin_board.bulletin_board.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/messenger")
    public String messenger() {
        return "messenger";
    }

    @GetMapping("/dialog-box/{recipientId}")
    public String showChatDialog(@PathVariable Long recipientId, Model model) {
        // Передайте recipientId на страницу, чтобы на фронтенде можно было знать, кому отправлять сообщения
        model.addAttribute("recipientId", recipientId);
        return "dialog-box"; // Возвращает имя страницы
    }
}
