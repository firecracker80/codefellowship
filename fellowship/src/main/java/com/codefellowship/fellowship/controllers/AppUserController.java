package com.codefellowship.fellowship.controllers;

import com.codefellowship.fellowship.models.AppUser;
import com.codefellowship.fellowship.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.GeneratedValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class AppUserController {
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    HttpServletRequest request;

    @GetMapping("/")
    public String getHomePage(Principal hp, Model p){
        if(hp !=null){
            String username = hp.getName();
            AppUser newAppUser = appUserRepo.findByUserName(username);

            p.addAttribute("username", username);
        }
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password){
      String hashedPassword = passwordEncoder.encode(password);
      AppUser newUser = new AppUser(username, hashedPassword);
      appUserRepo.save(newUser);
      authWithHttpServletRequest(username, password);
      return new RedirectView("/");
    }

    public void authWithHttpServletRequest(String username,String password){
        try{
            request.login(username, password);
        } catch (ServletException e){
            e.printStackTrace();
        }
    }
}
