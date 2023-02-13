package com.hcu.hot6;

import com.hcu.hot6.domain.request.PostCreationRequest;
import com.hcu.hot6.domain.request.PostUpdateRequest;
import com.hcu.hot6.domain.response.PostCreationResponse;
import com.hcu.hot6.domain.response.PostReadOneResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
/**
 * Post에 관련된 API의 정상, 비정상 작동 상황 모두 테스트
 *
 * 기타
 * - Post Create 시, LocalDateTime에 실제 날짜의 범위를 넘어선 값이 들어오는 경우에 대해서는 Validation 어노테이션 필요 없음 -> 자바에서 LocalDateTime으로 변환하는 과정에서 Validation 체크
 * */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Transactional
public class PostApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // ==== CREATE ==== //

    /**
     * Project, Study, Mentoring 각각, 정상 create 케이스 테스트
     * */
    @DisplayName("멘토링 생성 API 테스트 : Normal")
    @Test
    public void createMentoring() throws Exception{
        // 1L를 PK로 가지는 유저가 있는 상태에서 테스트. -> 통과
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("M");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setHasPay(true);
        request.setMaxMentor(0);
        request.setMaxMentee(4);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        Assertions.assertThat(response.getBody().getDtype().compareTo("M") == 0);
        Assertions.assertThat(response.getBody().getTitle().compareTo("제목 test") == 0);
    }

    @DisplayName("프로젝트 생성 API 테스트 : Normal")
    @Test
    public void createProject() throws Exception{
        // 1L를 PK로 가지는 유저가 있는 상태에서 테스트. -> 통과
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("P");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setHasPay(true);
        request.setMaxDeveloper(3);
        request.setMaxPlanner(2);
        request.setMaxDesigner(1);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        Assertions.assertThat(response.getBody().getDtype().compareTo("P") == 0);
        Assertions.assertThat(response.getBody().getTitle().compareTo("제목 test") == 0);
    }

    @DisplayName("스터디 생성 API 테스트 : Normal")
    @Test
    public void createStudy() throws Exception{
        // 1L를 PK로 가지는 유저가 있는 상태에서 테스트. -> 통과
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("S");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setMaxMember(5);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        Assertions.assertThat(response.getBody().getDtype().compareTo("S") == 0);
        Assertions.assertThat(response.getBody().getTitle().compareTo("제목 test") == 0);
    }


    /**
     * Validation 체크. RequestBody가 정상적으로 들어왔는지 다음과 같은 기준으로 확인한다. (PostCreationRequest에서 사용한 Validation 어노테이션이 잘 작동하는지 확인.)
     *
     * 1. 필수로 입력해야 하는 사항에 대해 입력하지 않은 경우 BAD_REQUEST 에러가 나야 한다.
     * 2. 날짜 입력 @Future
     * 3. 모집 인원 @PositiveOrZero
     * 5. hasPay
     * */

    /**
     * Validation 체크 1번 항목
     * */
    @DisplayName("PostCreationRequest @NotNull validation 체크 - BAD_REQUEST 에러가 나야 한다.")
    @Test
    public void notnull_validation() throws Exception{
        // dtype, title, contact, authorId, postEnd, projectEnd, projectStart 각각 주석 처리 해가면서 체크하기
        // String, String으로 포맷팅 되는 LocalDateTime은 validation 체크 가능하지만, hasPay는 Boolean 타입이라 여기서 validation 체크 불가능함
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("S");
        //request.setDtype("P");
        //request.setDtype("M");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        //request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setHasPay(true);
//        멘토링인 경우 아래 코드 활성화
//        request.setMaxMember(5);
//        프로젝트인 경우 아래 코드 활성화
//        request.setMaxDeveloper(3);
//        request.setMaxPlanner(2);
//        request.setMaxDesigner(1);
//        멘토링인 경우 아래 코드 활성화
//        request.setMaxMentor(1);
//        request.setMaxMentee(4);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * Validation 체크 2번 항목
     * */
    @DisplayName("날짜 입력 중 과거 날짜가 하나라도 들어온 상황에는 BAD_REQUEST 상태가 반환되어야 한다.")
    @Test
    public void 날짜입력_Future(){
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("P");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2022, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setHasPay(true);
        request.setMaxDeveloper(3);
        request.setMaxPlanner(2);
        request.setMaxDesigner(1);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    /**
     * Validation 체크 3번 항목
     *
     * Project, Study, Mentoring 각각, 모집 인원 음수인 경우 BAD_REQUEST 에러 나는지 테스트
     * 각 필드에 필요한 max 모집 인원 하나라도 음수를 입력하면 BAD_REQUEST 에러가 난다.
     * 하지만 실수로 필요한 max 모집 인원 필드를 채우지 않고 보냈을 때에는 엔티티의 @NotNull 어노테이션이 0으로 채워주기 때문에 정상 작동하는데에 문제가 없다.
     * */
    @DisplayName("max 모집 인원 수가 음수 이므로, bad request 에러가 나야 합니다.")
    @Test
    public void 모집인원_음수_study() throws Exception{
        // 1L를 PK로 가지는 유저가 있는 상태에서 테스트. -> 통과
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("S");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setMaxMember(-1);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("max 모집 인원 수가 음수 이므로, bad request 에러가 나야 합니다.")
    @Test
    public void 모집인원_음수_project() throws Exception{
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("P");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setHasPay(true);
        request.setMaxDeveloper(-3);
        request.setMaxPlanner(2);
        request.setMaxDesigner(1);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("max 모집 인원 수가 음수 이므로, bad request 에러가 나야 합니다.")
    @Test
    public void 모집인원_음수_mentoring() throws Exception{
        PostCreationRequest request = new PostCreationRequest();
        request.setDtype("M");
        request.setTitle("제목 test");
        request.setContent("내용 test");
        request.setContact("연락처 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 2, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 10, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        request.setAuthorId(1L);
        request.setHasPay(true);
        request.setMaxMentor(-1);
        request.setMaxMentee(4);

        String url = "http://localhost:"+port+"/posts";

        ResponseEntity<PostCreationResponse> response = restTemplate.postForEntity(url, request, PostCreationResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    // ==== DELETE ==== //

    @DisplayName("모집글 삭제 정상 케이스 테스트 - 삭제된 모집글의 PK가 반환되어야 한다.")
    @Test
    public void 모집글_삭제(){
        // url에 id와 Assertions의 id를 현재 디비에 있는 값으로 바꿔서 테스트 진행하기
        String url = "http://localhost:"+port+"/posts/12";
        ResponseEntity<Long> result
                = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Long.class
        );

        Assertions.assertThat(result.getBody()).isEqualTo(12L);
    }

    // === READ === //

    @DisplayName("모집글 상세 보기 정상 작동 - PostReadOneResponse 데이터를 정상적으로 반환 받아야 한다.")
    @Test
    public void 모집글_상세_보기(){
        // url에 id와 Assertions의 id를 현재 디비에 있는 값으로 바꿔서 테스트 진행하기
        String url = "http://localhost:"+port+"/posts/1";
        ResponseEntity<PostReadOneResponse> result
                = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                PostReadOneResponse.class
        );

        Assertions.assertThat(result.getBody().getDtype()).isEqualTo("M");
        Assertions.assertThat(result.getBody().getId()).isEqualTo(1L);
        Assertions.assertThat(result.getBody().getTitle()).isEqualTo("제목 test");
        Assertions.assertThat(result.getBody().getContent()).isEqualTo("내용 test");
        Assertions.assertThat(result.getBody().getContact()).isEqualTo("연락처 test");
        Assertions.assertThat(result.getBody().getPostStart().toString()).isEqualTo("2023-02-10T12:38:43.516279");
        Assertions.assertThat(result.getBody().getPostEnd().toString()).isEqualTo("2023-03-02T00:00");
        Assertions.assertThat(result.getBody().getProjectStart().toString()).isEqualTo("2023-03-10T00:00");
        Assertions.assertThat(result.getBody().getProjectEnd().toString()).isEqualTo("2023-07-02T00:00");
        Assertions.assertThat(result.getBody().getWriter()).isEqualTo("memberA");
        Assertions.assertThat(result.getBody().getMaxMentor()).isEqualTo(0);
        Assertions.assertThat(result.getBody().getMaxMentee()).isEqualTo(4);
        Assertions.assertThat(result.getBody().getCurrMentor()).isEqualTo(0);
        Assertions.assertThat(result.getBody().getCurrMentee()).isEqualTo(0);
        Assertions.assertThat(result.getBody().isHasPay()).isEqualTo(true);

    }

    // === UPDATE === //
    @DisplayName("Mentoring : 수정된 내용들이 바뀌고, 모집인원 현황이 바뀌고, total, currTotal도 갱신되어야 한다.")
    @Test
    public void 모집글_수정_Mentoring(){
        // isComplete = false, dtype 빼고는 모두 수정한 상태
        PostUpdateRequest request = new PostUpdateRequest();
        request.setDtype("M"); // 수정해서는 안된다. 유형 알려주는 용도
        request.setTitle("제목 수정 test");
        request.setContent("내용 수정 test");
        request.setContact("연락처 수정 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 3, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 11, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 3, 0, 0, 0));
        request.setCompleted(false);
        request.setHasPay(false);
        request.setMaxMentor(2);
        request.setMaxMentee(3);
        request.setCurrMentor(1);
        request.setCurrMentee(2);

        HttpEntity<PostUpdateRequest> httpEntity = new HttpEntity<>(request);

        // url에 id와 Assertions의 id를 현재 디비에 있는 값으로 바꿔서 테스트 진행하기
        String url = "http://localhost:"+port+"/posts/1";
        ResponseEntity<PostReadOneResponse> result
                = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                PostReadOneResponse.class
        );

        Assertions.assertThat(result.getBody().getDtype()).isEqualTo("M");
        Assertions.assertThat(result.getBody().getId()).isEqualTo(1L);
        Assertions.assertThat(result.getBody().getTitle()).isEqualTo("제목 수정 test");
        Assertions.assertThat(result.getBody().getContent()).isEqualTo("내용 수정 test");
        Assertions.assertThat(result.getBody().getContact()).isEqualTo("연락처 수정 test");
        Assertions.assertThat(result.getBody().getPostStart().toString()).isEqualTo("2023-02-10T12:38:43.516279");
        Assertions.assertThat(result.getBody().getPostEnd().toString()).isEqualTo("2023-03-03T00:00");
        Assertions.assertThat(result.getBody().getProjectStart().toString()).isEqualTo("2023-03-11T00:00");
        Assertions.assertThat(result.getBody().getProjectEnd().toString()).isEqualTo("2023-07-03T00:00");
        Assertions.assertThat(result.getBody().getWriter()).isEqualTo("memberA");
        Assertions.assertThat(result.getBody().getMaxMentor()).isEqualTo(2);
        Assertions.assertThat(result.getBody().getMaxMentee()).isEqualTo(3);
        Assertions.assertThat(result.getBody().getCurrMentor()).isEqualTo(1);
        Assertions.assertThat(result.getBody().getCurrMentee()).isEqualTo(2);
        Assertions.assertThat(result.getBody().isHasPay()).isEqualTo(false);
    }

    @DisplayName("Project : 수정된 내용들이 바뀌고, 모집인원 현황이 바뀌고, total, currTotal도 갱신되어야 한다.")
    @Test
    public void 모집글_수정_Project(){
        // isComplete = false, dtype 빼고는 모두 수정한 상태
        PostUpdateRequest request = new PostUpdateRequest();
        request.setDtype("P"); // 수정해서는 안된다. 유형 알려주는 용도
        request.setTitle("제목 수정 test");
        request.setContent("내용 수정 test");
        request.setContact("연락처 수정 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 3, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 11, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 3, 0, 0, 0));
        request.setCompleted(false);
        request.setHasPay(false);
        request.setMaxDeveloper(2);
        request.setMaxPlanner(3);
        request.setMaxDesigner(2);
        request.setCurrDeveloper(1);
        request.setCurrPlanner(1);
        request.setCurrDesigner(1);

        HttpEntity<PostUpdateRequest> httpEntity = new HttpEntity<>(request);

        // url에 id와 Assertions의 id를 현재 디비에 있는 값으로 바꿔서 테스트 진행하기
        String url = "http://localhost:"+port+"/posts/2";
        ResponseEntity<PostReadOneResponse> result
                = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                PostReadOneResponse.class
        );

        Assertions.assertThat(result.getBody().getDtype()).isEqualTo("P");
        Assertions.assertThat(result.getBody().getId()).isEqualTo(2L);
        Assertions.assertThat(result.getBody().getTitle()).isEqualTo("제목 수정 test");
        Assertions.assertThat(result.getBody().getContent()).isEqualTo("내용 수정 test");
        Assertions.assertThat(result.getBody().getContact()).isEqualTo("연락처 수정 test");
        Assertions.assertThat(result.getBody().getPostStart().toString()).isEqualTo("2023-02-10T13:48:24.255690");
        Assertions.assertThat(result.getBody().getPostEnd().toString()).isEqualTo("2023-03-03T00:00");
        Assertions.assertThat(result.getBody().getProjectStart().toString()).isEqualTo("2023-03-11T00:00");
        Assertions.assertThat(result.getBody().getProjectEnd().toString()).isEqualTo("2023-07-03T00:00");
        Assertions.assertThat(result.getBody().getWriter()).isEqualTo("memberA");
        Assertions.assertThat(result.getBody().getMaxDeveloper()).isEqualTo(2);
        Assertions.assertThat(result.getBody().getMaxPlanner()).isEqualTo(3);
        Assertions.assertThat(result.getBody().getMaxDesigner()).isEqualTo(2);
        Assertions.assertThat(result.getBody().getCurrDeveloper()).isEqualTo(1);
        Assertions.assertThat(result.getBody().getCurrPlanner()).isEqualTo(1);
        Assertions.assertThat(result.getBody().getCurrDesigner()).isEqualTo(1);
        Assertions.assertThat(result.getBody().isHasPay()).isEqualTo(false);
    }

    @DisplayName("Study : 수정된 내용들이 바뀌고, 모집인원 현황이 바뀌고, total, currTotal도 갱신되어야 한다.")
    @Test
    public void 모집글_수정_Study(){
        // isComplete = false, dtype 빼고는 모두 수정한 상태
        PostUpdateRequest request = new PostUpdateRequest();
        request.setDtype("S"); // 수정해서는 안된다. 유형 알려주는 용도
        request.setTitle("제목 수정 test");
        request.setContent("내용 수정 test");
        request.setContact("연락처 수정 test");
        request.setPostEnd(LocalDateTime.of(2023, 3, 3, 0, 0, 0));
        request.setProjectStart(LocalDateTime.of(2023, 3, 11, 0, 0, 0));
        request.setProjectEnd(LocalDateTime.of(2023, 7, 3, 0, 0, 0));
        request.setCompleted(false);
        request.setMaxMember(4);
        request.setCurrMember(1);

        HttpEntity<PostUpdateRequest> httpEntity = new HttpEntity<>(request);

        // url에 id와 Assertions의 id를 현재 디비에 있는 값으로 바꿔서 테스트 진행하기
        String url = "http://localhost:"+port+"/posts/3";
        ResponseEntity<PostReadOneResponse> result
                = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                PostReadOneResponse.class
        );

        Assertions.assertThat(result.getBody().getDtype()).isEqualTo("S");
        Assertions.assertThat(result.getBody().getId()).isEqualTo(3L);
        Assertions.assertThat(result.getBody().getTitle()).isEqualTo("제목 수정 test");
        Assertions.assertThat(result.getBody().getContent()).isEqualTo("내용 수정 test");
        Assertions.assertThat(result.getBody().getContact()).isEqualTo("연락처 수정 test");
        Assertions.assertThat(result.getBody().getPostStart().toString()).isEqualTo("2023-02-10T13:57:39.402273");
        Assertions.assertThat(result.getBody().getPostEnd().toString()).isEqualTo("2023-03-03T00:00");
        Assertions.assertThat(result.getBody().getProjectStart().toString()).isEqualTo("2023-03-11T00:00");
        Assertions.assertThat(result.getBody().getProjectEnd().toString()).isEqualTo("2023-07-03T00:00");
        Assertions.assertThat(result.getBody().getWriter()).isEqualTo("memberA");
        Assertions.assertThat(result.getBody().getMaxMember()).isEqualTo(4);
        Assertions.assertThat(result.getBody().getCurrMember()).isEqualTo(1);
    }

}