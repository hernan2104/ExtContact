package com.itx.clientes;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilesController {

	// inject via application.properties
	
    @GetMapping("/")
    public String main(Model model) {


        return "welcome"; //view
    }
}
