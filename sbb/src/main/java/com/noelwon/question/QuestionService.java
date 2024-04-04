package com.noelwon.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.noelwon.DataNotFoundException;
import com.noelwon.answer.Answer;
import com.noelwon.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// 서비스 어노테이션으로 서비스라는 것을 지정해줌
@Service 
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	// page 없을 때 
//	public List<Question> getList(){
//		return this.questionRepository.findAll();
//	}
	
	
	// page 설정 , 최신순 표시
	// int page => 페이지 번호를 입력받음
//	public Page<Question> getList(int page){
//		// 최신순 정렬 하는 부분
//		List<Sort.Order> sorts = new ArrayList();
//		// desc : 내림차순 asc: 오름차순
//		sorts.add(Sort.Order.desc("createDate"));
//		// page당 10개의 게시물을 보여준다.
//		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
//	
//		return this.questionRepository.findAll(pageable);
//	}
	
	// 검색기능 추가된 것
	public Page<Question> getList(int page,String kw){
		// 최신순 정렬 하는 부분
		List<Sort.Order> sorts = new ArrayList();
		// desc : 내림차순 asc: 오름차순
		sorts.add(Sort.Order.desc("createDate"));
		// page당 10개의 게시물을 보여준다.
		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
		Specification<Question> spec = search(kw);
		return this.questionRepository.findAll(spec,pageable);
	}
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if (question.isPresent()) {
			return question.get();
		}else {
			throw new DataNotFoundException("quesiton not found");
		}
		
	}
	
	// 질문 새로 저장하기
	public void create(String subject, String content, SiteUser user) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(user);
		this.questionRepository.save(q);
	}
	
	// 질문 수정하기 
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	// 질문 삭제
	public void delete(Question question) {
        this.questionRepository.delete(question);
    }
	
	// 추천인을 저장할 수 있도록 ????
	public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
	
	// 검색기능
	 private Specification<Question> search(String kw) {
	        return new Specification<>() {
	            private static final long serialVersionUID = 1L;
	            @Override
	            // q: Root 자료형으로, 즉 기준이 되는 Question 엔티티의 객체를 의미 >> 질문 제목과 내용을 검색
	            // u1: Question 엔티티와 SiteUser 엔티티를 아우터 조인하여 만든 SiteUser 엔티티의 객체 >> 질문작성자 검색
	            // a: Question 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체 >> 답변내용 검색
	            // u2: 바로 앞에 작성한 a 객체와 다시 한번 SiteUser 엔티티와 아우터 조인하여 만든 SiteUser 엔티티의 객체 >> 답변 작성자 검색
	            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
	                query.distinct(true);  // 중복을 제거 
	                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
	                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
	                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
	                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목 
	                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
	                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자 
	                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용 
	                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자 
	            }
	        };
	    }
}
