package com.example.cabServiceMegaCity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cabServiceMegaCity.components.LoggedUserData;
import com.example.cabServiceMegaCity.models.UserModel;
import com.example.cabServiceMegaCity.services.UserServices;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/manageUsers")
public class UserControl {
	
	@Autowired 
	private UserServices userServices;
	@Autowired 
	private LoggedUserData loggedUserData;
	
	 @GetMapping
	    public String usersList(Model model) {
	        List<UserModel> users = userServices.getAllUsers();
	        model.addAttribute("users", users);
	        return "users";
	    }
	 
	 @GetMapping("/edit/{id}")
	 public String editUser(@PathVariable("id") int userId, Model model) {
	     UserModel user = userServices.getUser(userId);
	     if (user != null) {
	         model.addAttribute("user", user);
	         return "editUser"; 
	     } else {
	         model.addAttribute("error", "User not found");
	         return "redirect:/manageUsers"; 
	     }
	 }
	 
	 @GetMapping("/myProfile")
	    public String getClient(Model model, RedirectAttributes redirectAttributes) {
	        UserModel user = userServices.getUser(loggedUserData.getLoggedUser().getID());
	        
	        if (user != null) {
	            model.addAttribute("user", user);
	            return "profile"; 
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Client not found!");
	            return "redirect:/";
	        }
	    }


	 @PostMapping("/update")
	 public String updateUser(@RequestParam("id") int id, @ModelAttribute UserModel user, RedirectAttributes redirectAttributes) {
	     user.setID(id);

	     UserModel existingUser = userServices.getUser(id);
	     if (existingUser == null) {
	         redirectAttributes.addFlashAttribute("error", "User not found!");
	         return "redirect:/manageUsers";
	     }

	     if (!existingUser.getNic().equals(user.getNic()) && userServices.isNicExists(user.getNic())) {
	         redirectAttributes.addFlashAttribute("error", "NIC already exists for another user!");
	         return "redirect:/manageUsers/edit/" + id;
	     }

	     if (!existingUser.getUsername().equals(user.getUsername()) && userServices.isUsernameExists(user.getUsername())) {
	         redirectAttributes.addFlashAttribute("error", "Username already exists for another user!");
	         return "redirect:/manageUsers/edit/" + id;
	     }

	     if (!existingUser.getEmail().equals(user.getEmail()) && userServices.isEmailExists(user.getEmail())) {
	         redirectAttributes.addFlashAttribute("error", "Email already exists for another user!");
	         return "redirect:/manageUsers/edit/" + id;
	     }

	     boolean updated = userServices.updateUser(user);
	     if (updated) {
	         redirectAttributes.addFlashAttribute("success", "User updated successfully!");
	     } else {
	         redirectAttributes.addFlashAttribute("error", "User update failed!");
	     }
	     return "redirect:/manageUsers";
	 }
	 
	 @PostMapping("/profileUpdate")
	 public String updateProfile(@RequestParam("id") int id, @ModelAttribute UserModel user, RedirectAttributes redirectAttributes) {
	     user.setID(id);

	     UserModel existingUser = userServices.getUser(id);
	     if (existingUser == null) {
	         redirectAttributes.addFlashAttribute("error", "User not found!");
	         return "redirect:/manageUsers";
	     }

	     if (!existingUser.getNic().equals(user.getNic()) && userServices.isNicExists(user.getNic())) {
	         redirectAttributes.addFlashAttribute("error", "NIC already exists for another user!");
	         return "redirect:/manageUsers/myProfile/" + id;
	     }

	     if (!existingUser.getUsername().equals(user.getUsername()) && userServices.isUsernameExists(user.getUsername())) {
	         redirectAttributes.addFlashAttribute("error", "Username already exists for another user!");
	         return "redirect:/manageUsers/myProfile/" + id;
	     }

	     if (!existingUser.getEmail().equals(user.getEmail()) && userServices.isEmailExists(user.getEmail())) {
	         redirectAttributes.addFlashAttribute("error", "Email already exists for another user!");
	         return "redirect:/manageUsers/myProfile/" + id;
	     }

	     boolean updated = userServices.updateUser(user);
	     if (updated) {
	         redirectAttributes.addFlashAttribute("success", "User updated successfully!");
	     } else {
	         redirectAttributes.addFlashAttribute("error", "User update failed!");
	     }
	     return "redirect:/manageUsers/myProfile";
	 }

	 @GetMapping("/profile")
	 public String userProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
	     String username = (String) session.getAttribute("username");

	     if (username != null) {
	         UserModel user = userServices.getUserByUsername(username);
	         if (user != null) {
	             model.addAttribute("user", user);  // ✅ Pass user object to model
	             return "profile";                 // ✅ Load profile.html
	         } else {
	             redirectAttributes.addFlashAttribute("error", "User not found!");
	             return "redirect:/users/home";
	         }
	     } else {
	         redirectAttributes.addFlashAttribute("error", "Please log in first!");
	         return "redirect:/";
	     }
	 }

	 @GetMapping("/delete/{userID}")
	    public String deleteClient(@PathVariable int userID, RedirectAttributes redirectAttributes) {
	        UserModel user = userServices.getUser(userID);
	        if (user != null) {
	            userServices.deleteUser(userID);
	            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "User not found!");
	        }
	        return "redirect:/manageUsers";
	    }

}
