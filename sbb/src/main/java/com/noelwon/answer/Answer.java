package com.noelwon.answer;

import java.time.LocalDateTime;
import java.util.Set;

import com.noelwon.question.Question;
import com.noelwon.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate; 
    
    // 부모: Question 자식 : Answer => 하나의 질문에 답변이 여려개 달릴 수 있다.
    @ManyToOne
    private Question question;
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
 // 추천기능을 위한 사용자 DB 추가
    @ManyToMany
    Set<SiteUser> voter;
}
