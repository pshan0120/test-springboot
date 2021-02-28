package com.chandy.test.springboot.web;

import com.chandy.test.springboot.config.auth.LoginUser;
import com.chandy.test.springboot.config.auth.dto.SessionUser;
import com.chandy.test.springboot.domain.posts.PostsRepository;
import com.chandy.test.springboot.service.posts.PostsService;
import com.chandy.test.springboot.web.dto.PostsResponseDto;
import javax.mail.Session;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

  private final PostsService postsService;
  private final HttpSession httpSession;

  @GetMapping("/")
  public String index(Model model, @LoginUser SessionUser user) {
    model.addAttribute("posts", postsService.findAllDesc());
    // CustomOAuth2UserService에서 세션에 저장한 값
    if(user != null) {
      model.addAttribute("userName", user.getName());
    }
    return "index";
  }

  @GetMapping("/posts/save")
  public String postsSave() {
    return "posts-save";
  }

  @GetMapping("/posts/update/{id}")
  public String postsUpdate(@PathVariable Long id, Model model) {
    PostsResponseDto dto = postsService.findById(id);
    model.addAttribute("post", dto);
    return "posts-update";
  }

}
