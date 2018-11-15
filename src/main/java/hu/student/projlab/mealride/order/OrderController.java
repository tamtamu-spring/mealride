package hu.student.projlab.mealride.order;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/order")
    public ModelAndView OrderForm(@RequestParam(value="restId") Long restId, ModelAndView modelAndView, final BindingResult result) {

        modelAndView = new ModelAndView(new RedirectView("/order"));

        if (result.hasErrors()) {
            modelAndView.addObject("error", "There are some error!");
            return modelAndView;
        }

        modelAndView.addObject("rest", orderService.getRestaurant(restId));
        modelAndView.addObject("user", orderService.getUser());
        modelAndView.addObject("cartitems", orderService.getCartItems());
        modelAndView.addObject("fullprice", orderService.getFullPrice());
        modelAndView.addObject("addresses", orderService.getAddresses());
        modelAndView.addObject("cards", orderService.getCards());
        modelAndView.addObject("comment", new String());
        modelAndView.setViewName("user/order");
        return modelAndView;

    }

    @PostMapping("/order/finalize")
    public ModelAndView finalizeOrder(@RequestParam(value="restId") Long restId,
                                      @RequestParam(value="selectedAddress") Long addressId,
                                      @RequestParam(value="selectedCard") Long cardNumber,
                                      @RequestParam(value="orderCustomerComment") String comment,
                                      ModelAndView modelAndView, final BindingResult result) {

       modelAndView = new ModelAndView(new RedirectView("/"));


       //TODO throw a lot of exceptions inside this method and then handle those here
       orderService.createNewOrder(restId,addressId,cardNumber,comment);

        if (result.hasErrors()) {
            modelAndView.addObject("error", "There are some error!");
            return modelAndView;
        }

        /*modelAndView.addObject("rest", orderService.getRestaurant(restId));
        modelAndView.addObject("user", orderService.getUser());
        modelAndView.addObject("cartitems", orderService.getCartItems());
        modelAndView.addObject("fullprice", orderService.getFullPrice());
        modelAndView.addObject("addresses", orderService.getAddresses());
        modelAndView.addObject("cards", orderService.getCards());
        modelAndView.setViewName("user/order");*/
        return modelAndView;

    }





}
