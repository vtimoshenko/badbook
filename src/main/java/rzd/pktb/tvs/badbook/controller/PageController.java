package rzd.pktb.tvs.badbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rzd.pktb.tvs.badbook.dao.FriendDAO;
import rzd.pktb.tvs.badbook.dao.UserDAO;
import rzd.pktb.tvs.badbook.models.Friend;
import rzd.pktb.tvs.badbook.models.User;
import rzd.pktb.tvs.badbook.models.UserSearch;

import javax.validation.Valid;

@Controller
@RequestMapping("/page")
public class PageController {

    private UserDAO userDAO;
    private FriendDAO friendDAO;

    @Autowired
    public PageController(UserDAO userDAO, FriendDAO friendDAO) {
        this.userDAO = userDAO;
        this.friendDAO = friendDAO;
    }

    @GetMapping
    public String list(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            model.addAttribute("currentUser", current);
        }

        model.addAttribute("users", userDAO.list());
        return "page/list";
    }

    @GetMapping("/search")
    public String search(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            model.addAttribute("currentUser", current);
        }

        model.addAttribute("userSearch", new UserSearch());
        return "page/search";
    }

    @PostMapping("/find")
    public String find(@ModelAttribute("userSearch") @Valid UserSearch userSearch, BindingResult bindingResult, Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            model.addAttribute("currentUser", current);
        }

        if (bindingResult.hasErrors()) {
            return "page/search";
        }

        model.addAttribute("users", userDAO.list(userSearch));
        return "page/list";
    }

    @GetMapping("/findg/{name}.{surname}")
    public String findg(@PathVariable("name") String name, @PathVariable("surname") String surname, Model model) {
        UserSearch userSearch = new UserSearch();
        userSearch.setName(name);
        userSearch.setSurname(surname);
        model.addAttribute("users", userDAO.list(userSearch));
        return "page/listg";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userDAO.get(id));
        model.addAttribute("friends", userDAO.list(friendDAO.get(id)));
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            model.addAttribute("currentUser", current);
            if (current.getId() == id)
                return "page/viewMy";
        }
        return "page/view";
    }

    @GetMapping("/toFriends/{id}")
    public String toFriends(@PathVariable("id") int id, Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            Friend friend = new Friend(current.getId(), id);
            if (!friendDAO.existsFriend(friend))
                friendDAO.save(friend);
            return "redirect:/page/" + current.getId();
        } else
            return "redirect:/page/" + id;
    }

    @GetMapping("/fromFriends/{id}")
    public String fromFriends(@PathVariable("id") int id, Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        if (loggedInUser.getPrincipal().getClass().getName().equals(User.class.getName())) {
            User current = (User)loggedInUser.getPrincipal();
            Friend friend = new Friend(current.getId(), id);
            friendDAO.delete(friend);
            return "redirect:/page/" + current.getId();
        } else
            return "redirect:/page/" + id;
    }

}
