package rzd.pktb.tvs.badbook.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rzd.pktb.tvs.badbook.models.User;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping(value={"/index", "/"})
    public String index(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            model.addAttribute("currentUser", current);
        }
        return "/index";
    }

}
