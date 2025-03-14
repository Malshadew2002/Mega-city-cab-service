package com.example.cabServiceMegaCity.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cabServiceMegaCity.components.LoggedUserData;
import com.example.cabServiceMegaCity.models.AmountModel;
import com.example.cabServiceMegaCity.models.BookingModel;
import com.example.cabServiceMegaCity.models.CabModel;
import com.example.cabServiceMegaCity.models.UserModel;
import com.example.cabServiceMegaCity.services.AmountServices;
import com.example.cabServiceMegaCity.services.BookingServices;
import com.example.cabServiceMegaCity.services.CabServices;

@Controller
@RequestMapping("/booking")
public class BookingControl {
    @Autowired
    private BookingServices bookingServices;
    @Autowired
    private LoggedUserData loggedUserData;
    @Autowired
    private CabServices cabServices;
    @Autowired
    private AmountServices amountServices;
    
    @GetMapping
    public String createBooking(Model model) {
        
        List<String> categories = Arrays.asList("van", "car");
        model.addAttribute("categories", categories);
        model.addAttribute("loggedUserData", loggedUserData);

        return "createBooking";
    }

    // Handle booking filter request
    @GetMapping("/filterCabs")
    public String filterBookings(@RequestParam(required = false) String category,
                                 @RequestParam(required = false) List<String> dates,
                                 Model model) {
    	model.addAttribute("loggedUserData", loggedUserData);
        List<String> categories = Arrays.asList("van", "car");
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedDates", dates);

        List<CabModel> filteredCabs = bookingServices.getAvailableCabs(category, dates);
        model.addAttribute("filteredCabs", filteredCabs);

        return "createBooking"; // Returns the HTML template
    }

    
    @GetMapping("/confirmBooking/{id}")
    public String confirmBooking(@PathVariable int id, 
                                 @RequestParam(required = false) String dates, 
                                 @RequestParam(required = false) String driverOption, 
                                 Model model) {
        model.addAttribute("loggedUserData", loggedUserData);

        // Fetch the logged-in user
        UserModel loggedUser = loggedUserData.getLoggedUser();

        // Check if the logged user is null
        if (loggedUser == null) {
            return "redirect:/"; // Redirect to the login page if no user is logged in
        }

        CabModel selectedCab = cabServices.getCab(id);

        if (selectedCab != null) {
            AmountModel amountModel = amountServices.getAmount(1);
            double taxPercentage = (amountModel != null) ? amountModel.getTax() : 0.00;
            double discountPercentage = (amountModel != null) ? amountModel.getDiscount() : 0.00;
            double driverAmount = (amountModel != null) ? amountModel.getDriverAmount() : 0.00;

            double amount = 0;
            double driverCost = 0;
            String selectedDates = dates;

            if (dates == null || dates.trim().isEmpty()) {
                selectedDates = "No dates selected";
            } else {
                amount = selectedCab.getPerDayAmount() * dates.split(",").length;
            }

            if ("With Driver".equals(driverOption)) {
                driverCost = driverAmount * dates.split(",").length;
                amount += driverCost;
            }

            double totalTax = amount * (taxPercentage / 100);
            double totalDiscount = amount * (discountPercentage / 100);

            double totalAmount = amount + totalTax - totalDiscount;

            model.addAttribute("cab", selectedCab);
            model.addAttribute("name", loggedUser.getName()); // Use loggedUser to get the name
            model.addAttribute("selectedDates", selectedDates);
            model.addAttribute("driverOption", driverOption);
            model.addAttribute("taxPercentage", taxPercentage);
            model.addAttribute("discountPercentage", discountPercentage);
            model.addAttribute("totalTax", totalTax);
            model.addAttribute("totalDiscount", totalDiscount);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("driverAmount", driverAmount);

            return "confirmBooking";
        } else {
            model.addAttribute("error", "Cab not found.");
            return "error";
        }
    }

    
    @PostMapping("/confirm")
    public String confirmBookingSubmission(@RequestParam int cabId,
                                           @RequestParam String selectedDates,
                                           @RequestParam String driverOption,
                                           @RequestParam double totalAmount,
                                           @RequestParam String userName,
                                           @RequestParam String contactNumber,
                                           Model model) {
    	
    	if (loggedUserData.getLoggedUser() == null) {
            return "redirect:/";
        }
        
        BookingModel booking = new BookingModel();
        booking.setUserID(loggedUserData.getLoggedUser().getID());
        booking.setCabID(cabId);
        booking.setUserFullName(userName);
        booking.setUserContactNumber(contactNumber);
        booking.setDriverOption(driverOption);
        booking.setDates(selectedDates);
        booking.setBookingDate(LocalDate.now().toString());
        booking.setTotalAmount(totalAmount);

        BookingModel savedBooking = bookingServices.newBooking(booking);
        CabModel cab = cabServices.getCab(cabId);

        if (savedBooking != null) {
            AmountModel amountModel = amountServices.getAmount(1);
            double taxPercentage = (amountModel != null) ? amountModel.getTax() : 0.00;
            double discountPercentage = (amountModel != null) ? amountModel.getDiscount() : 0.00;
            double driverAmount = (amountModel != null) ? amountModel.getDriverAmount() : 0.00;

            double baseAmount = cab.getPerDayAmount() * selectedDates.split(",").length;
            double driverCost = "With Driver".equals(driverOption) ? driverAmount * selectedDates.split(",").length : 0;
            double totalTax = (baseAmount + driverCost) * (taxPercentage / 100);
            double totalDiscount = (baseAmount + driverCost) * (discountPercentage / 100);
            double totalFinalAmount = baseAmount + driverCost + totalTax - totalDiscount;

            model.addAttribute("booking", savedBooking);
            model.addAttribute("cab", cab);
            model.addAttribute("baseAmount", baseAmount);
            model.addAttribute("driverCost", driverCost);
            model.addAttribute("taxPercentage", taxPercentage);
            model.addAttribute("discountPercentage", discountPercentage);
            model.addAttribute("totalTax", totalTax);
            model.addAttribute("totalDiscount", totalDiscount);
            model.addAttribute("totalAmount", totalFinalAmount);

            return "successBooking";
        } else {
            model.addAttribute("errorMessage", "Failed to confirm booking.");
            return "confirmBooking";
        }
    }
    
    @GetMapping("/myBookings")
    public String viewMyBookings(Model model) {
        if (loggedUserData.getLoggedUser() == null) {
            return "redirect:/";
        }

        int userID = loggedUserData.getLoggedUser().getID();
        List<BookingModel> myBookings = bookingServices.getBookingsByUserID(userID);
        List<CabModel> cabDetails = new ArrayList<>();

        for (BookingModel booking : myBookings) {
            cabDetails.add(cabServices.getCab(booking.getCabID()));
        }

        model.addAttribute("loggedUserData", loggedUserData);
        model.addAttribute("myBookings", myBookings);
        model.addAttribute("cabDetails", cabDetails);

        return "myBookings";
    }
    @GetMapping("/allBookings")
    public String getAllBookings(Model model) {
        List<BookingModel> bookings = bookingServices.getAllBookings();
        List<CabModel> cabDetails = new ArrayList<>();

        for (BookingModel booking : bookings) {
            CabModel cab = cabServices.getCab(booking.getCabID());
            if (cab != null) {
                cabDetails.add(cab);
            } else {
                cabDetails.add(new CabModel()); // Add an empty CabModel if no cab is found
            }
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("cabDetails", cabDetails);
        return "bookings"; // Ensure this matches the name of your Thymeleaf template
    }
    
    @GetMapping("/adminDeleteBooking/{bookingID}")
    public String adminDeleteBooking(@PathVariable int bookingID, RedirectAttributes redirectAttributes) {
        boolean isDeleted = bookingServices.deleteBooking(bookingID);

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking canceled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel booking.");
        }

        return "redirect:/booking/allBookings";
    }
    
    @GetMapping("/deleteBooking/{bookingID}")
    public String deleteBooking(@PathVariable int bookingID, RedirectAttributes redirectAttributes) {
        boolean isDeleted = bookingServices.deleteBooking(bookingID);

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking canceled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel booking.");
        }

        return "redirect:/booking/myBookings";
    }
  

}
