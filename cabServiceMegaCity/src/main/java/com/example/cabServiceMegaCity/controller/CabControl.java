package com.example.cabServiceMegaCity.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cabServiceMegaCity.models.CabModel;
import com.example.cabServiceMegaCity.services.CabServices;

@Controller
@RequestMapping("/manageCabs")
public class CabControl {
	@Autowired 
    private CabServices cabServices;
	
    @GetMapping("/newCab")
    public String newCab(Model model) {
        model.addAttribute("cab", new CabModel());
        return "newCab";
    }

	
    @PostMapping("/addCab")
    public String addCab(@ModelAttribute CabModel cab, 
            @RequestParam("file") MultipartFile file, 
            RedirectAttributes redirectAttributes) {
        
        if (!file.isEmpty()) {
            try {
                String uploadDir = "uploads/"; // Directory where files will be saved
                Path uploadPath = Paths.get(uploadDir);

                // Create the directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Get the original filename
                String fileName = file.getOriginalFilename();

                // Resolve the full path for the file
                Path filePath = uploadPath.resolve(fileName);

                // Copy the file to the target location
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Set the image URL in the CabModel (only the filename, not the full path)
                cab.setImage(fileName);
            } catch (IOException e) {
                // If file upload fails, add error message and redirect back
                redirectAttributes.addFlashAttribute("error", "File upload failed!");
                return "redirect:/manageCabs/newCab";
            }
        }

        // Save the cab to the database
        CabModel newCab = cabServices.addCab(cab);
        
        if (newCab == null) {
            // If cab number already exists, add error and redirect
            redirectAttributes.addFlashAttribute("error", "Cab number already exists!");
            return "redirect:/manageCabs/newCab";
        }
        
        // On success, add success message and redirect
        redirectAttributes.addFlashAttribute("success", "Cab added successfully!");
        return "redirect:/manageCabs";
    }

    
    @GetMapping
    public String cabsList(Model model) {
        List<CabModel> cabs = cabServices.getAllCabs();
        model.addAttribute("cabs", cabs);
        return "cabs";
    }

    @GetMapping("/edit/{id}")
    public String editCab(@PathVariable("id") int cabId, Model model) {
        CabModel cab = cabServices.getCab(cabId);
        if (cab != null) {
            model.addAttribute("cab", cab);
            return "editCab";
        } else {
            return "redirect:/manageCabs"; 
        }
    }

    @PostMapping("/edit")
    public String updateCab(CabModel cab, 
                            @RequestParam(value = "file", required = false) MultipartFile file, 
                            RedirectAttributes redirectAttributes) {
        // Check if cab number already exists
        CabModel existingCab = cabServices.getCabByCabNumber(cab.getCabNumber());
        if (existingCab != null && existingCab.getId() != cab.getId()) {
            redirectAttributes.addFlashAttribute("error", "Cab number already exists!");
            return "redirect:/manageCabs/edit/" + cab.getId();  
        }

        // Handle file upload if a new image is provided
        if (file != null && !file.isEmpty()) {
            try {
                String uploadDir = "uploads/"; // Directory where files will be saved
                Path uploadPath = Paths.get(uploadDir);

                // Create the directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Get the original filename
                String fileName = file.getOriginalFilename();

                // Resolve the full path for the file
                Path filePath = uploadPath.resolve(fileName);

                // Copy the file to the target location
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Set the image URL in the CabModel (only the filename, not the full path)
                cab.setImage(fileName);
            } catch (IOException e) {
                // If file upload fails, add error message and redirect back
                redirectAttributes.addFlashAttribute("error", "File upload failed!");
                return "redirect:/manageCabs/edit/" + cab.getId();
            }
        } else {
            // If no new file is uploaded, keep the existing image
            CabModel currentCab = cabServices.getCab(cab.getId());
            cab.setImage(currentCab.getImage());
        }

        // Update the cab details in the database
        boolean updated = cabServices.updateCab(cab);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Cab updated successfully!");
            return "redirect:/manageCabs";  
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update the cab.");
            return "redirect:/manageCabs/edit/" + cab.getId();  
        }
    }

    
    @GetMapping("/delete/{id}")
    public String deleteCab(@PathVariable("id") int cabId, RedirectAttributes redirectAttributes) {
        boolean deleted = cabServices.deleteCab(cabId);

        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Cab deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete the cab.");
        }
        return "redirect:/manageCabs";
    }
}
