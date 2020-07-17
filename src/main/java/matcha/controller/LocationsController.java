package matcha.controller;

import lombok.RequiredArgsConstructor;
import matcha.db.EntityActions;
import matcha.model.Location;
import matcha.properties.Gateways;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LocationsController {

    private final EntityActions entityActions;

    @GetMapping("/locations")
    public String confirmRegistration(WebRequest request, Model model) {
        List<Location> locationList = entityActions.getLocationList();
        if (locationList != null)
            model.addAttribute("name", locationList.stream().map(location -> "<p>" + location + "</p>").collect(Collectors.joining()));
        else
            model.addAttribute("name", String.join("", "Filed to load locations"));
        return "greeting";
    }
}