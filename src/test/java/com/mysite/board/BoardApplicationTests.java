package com.mysite.board;

import com.mysite.board.answer.Answer;
import com.mysite.board.answer.AnswerRepository;
import com.mysite.board.question.Question;
import com.mysite.board.question.QuestionRepository;
import com.mysite.board.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BoardApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void 질문저장() {
		Question q1 = new Question();
		q1.setSubject("board가 무엇인가요?");
		q1.setContent("board에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링 부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);
	}

	@Test
	void 질문조회1() {
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("board가 무엇인가요?", q.getSubject());
	}

	@Test
	void 질문조회2() {
		Optional<Question> oq = this.questionRepository.findById(1);
		if (oq.isPresent()) {
			Question q = oq.get();
			assertEquals("board가 무엇인가요?", q.getSubject());
		}
	}

	@Test
	void 질문조회3() {
		Question q = questionRepository.findBySubject("board가 무엇인가요?");
		assertEquals(1, q.getId());
	}

	@Test
	void 질문조회4() {
		Question q = questionRepository.findBySubjectAndContent("board가 무엇인가요?", "board에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}

	@Test
	void 질문수정() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		questionRepository.save(q);
	}


	@Test
	void 질문삭제() {
		assertEquals(2, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		questionRepository.delete(q);
		assertEquals(1, questionRepository.count());
	}

	@Test
	void 답변저장() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);
	}

	@Test
	void 답변조회() {
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Test
	@Transactional
	void 질문으로답변조회() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();
		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	void 데이터생성() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용없음";
			questionService.create(subject, content);
		}
	}
}
