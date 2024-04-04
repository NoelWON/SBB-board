package com.noelwon.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.noelwon.DataNotFoundException;
import com.noelwon.question.Question;
import com.noelwon.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	// 질문에 대한 답변 저장 쿼리문 
	public void create(Question question, String content,SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		
	}
	
	// 답변 수정 
	// 1. 질문에 대한 답변을 조회해야 한다.
	public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }
	// 2. 조회된 답변을 수정한다.
	public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
	
	// 답변 삭제
	public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
	
	// 추천 부분
	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		this.answerRepository.save(answer);
	}
}
