package com.jaikeex.registerservice.controller;

import com.jaikeex.registerservice.dto.ModelAttributeDto;
import com.jaikeex.registerservice.dto.UserDto;
import com.jaikeex.registerservice.models.ModelAttribute;
import com.jaikeex.registerservice.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {

    RegisterService service;

    @Autowired
    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping(value = "/new-user", consumes = "application/json", produces = "application/json")
    public List<ModelAttribute> registerUser(@RequestBody UserDto userDto) {
        return service.registerUser(userDto);
    }
}
