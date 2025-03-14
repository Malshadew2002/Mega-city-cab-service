package com.example.cabServiceMegaCity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cabServiceMegaCity.models.AmountModel;
import com.example.cabServiceMegaCity.services.AmountServices;

@Controller
@RequestMapping("/amounts")
public class AmountControl {
	@Autowired
    private AmountServices amountServices;

    public void ensureDefaultAmount() {
        amountServices.addDefaultAmounts();
    }

    @GetMapping
    public String getAmount(Model model) {
        // Ensure default amount exists
        amountServices.addDefaultAmounts();

        AmountModel amount = amountServices.getAmount(1);
        model.addAttribute("amount", amount);
        return "amounts";
    }

    @PostMapping("/update")
    public String updateAmount(@ModelAttribute AmountModel amountModel, RedirectAttributes redirectAttributes) {
        boolean updated = amountServices.updateAmounts(amountModel);

        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Amount updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update amount!");
        }
        return "redirect:/amounts";
    }
}
