package bootsamples.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import bootsamples.service.CinemaService;

@Controller
public class MainController { // интересно -))
	
	@Autowired
	private CinemaService cinemaService;
	
	@GetMapping("/")
	public String home()
	{
		return "index";
	}

}
