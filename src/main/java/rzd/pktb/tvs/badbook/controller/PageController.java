package rzd.pktb.tvs.badbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rzd.pktb.tvs.badbook.dao.FriendDAO;
import rzd.pktb.tvs.badbook.dao.UserDAO;
import rzd.pktb.tvs.badbook.models.Friend;
import rzd.pktb.tvs.badbook.models.User;

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
