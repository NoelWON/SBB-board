package com.noelwon.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.noelwon.answer.Answer;
import com.noelwon.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {
    @Id    // key값을 가지게 하려고
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    
    // cascade : 질문 즉 부모가 사라지면 답변도 전부 사라지게 하는 것이다.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    // 답변은 List 형태로 달리게 된다.
    private List<Answer> answerList; 
    
    // 사용자 한명이 질문을 여러개 등록가능
    @ManyToOne
    private SiteUser author;
    
    // 수정, 삭제 될때 언제 수정되었는지 확인할 수 있는 부분
    private LocalDateTime modifyDate;
    
    // 추천기능을 위한 사용자 DB 추가 테이블이 따로 추가가됨
    // @ManyToMany 애너테이션을 사용해 다대다 관계로 속성을 생성하면 새로운 테이블을 만들어 관련 데이터를 관리
    @ManyToMany
    Set<SiteUser> voter;
}
