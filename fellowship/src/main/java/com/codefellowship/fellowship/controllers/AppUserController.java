package com.codefellowship.fellowship.controllers;

import com.codefellowship.fellowship.models.AppUser;
import com.codefellowship.fellowship.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.GeneratedValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.security.Principal;
import java.time.LocalDateTime;

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

    @GetMapping("/test")
    public String getTestPage(Principal hp, Model p){
        if(hp != null){
            String username = hp.getName();
            AppUser appUser = appUserRepo.findByUserName(username);

            p.addAttribute("username", username);
        }
        return "test";
    }

    @GetMapping("/users/{id}")
    public String getUserInfo(Model p, Principal hp, @PathVariable Long id){
        if(hp != null){
            String username = hp.getName();
            AppUser appUser = appUserRepo.findByUserName(username);

            p.addAttribute("username", username);
        }

        AppUser dbUser = appUserRepo.findById(id).orElseThrow();
        p.addAttribute("dbUserUsername", dbUser.getUsername());
        p.addAttribute("dbUserId",dbUser.getId());

        p.addAttribute("testDate", LocalDateTime.now());
        return "userInfo";
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
    public RedirectView createUser(String username, String password, String firstName, String lastName, String gender, int month, int day, int year, Image img){
      String hashedPassword = passwordEncoder.encode(password);
      AppUser newUser = new AppUser(username, hashedPassword, firstName, lastName, gender, month, day, year, img);
      appUserRepo.save(newUser);
      authWithHttpServletRequest(username, password, firstName, lastName, gender, month, day, year, img);
      return new RedirectView("/");
    }

    @PostMapping("/test")
    public RedirectView testUser(){
        String hashedPassword = passwordEncoder.encode("password");
        AppUser newUser = new AppUser("Pira", hashedPassword, "Saphira", "Velazquez", "Female", 12, 3, 2006, com/codefellowship/fellowship/pira.jpg);
    }

    @PutMapping("/users/{id}")
    public RedirectView editUserInfo(Model p, Principal hp, @PathVariable Long id, String username, RedirectAttributes redir){
        if(hp != null && hp.getName().equals(username)){
            AppUser newUser = appUserRepo.findById(id).orElseThrow();
            newUser.setUsername(username);
            appUserRepo.save(newUser);
        } else {
            redir.addFlashAttribute("errorMessage", "Cannot edit another user's info");
        }
        return new RedirectView("/users/" + id);

    }

    public void authWithHttpServletRequest(String username,String password, String firstName, String lastName, String gender, int month, int day, int year, Image img){
        try{
            request.login(username, password);
        } catch (ServletException e){
            e.printStackTrace();
        }
    }
}
