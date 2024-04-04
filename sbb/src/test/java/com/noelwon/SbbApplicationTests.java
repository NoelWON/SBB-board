package com.noelwon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.noelwon.answer.AnswerRepository;
import com.noelwon.question.QuestionRepository;
import com.noelwon.question.QuestionService;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionService questionService;
	
    @Autowired
    private AnswerRepository answerRepository;

//    @Transactional  // 메서드가 종료될 때까지 DB 세션이 유지 답변을 통해 질문을 찾을 때 사용됨
	@Test
	void testJpa() {
		for (int i = 1 ; i <= 100; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content,null);
		}
		
		
//      Question q = new Question();
//      q.setSubject("질문추가제목");
//      q.setContent("추가가 됬나 확인");
//      q.setCreateDate(LocalDateTime.now());
//      this.questionRepository.save(q);  // 직접 추가해본 것
      
//      Optional<Question> oq = this.questionRepository.findById(4);
//      assertTrue(oq.isPresent());
//      Question q = oq.get();
//      q.setContent("추가된것 확인");
//      this.questionRepository.save(q);	
      
   // 데이터 실제 채우는것
//      Question q1 = new Question();
//      q1.setSubject("sbb가 무엇인가요?");
//      q1.setContent("sbb에 대해서 알고 싶습니다.");
//      q1.setCreateDate(LocalDateTime.now());
//      this.questionRepository.save(q1);  // 첫번째 질문 저장
//      
//      
//      Question q2 = new Question();
//      q2.setSubject("스프링 모델 질문");
//      q2.setContent("ID는 자동생성됨?");
//      q2.setCreateDate(LocalDateTime.now());
//      this.questionRepository.save(q2);  // 질문 저장
		
//		// findall() : question리퍼지토리 전체 찾는것
//		List<Question> all = this.questionRepository.findAll(); 
//		// assertEquals(기댓값, 실젯값)와 같이 작성하고 기댓값과 실젯값이 동일한지를 조사
//		// 즉 all.size가 2개 이므로 동일하게 나온다.
//		assertEquals(2,all.size());       
//		// 2개중 get(index)로 해서 index번호에 맞게 제목이 동일한지 확인할 수 있다.
//		Question q = all.get(1);
//		assertEquals("스프링 모델 질문", q.getSubject());
		
		// findById() : id값을 활용해 데이터를 조회하는 것
		// id가 1인 것을 찾아서 oq에 저장
//		Optional<Question> oq = this.questionRepository.findById(1);
//		if (oq.isPresent()) {
//			// q는 id가 1인것을 객체로 가지고 있음 
//			Question q = oq.get();
//			// 그중 제목이 저게 맞나 확인하는 것
//			assertEquals("sbb가 무엇인가요?",q.getSubject());
//			
//		}
		
		
		// findBySubject() : Jparepository에서는 findBySubject 메소드는 제공하지 않는다. 따라서 respository에서 수정을 해줘야 한다.
		// subject 값으로 값을 조회 할 수 있다.
//		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
//		// 객체 q 에 대한 제목으로 찾고 그 Id가 1인지 2인지 확인하는 것이다.
//		assertEquals(1,q.getId());
		
		// findBySubjectAndContent() 메소드 : 제목과 내용 함께 조회 하는 것 , 마찬가지로 리포지토리에 추가를 해줘야 한다.
//		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//		// 동일하게 위에서 찾은 쿼리문이 아래에서 Id가 1번인게 맞는지 확인하는 것이다.
//		assertEquals(1,q.getId());
	
		// findBySubjectLike
		// sbb% : sbb로 시작하는 문자열 %sbb : 끝나는 문자열 %sbb% : 포한하는 문자열
//		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
//		Question q = qList.get(0);
//		assertEquals(1, q.getId());
//		assertEquals("sbb가 무엇인가요?", q.getSubject());
		
		// 데이터 질문 수정하는 법
//        Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        q.setSubject("다시 수정하고 싶어");
//        this.questionRepository.save(q);	
		
		// 질문 데이터 삭제하기
//        assertEquals(2, this.questionRepository.count());
//        Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        this.questionRepository.delete(q);
//        assertEquals(1, this.questionRepository.count());
		
		// 답변 저장하기
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        Answer a = new Answer();
//        a.setContent("네 자동으로 생성됩니다.");
//        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
//        a.setCreateDate(LocalDateTime.now());
//        this.answerRepository.save(a);
		
		// 답변데이터 조회하기
//        Optional<Answer> oa = this.answerRepository.findById(1);
//        assertTrue(oa.isPresent());
//        Answer a = oa.get();
//        assertEquals(2, a.getQuestion().getId());
		
		// 답변을 통해 질문을 찾기
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        List<Answer> answerList = q.getAnswerList();
//
//        assertEquals(1, answerList.size());
//        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    	
	}
}