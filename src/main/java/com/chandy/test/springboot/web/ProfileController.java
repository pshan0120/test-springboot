package com.chandy.test.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProfileController {
  private final Environment env;

  @GetMapping("/profile")
  public String profile() {
    // 현재 실행중 activeProfile을 모두 가져옴
    // real, outh, read-db등이 활성화라면 모두 담겨 있음
    List<String> profiles = Arrays.asList(env.getActiveProfiles());
    // real, rea1, real2만 뽑아서 반환, real은 step2에서 사용해 볼 수 있으면 가져옴
    List<String> realProfiles = Arrays.asList("real", "real1", "real2");
    String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);

    return profiles.stream()
        .filter(realProfiles::contains)
        .findAny()
        .orElse(defaultProfile);
  }
}
