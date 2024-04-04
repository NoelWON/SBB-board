package com.noelwon.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.noelwon.question.Question;
import com.noelwon.question.QuestionService;
import com.noelwon.user.SiteUser;
import com.noelwon.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	// <form th:action="@{|/asnwer/create/${question.id}|}" method="post"> question_html 에서 호출
//	@PostMapping("/create/{id}")
//	// 답변으로 입력한 내용(content)을 얻으려고 추가한 것
//	// <textarea>의 name 속성명이 content이므로 여기서도 변수명을 content로 사용
//	// 여기서 id 값은 question 번호를 말하는 것이다.
//	public String createAnswer(Model model, @PathVariable("id")Integer id,
//			@RequestParam(value="content") String content) {
//		Question question = this.questionService.getQuestion(id);
//		this.answerService.create(question, content);
//		return String.format("redirect:/question/detail/%s", id);
//	}
	
	
	// 마찬가지로 vaild 검사를 하기 위해서 form 객체를 활용한다.
	// principal : principal 객체를 사용하면 이제 로그인한 사용자명을 알 수 있으므로 사용자명으로 SiteUser 객체를 조회 가능 ( userService에서 getUser로 가져온다.)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id")Integer id,
			@Valid AnswerForm answerForm, BindingResult bindingResult,
			Principal principal) {
		Question question = this.questionService.getQuestion(id);
		// 사용자 정보 추가 부분
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		if (bindingResult.hasErrors()) {
			// 추가적으로 detail 부분에서는 question객체가 필요하므로 model객체에 Question객체를 저장해야 한다.
			model.addAttribute("question",question);
			return "question_detail";
		}
		this.answerService.create(question, answerForm.getContent(),siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	// 답변 수정 부분 (Get)
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }
	// 답변 수정 (Post)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
            @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
    
    // 답변 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
    
    // 추천 부분
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
}
