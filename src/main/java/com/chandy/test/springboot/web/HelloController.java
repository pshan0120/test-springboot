package com.chandy.test.springboot.web;

import com.chandy.test.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

  @GetMapping("/calc")
  public String calc() {
    return String.valueOf(1+5);
  }

  @GetMapping("/hello/dto")
  public HelloResponseDto helloDto(@RequestParam("name") String name,
      @RequestParam("amount") int amount) {
    System.out.println("qwrr");
    return new HelloResponseDto(name, amount);
  }


}
