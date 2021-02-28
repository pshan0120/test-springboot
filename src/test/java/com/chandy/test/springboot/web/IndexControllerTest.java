package com.chandy.test.springboot.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void mainpage_loading() {
    //when
    String body = this.restTemplate.getForObject("/", String.class);
    System.out.println(body);

    //then
    assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
  }


}
