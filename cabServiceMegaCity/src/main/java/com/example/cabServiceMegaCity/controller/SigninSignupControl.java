package com.example.cabServiceMegaCity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cabServiceMegaCity.components.LoggedUserData;
import com.example.cabServiceMegaCity.models.UserModel;
import com.example.cabServiceMegaCity.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class SigninSignupControl {

    @Autowired
    private UserServices services;
    @Autowired
    private LoggedUserData loggedUserData;

    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; 
    }
    
    @GetMapping("/admin")
    public String adminPage() {
        return "admin";  
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "home";  
    }
    
    @GetMapping("/profile")
    public String profilePage() {
        return "profile";  
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute UserModel user, RedirectAttributes redirectAttributes) {
        if (services.isNicExists(user.getNic())) {
            redirectAttributes.addFlashAttribute("error", "NIC already exists!");
            return "redirect:/users/signup";
        }
        if (services.isUsernameExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/users/signup";
        }
        if (services.isEmailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists!");
            return "redirect:/users/signup";
        }

        UserModel newUser = services.addUser(user);

        if (newUser == null) {
            redirectAttributes.addFlashAttribute("error", "Registration failed!");
            return "redirect:/users/signup";
        }

        redirectAttributes.addFlashAttribute("success", "Registration Successful!");
        return "redirect:/users/signup";  
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, 
                        @RequestParam("password") String password, 
                        HttpSession session, 
                        RedirectAttributes redirectAttributes) {
        
        if ("admin".equals(username) && "1234".equals(password)) {
            return "redirect:/users/admin"; 
        } 
        
        UserModel user = services.getUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
        	UserModel userModel = services.getUserByUsername(username);
            loggedUserData.setLoggedUser(userModel);
            return "redirect:/users/home";  
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password!");
            return "redirect:/";
        }
    }

}
