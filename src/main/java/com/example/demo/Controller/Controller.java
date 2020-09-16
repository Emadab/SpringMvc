package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@RequestMapping("/a")
	@ResponseBody
	public String index(){
		return "Index Page!!!";
	}
}