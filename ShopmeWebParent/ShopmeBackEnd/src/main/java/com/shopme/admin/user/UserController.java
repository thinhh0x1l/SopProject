package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ControllerAdvice
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listAll(Model model) {
        model.addAttribute("listUsers",userService.listAll());
        return "users";
    }
    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        model.addAttribute("listRoles",userService.listRoles());
        model.addAttribute("message","Create New User");
        return "user_form";
    }
    @PostMapping("/users/save")
    public String save(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            User u = userService.get(id);
            model.addAttribute("user",u);
            model.addAttribute("listRoles",userService.listRoles());
            model.addAttribute("i",true);
            model.addAttribute("message","Edit User [ID: " + u.getId() + "]");
            return "user_form";
        }catch(UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }
    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "The user ID "+id+" was previously deleted");
        if(userService.delete(id))
            redirectAttributes.addFlashAttribute("message", "The user ID "+id+" has been deleted successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/{id}/enabled/{enabled}")
    public String enabled(@PathVariable("id") Integer id,
                          @PathVariable("enabled") boolean enabled,
                          RedirectAttributes redirectAttributes) {
        userService.changeEnabled(id, enabled);
        redirectAttributes.addFlashAttribute("message",
                "The user ID "+id+" has been "+(enabled?"Enabled":"Disabled"));

        return "redirect:/users";
    }

}
