package com.noelwon.question;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
// jparepository의 경우 CRUD 작업을 처리하는 메소드를 이미 내장하고 있는 것이다.
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Question,Integer> 의 경우 Question 엔티티로 리포지터리를 생성한다는 것이다. 그리고 기본키가 Integer (=ID)를 말한다.
public interface QuestionRepository extends JpaRepository<Question,Integer>{
	// findBySubject() 메소드를 사용하기 위해 추가해준다.
	Question findBySubject(String subject);
	
	// findBySubjectAndContent() 사용하기 위한 것. : Subject, Content 열과 일치하는 데이터를 조회
	Question findBySubjectAndContent(String subject, String content);
	
	// 주의 점!!!!!
	// 응답 결과가 여러 건인 경우에는 리포지터리 메서드의 리턴 타입을 Question이 아닌 List<Question>으로 작성해야 함을 꼭 기억해 두자.
	
	// Subject열 또는 Content 열과 일치하는 데이터를 조회
	Question findBySubjectOrContent(String subject, String content);
	
	// CreateDate 열의 데이터 중 정해진 범위 내에 있는 데이터를 조회
	List<Question> findByCreateDateBetween(LocalDateTime fromDate, LocalDateTime  toDate);

	// Id 열에서 조건보다 작은 데이터를 조회
	List<Question> findByIdLessThan(Integer id);
	// Id 열에서 조건보다 크거나 같은 데이터를 조회
	List<Question> findByIdGreaterThanEqual(Integer id);
	
	// Subject 열에서 문자열 ‘subject’와 같은 문자열을 포함한 데이터를 조회
	List<Question> findBySubjectLike(String subject);
	
	// Subject 열의 데이터가 주어진 배열에 포함되는 데이터만 조회
	List<Question> findBySubjectIn(String[] subjects);
	
	// Subject 열 중 조건에 일치하는 데이터를 조회하여 CreateDate 열을 오름차순으로 정렬하여 반환
	List<Question> findBySubjectOrderByCreateDateAsc(String subject);
	
	// 페이징 구현
	Page<Question> findAll(Pageable pageable);
	
	// 검색기능 관련 추가 (서비스의 specification을 쓰기 위함)
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}

