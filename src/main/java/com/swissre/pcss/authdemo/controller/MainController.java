package com.swissre.pcss.authdemo.controller;

import com.swissre.pcss.authdemo.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class MainController {

    @Autowired
    ApplicationProperties appProps;

    @GetMapping("/")
    public String hello(Principal principal, Model model) {
        addUserToModel(principal, model);
        return "index";
    }

    @GetMapping("/protected")
    public String protectedArea(Principal principal, Model model) {
        log.info("Accessing protected stuff");
        addUserToModel(principal, model);
        return "protected";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

    // simple "ping" endpoint for CloudFoundry http health check
    @GetMapping(value = "/cfhealth")
    @ResponseBody
    public String cfhealth() {
        return "ok";
    }

    @GetMapping("/me")
    @ResponseBody
    public String me(Principal principal) {
        if (principal != null) {
            return principal.getName();
        } else {
            return "anonymous";
        }
    }

    @GetMapping(value = "/myroles")
    @ResponseBody
    public Collection<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream().map(
                    authority -> authority.getAuthority()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/headers")
    @ResponseBody
    public String headers(@RequestHeader HttpHeaders headers) {
        Map<String, String> headerMap = headers.toSingleValueMap();
        StringBuffer b = new StringBuffer();
        headerMap.keySet().stream().forEach(key -> b.append(key + " = " + headerMap.get(key) + "; " + "<br>"));
        return b.toString();
    }

    private void addUserToModel(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("user", principal.getName());
        } else {
            model.addAttribute("user", "anonymous");
        }
        model.addAttribute("firstName", "first name to be mapped");
        model.addAttribute("lastName", "last name to be mapped");
        model.addAttribute("email", "to be mapped");
        model.addAttribute("principal", principal);
    }

}