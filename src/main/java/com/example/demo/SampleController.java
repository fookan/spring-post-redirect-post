package com.example.demo;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SampleController {

  int count = 0;

  @GetMapping("/")
  public String init() {
    return "index";
  }

  @PostMapping("/sample")
  public String sample(
      HttpServletRequest request,
      @ModelAttribute("sampleData") SampleData sampleData,
      Model model
  ) {
      System.out.println("**** POST表示");

      // forwardからきた場合、requestにデータが入っている
      String requestMessage = (String)request.getAttribute("message");
      if(!Objects.isNull(requestMessage)) {
        System.out.println(" REQUEST MESSAGE:" + requestMessage);
      }
      String modelMessage = (String)model.getAttribute("message");
      if(Objects.isNull(modelMessage)) {
        sampleData.setCode("");
      } else {
        System.out.println("MESSAGE:" + modelMessage);
      }
      System.out.println(sampleData.toString());
      return "sample";
  }

  @PostMapping("/sample-do")
  public String sampleDo(
      @ModelAttribute("sampleData") SampleData sampleData,
      RedirectAttributes redirectAttributes,
      HttpServletRequest httpServletRequest
  ) {
      System.out.println("**** POST登録");
      System.out.println(sampleData.toString());

      count++;
      System.out.println(" count:" + count);
      if (count % 2 == 0) {
        redirectAttributes.addFlashAttribute("message", "redirect-message");
      }
      httpServletRequest.setAttribute(
        View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

      return "redirect:/sample";
  }

  @PostMapping("/sample-forward")
  public String sampleForward(
      @ModelAttribute("sampleData") SampleData sampleData,
      HttpServletRequest request
  ) {
      System.out.println("**** POST-FOWARD");
      System.out.println(sampleData.toString());


      // fowardしてもredirectAttributeなどは使えない。以下は意味ない。やるならセッション管理
      request.setAttribute("message", "forward-message");

      // forwardだとURLが変わらない。
      return "forward:/sample";
  }
}
