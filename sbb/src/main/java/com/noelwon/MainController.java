package com.noelwon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/sbb")
	@ResponseBody 
//	 아래와 같이 실행하면 500 에러가 뜬다. 응답을 return 해야 하는데 리턴값이 없기 때문이다.
//	public void index() {
//		System.out.println("index");
//	}
	public String index() {
		return "안녕하세요 sbb에 오신 것을 환영합니다.";
	}
	
	// root url 사용하는 법
	@GetMapping("/")
	public String root() {
		return "redirect:/question/list";
	}

}
