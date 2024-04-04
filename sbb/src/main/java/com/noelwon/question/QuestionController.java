package com.noelwon.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.noelwon.answer.AnswerForm;
import com.noelwon.user.SiteUser;
import com.noelwon.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



// 프리픽스(prefix): URL 접두사를 지정해놓을 수 있다.
@RequestMapping("/question")
// @RequiredArgsConstructor : 롬복이 제공하는 애너테이션 final이 붙은 속성을 포함하는 생성자를 자동으로 만들어준다. 
// 즉 롬복으로 만들어진 생성자에 의해 리퍼지토리 객체가 자동으로 주입된다.
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	// 서비스를 사용하지 않고 바로 repository를 사용하는 것
//	private final QuestionRepository questionRepository ;
//	
//	@GetMapping("/question/list")
//	
//	// 여기서 model은 리퍼지토리에서 조회한 데이터를 model을 사용해서 넘겨주는 것이다.
//	public String list(Model model) {
//		List<Question> questionList = this.questionRepository.findAll();
//		// questionList에 담긴 리퍼지토리 데이터를 model을 통해 템플릿에 사용될 questionList에 담아준다.
//		model.addAttribute("questionList",questionList);
//		// return이 템플릿 이름으로 된다.
//		return "question_list";
//	}
	
	// 서비스를 사용한 것 차이점! :  생성자가 리퍼지토리인지 서비스인지 달라짐 
	private final QuestionService questionService ;
	private final UserService userService;
	
	// 페이지 설정 없을 시
//	@GetMapping("/list")
//	public String list(Model model) {
//		List<Question> questionList = this.questionService.getList();
//		model.addAttribute("questionList",questionList);
//		return "question_list";
//	}
	// 페이지 설정했을 시
	// http://localhost:8080/question/list?page=0 이런식으로 가져오기 위해 
	// 매개변수에 @RequestParam(value="page", defaultValue="0") int page ; 추가함
//	@GetMapping("/list")
//	public String list(Model model, @RequestParam(value="page",defaultValue="0")int page) {
//		Page<Question> paging = this.questionService.getList(page);
//		model.addAttribute("paging",paging);
//		return "question_list";
//	}
	
	// 검색기능 추가시
	@GetMapping("/list")
	public String list(Model model, @RequestParam(value="page",defaultValue="0")int page,
			@RequestParam(value="kw",defaultValue = "") String kw) {
		Page<Question> paging = this.questionService.getList(page,kw);
		model.addAttribute("paging",paging);
		model.addAttribute("kw",kw);
		return "question_list";
	}
	
	// @PathVariable("id") : id처럼 변동되는 값을 얻을 때 사용한다. 여기서 mapping과 같은 값을 사용해야 한다.
	// 매개변수에 AnswerForm 사용이유는 타당성 검사를 하기 위해서
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id,AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question",question);
		return "question_detail";
	}
	
	
	
	// get과 post 맵핑이 각각 달라서 메소드 이름이 중복되도 알아서 찾아서 들어간다.
	// vaild 를 사용하기 위해 object 속성을 추가해서 매개변수에 QuestionForm 객체가 필요해서 추가로 넣어줘야 한다.
	// @PreAuthorize("isAuthenticated()") : 로그인 경우에만 실행되게 한다. 로그아웃 상태면 로그인 페이지로 강제 이동 이걸 동작하게 하려면 스프링시큐리티도 수정해야함
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	
	// questionform 사용 전 빈 것도 가능하게 한다.
//	@PostMapping("/create")
//	public String questionCreate(@RequestParam(value="subject")String subject,
//			@RequestParam(value="content")String content) {
//		this.questionService.create(subject, content);
//		return "redirect:/question/list";
//	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	// 매개변수를 타당성 검사하는 객체로 가서 가져오게 한다.
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,
			Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		// 글쓴이 가져오는 것
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(),questionForm.getContent(),siteUser);
		return "redirect:/question/list";
	}
	
	
	// 질문 수정 부분
	// questionForm 객체에 id값으로 조회한 질문의 제목(subject)과 내용(object)의 값을 담아서 템플릿으로 전달
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }
	
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    // 질문 삭제 
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
    
    // 추천인 저장
    // [추천] 버튼을 눌렀을 때 GET 방식으로 호출되는 @{|/question/vote/${question.id}|} URL을 처리하기 위해
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
