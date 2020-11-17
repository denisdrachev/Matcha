package matcha.controller;

import lombok.RequiredArgsConstructor;
import matcha.user.model.UserEntity;
import matcha.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/users")
    public String confirmRegistration(Model model) {
//        List<Location> locationList = locationService.getAllLocations();
        List<UserEntity> allUsers = userService.getAllUsers();
        if (allUsers != null)
            model.addAttribute("name", allUsers.stream().map(location -> "<p>" + location + "</p>").collect(Collectors.joining()));
        else
            model.addAttribute("name", String.join("", "Filed to load users"));
        return "greeting";
    }
}