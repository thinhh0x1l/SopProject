package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
@ControllerAdvice
public class UserController {
    private  String field="id";
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return listByPage(1,model,"id","asc");
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

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, Model model,
                             @RequestParam("sortField") String sortField,
                             @RequestParam( "sortDir") String sortDir) {
        if(!field.equals(sortField)) {
            field=sortField;
            sortDir="asc";
        }
        String reSortDir = sortDir.equals("asc") ? "desc" : "asc";
        Page<User> users = userService.listByPage(pageNum,sortField,sortDir);
        long startCount =  (pageNum - 1L) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE -1;
        if(endCount > users.getTotalElements())
            endCount = users.getTotalElements();
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",users.getTotalElements());
        model.addAttribute("totalPages",users.getTotalPages());
        model.addAttribute("listUsers",users.getContent());
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reSortDir",reSortDir);

        return "users";
    }
    @PostMapping("/users/save")
    public String save(@ModelAttribute("user") User user,
                       RedirectAttributes redirectAttributes,
                       @RequestParam("image")MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhotos(fileName);
        String uploadDir = "user-photos/" + userService.save(user).getId();
        if(!multipartFile.isEmpty())
            FileUploadUtil.cleanDir(uploadDir);
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model,
                       RedirectAttributes redirectAttributes) {
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
