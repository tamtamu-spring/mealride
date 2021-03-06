package hu.student.projlab.mealride.user;


import hu.student.projlab.mealride.cart.ShoppingCart;
import hu.student.projlab.mealride.deliveryaddress.DeliveryAddress;
import hu.student.projlab.mealride.deliveryaddress.DeliveryAddressService;
import hu.student.projlab.mealride.exception.PasswordNotMatchingException;
import hu.student.projlab.mealride.order.Order;
import hu.student.projlab.mealride.order.OrderService;
import hu.student.projlab.mealride.restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
class UserController {

    private UserService userService;

    private DeliveryAddressService deliveryAddressService;

    private OrderService orderService;

    @Autowired
    public UserController(UserService userService, DeliveryAddressService deliveryAddressService, OrderService orderService) {
        this.userService = userService;
        this.deliveryAddressService = deliveryAddressService;
        this.orderService = orderService;
    }




    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("addresses", deliveryAddressService.getAddresses());
        return "users";
    }

    @PostMapping("/registration")
    public ModelAndView processRegistrationForm(@ModelAttribute(value="user") User user,@ModelAttribute(value="address")DeliveryAddress address,
                                                BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("alreadyRegisteredMessage", "Some error occured during registration prcoess!");
            modelAndView.setViewName("registration");
            return modelAndView;
        }

        if(user.getEmail() == null || user.getEmail().equals("")) {
            modelAndView.addObject("alreadyRegisteredMessage", "You must provide an email address!");
            modelAndView.setViewName("registration");
            return modelAndView;
        }

        User userExists = userService.findUserByEmail(user.getEmail());

        if(userExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  " +
                    "There is already a user registered with the email provided.");
                        bindingResult.reject("email");
        } else {

            userService.addUser(user);

            deliveryAddressService.registerUserWithAddress(address,user);

            modelAndView.addObject("successMessage", "You are registered successfully!");
        }

        modelAndView.setViewName("registration");
        return modelAndView;
    }

    // We will know which user is logged in, then we can autocomplete the form
    @GetMapping("/personaldata")
    public String getUserData(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "user/personaldata";
    }

    // We will know which user is logged in so we can call an updateUser method based on UserId
    @PostMapping("/personaldata")
    public ModelAndView editUserData(@ModelAttribute(value="user") User user,ModelAndView modelAndView) {
        userService.editUser(user);
        modelAndView.setViewName("/user/personaldata");
        return modelAndView;
    }

    @GetMapping("/password")
    public String changePasswordForm(Model model) {
        model.addAttribute("changer", new PasswordChanger());
        return "user/password";
    }

    @PostMapping("/password")
    public ModelAndView changePassword(ModelAndView modelAndView, @ModelAttribute(value="changer") PasswordChanger changer ) {

        try {
            userService.changePassword(changer);
            modelAndView.addObject("onPasswordChangeSuccess","Password is changed!");
        }
        catch(PasswordNotMatchingException e) {
            modelAndView.addObject("onPasswordChangeFailure",e.getMessage());
        }

        modelAndView.setViewName("user/password");
        return modelAndView;
    }

    @GetMapping("/previous-orders")
    public String getPreviousOrders(Model model) {
        model.addAttribute("orders", userService.getCurrentUser().getOrders());
        return "user/previous-orders";
    }

}
