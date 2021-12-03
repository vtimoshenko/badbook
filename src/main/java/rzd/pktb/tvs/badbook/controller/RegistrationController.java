package rzd.pktb.tvs.badbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rzd.pktb.tvs.badbook.dao.UserDAO;
import rzd.pktb.tvs.badbook.models.User;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class RegistrationController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userDAO.save(user, bCryptPasswordEncoder.encode(user.getPassword()))){
            bindingResult.addError(new FieldError("user","username", "Пользователь с таким именем уже существует"));
            return "registration";
        }
        return "redirect:/login";
    }
}
