package com.example.sec;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

	@RequestMapping (value="/login")
	public String login () {
		
		return "login";
	}
	
	@RequestMapping (value="/")
	public String home () {
		
		return "redirect:/operations";
	}
	
	@RequestMapping (value="/403")
	public String accessDenied () {
		
		return "403";
	}
}
