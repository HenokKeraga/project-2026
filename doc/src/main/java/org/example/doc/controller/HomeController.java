package org.example.doc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin("*")
public class HomeController {

    // This MVC endpoint serves the main HTML page
    @GetMapping("/")
    public String index() {
        // Assuming you have an 'index.html' file in src/main/resources/static/ or templates/
        return "index.html";
    }
}
