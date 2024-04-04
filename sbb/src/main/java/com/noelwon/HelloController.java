package com.noelwon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//컨트롤러 어노테이션 => 컨트롤러로 인식을 한다는 것
@Controller
public class HelloController {
	@GetMapping("/hello")
	// @ResponseBody 어노테이션은 밑에 있는 hello method가 문자열 결과 그대로라는 뜻이므로 html이 필요가 없다.
	@ResponseBody
	public String hello() {
//		return "hello world";
		return "hello spring boot brand";
	}
}
