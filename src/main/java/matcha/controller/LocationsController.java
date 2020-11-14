package matcha.controller;

import lombok.RequiredArgsConstructor;
import matcha.location.model.Location;
import matcha.location.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LocationsController {

    private final LocationService locationService;

    @GetMapping("/locations")
    public String confirmRegistration(Model model) {
        List<Location> locationList = locationService.getAllLocations();
        if (locationList != null)
            model.addAttribute("name", locationList.stream().map(location -> "<p>" + location + "</p>").collect(Collectors.joining()));
        else
            model.addAttribute("name", String.join("", "Filed to load locations"));
        return "greeting";
    }
}