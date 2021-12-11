package kdt.prgrms.kazedon.everevent.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kdt.prgrms.kazedon.everevent.EvereventApplication;
import kdt.prgrms.kazedon.everevent.domain.user.dto.SignUpRequest;
import kdt.prgrms.kazedon.everevent.service.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@SpringBootTest(classes = EvereventApplication.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CustomUserDetailService userDetailService;

  private SignUpRequest signUpRequest;

  private String userEmail;

  @BeforeEach
  public void setUp(
      WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .apply(
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(modifyUris().host("test.com").removePort(), prettyPrint())
                .withResponseDefaults(prettyPrint())
        )
        .alwaysDo(MockMvcResultHandlers.print())
        .build();

    userEmail = "test-user@gmail.com";
    signUpRequest = SignUpRequest.builder()
        .email(userEmail)
        .nickname("user-nickname")
        .password("password")
        .build();
  }

  @Test
  void signUp() throws Exception {
    //When
    given(userDetailService.signUp(ArgumentMatchers.any(SignUpRequest.class))).willReturn(1L);

    //Then
    mockMvc.perform(post("/api/v1/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("signUp",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("name"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("password"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("nickname")
            )
        ));
  }

  @Test
  @WithMockUser(roles = {"USER", "BUSINESS","ADMIN"})
  void logout() throws Exception {
    //When
    given(userDetailService.signUp(ArgumentMatchers.any(SignUpRequest.class))).willReturn(1L);

    //Then
    mockMvc.perform(post("/api/v1/logout")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("signUp",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }
}