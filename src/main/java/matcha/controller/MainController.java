package matcha.controller;

import matcha.db.EntityActions;
import matcha.properties.Gateways;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
public class MainController {

    List<String> uris = new ArrayList<>();

    @PostConstruct
    private void init() {
        String url = "https://matcha-server.herokuapp.com/";
        String row = "<p th:text=\"${name}\" />";
        for (Gateways el : Gateways.values()) {
            System.err.println(url.concat(el.getUri()));
            uris.add("<p>".concat(url.concat(el.getUri())).concat("</p>"));
        }
    }

    @GetMapping("/")
    public String confirmRegistration(WebRequest request, Model model) {
        System.err.println(uris.size());
        model.addAttribute("name", uris.stream().collect(Collectors.joining("")));
        return "greeting";
    }
}