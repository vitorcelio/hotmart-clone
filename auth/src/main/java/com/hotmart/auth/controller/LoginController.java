package com.hotmart.auth.controller;

import com.hotmart.auth.dto.request.ChangePasswordRequestDTO;
import com.hotmart.auth.dto.request.RecoveryPasswordRequestDTO;
import com.hotmart.auth.dto.request.UserRequestDTO;
import com.hotmart.auth.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @GetMapping
    public String login(Model model) {
        model.addAttribute("page", "login");
        return "login";
    }

    @GetMapping("/signup")
    public String signup(UserRequestDTO request, Model model) {
        model.addAttribute("subtitle", "Vamos te ajudar desde os primeiros passos. {BREAK}Cadastre-se grátis.");
        model.addAttribute("page", "signup");
        return "signup";
    }

    @PostMapping("/signup")
    public String createAccount(@Validated @ModelAttribute final UserRequestDTO request, BindingResult result, Model model) {
        model.addAttribute("subtitle", "Vamos te ajudar desde os primeiros passos. {BREAK}Cadastre-se grátis.");
        model.addAttribute("page", "signup");

        if (result.hasErrors()) {
            return "signup";
        }

        try {
            userService.save(request);
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/recovery")
    public String recovery(RecoveryPasswordRequestDTO request, Model model) {
        model.addAttribute("page", "recovery");
        return "recovery";
    }

    @PostMapping("/recovery")
    public String recoveryPassword(@Validated @ModelAttribute RecoveryPasswordRequestDTO requestRecovery,
                                   BindingResult result, Model model) {
        model.addAttribute("page", "recovery");

        if (result.hasErrors()) {
            return "recovery";
        }

        try {
            userService.recovery(requestRecovery);
            return "verify";
        } catch (Exception e) {
            return "recovery";
        }
    }

    @GetMapping("/changePassword")
    public String changePassword(ChangePasswordRequestDTO request,
                                 @RequestParam("pswdrst") String pswdrst,
                                 @RequestParam("uuid") String uuid,
                                 Model model) {

        userService.validationTokenUser(pswdrst, uuid);

        model.addAttribute("page", "changePassword");
        model.addAttribute("pswdrst", pswdrst);
        model.addAttribute("uuid", uuid);
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Validated @ModelAttribute ChangePasswordRequestDTO request,
                                 @RequestParam("pswdrst") String pswdrst,
                                 @RequestParam("uuid") String uuid,
                                 BindingResult result, Model model) {

        model.addAttribute("page", "changePassword");

        if (result.hasErrors()) {
            return "changePassword";
        }

        try {
            userService.updatePassword(request, pswdrst, uuid);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "changePassword";
        }
    }

}
