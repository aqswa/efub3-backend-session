package efub.session.blog.domain.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.session.blog.domain.account.domain.Account;
import efub.session.blog.domain.account.dto.response.AccountResponseDto;
import efub.session.blog.domain.account.dto.request.SignUpRequestDto;
import efub.session.blog.domain.account.service.AccountService;
import org.hibernate.sql.OracleJoinFragment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @MockBean
    AccountService accountService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    public void signUp_성공() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("email@email.com", "pw", "nickname", "bio");
        Account account = new Account(1L, "email@email.com", "pw", "nickname", "bio");
        //AccountResponseDto accountResponseDto = mock(AccountResponseDto.class);

        when(accountService.signUp(signUpRequestDto)).thenReturn(account);

        mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().is4xxClientError());

        //AccountResponseDto returnedDto = accountController.signUp(signUpRequestDto);

        //verify(accountService).signUp(signUpRequestDto);
    }

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    public void signUp_실패() throws Exception {

    }

    @Test
    @DisplayName("계정 조회 컨트롤러 테스트")
    void getAccount_성공() throws Exception {

    }

    @Test
    @DisplayName("계정 조회 컨트롤러 테스트")
    void getAccount_실패() throws Exception {

    }

    @Test
    @DisplayName("계정 업데이트 컨트롤러 테스트")
    void update() throws Exception {

    }

    @Test
    void withdraw() {
    }

    @Test
    void delete() {
    }
}