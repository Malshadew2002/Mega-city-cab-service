package com.example.cabServiceMegaCity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.cabServiceMegaCity.services.UserServices;
import com.example.cabServiceMegaCity.services.BookingServices;
import com.example.cabServiceMegaCity.services.CabServices;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {
	
	 @Autowired
	    private UserServices userServices;
	    
	    @Autowired
	    private CabServices cabServices;
	    
	    @Autowired
	    private BookingServices bookingServices;
	    
	    
	    @GetMapping("/stats")
	    public Map<String, Integer> getDashboardStats() {
	        Map<String, Integer> stats = new HashMap<>();
	        
	        // Updated keys to match the frontend
	        stats.put("userCount", Objects.requireNonNullElse(userServices.getUserCount(), 0));
	        stats.put("cabsCount", Objects.requireNonNullElse(cabServices.getCabsCount(), 0));
	        stats.put("bookingCount", Objects.requireNonNullElse(bookingServices.getBookingCount(), 0));


	        return stats;
	    }


}
