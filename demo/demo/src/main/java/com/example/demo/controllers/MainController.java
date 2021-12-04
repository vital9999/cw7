package com.example.demo.controllers;


import com.example.demo.model.*;
import com.example.demo.repos.*;
import com.example.demo.save.ProductsSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private StockRepo stockRepo;
    @Autowired
    private ProducrRepo producrRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("user", user);
        if(user.getRoles().equals(Collections.singleton(Role.ADMIN))) {
            return "redirect:/admin";
        }
        List<Product> products = (List<Product>) producrRepo.findAll();
        model.addAttribute("products", products);
        //fill(user);
        return "main";
    }
    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("user", user);
        List<Product> products = (List<Product>) producrRepo.findAll();
        model.addAttribute("products", products);
        return "admin";
    }

    @GetMapping("/main/orders")
    public String orders(@AuthenticationPrincipal User user,Model model) {
        List<Customer> customer = customerRepo.findByUser(user);
        List<Order> orders = customer.get(0).getOrders();
        List<OrderDetails> orderDetails = new ArrayList<>();
        for(int i = 0;i<orders.size();i++){
            orderDetails.addAll(orders.get(i).getOrderDetails());
        }
        List<Product> products = new ArrayList<>();
        for(int i = 0;i<orderDetails.size();i++){
            products.add(orderDetails.get(i).getProduct());
        }
        model.addAttribute("user", user);
        model.addAttribute("customers", customer);
        model.addAttribute("orders", orders);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("products", products);

        return "orders";
    }
    @GetMapping("/main/product/{id}/view")
    public String viewProduct(@AuthenticationPrincipal User user, Model model, @PathVariable("id") int id) {
        if(!producrRepo.existsById(id)){
            return "redirect:/main";
        }
        Product product = producrRepo.findById(id).orElse(new Product());
        model.addAttribute("product", product);
        return "product";
    }
    @PostMapping("/main/product/{id}/addCart")
    public String addCart(@AuthenticationPrincipal User user, Model model, @PathVariable("id") int id,
    HttpServletRequest request, HttpSession session) {
        if(!producrRepo.existsById(id)){
            return "redirect:/main";
        }
        List<Product> products = (List<Product>) session.getAttribute("products");
        if(products == null)  products = new ArrayList<>();



        Product product = producrRepo.findById(id).orElse(new Product());
        products.add(product);
        request.getSession().setAttribute("products", products);

        model.addAttribute("product", product);
        model.addAttribute("user", user);
        return "user";
    }
    @GetMapping("/main/cart")
    public String  cart(@AuthenticationPrincipal User user, Model model, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("products");
        if(cart == null) return "redirect:/main";
        model.addAttribute("cart", cart);
        //ProductsSave count = new ProductsSave();
        //model.addAttribute("count", count);
        return "cart";
    }
    @PostMapping("/main/cart/save")
    public String  cartSave(@AuthenticationPrincipal User user, Model model, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("products");
        OrderDetails orderDetails = new OrderDetails();
        int sum = 0;
        for(int i=0;i<cart.size();i++) {
            orderDetails.setProduct(cart.get(i));
            orderDetails.setQuantity(1);
            orderDetails.setPrice(cart.get(i).getPrice());
            orderDetails.setSum(cart.get(i).getPrice() * orderDetails.getQuantity());

            cart.get(i).setQuantity(cart.get(i).getQuantity()-orderDetails.getQuantity());
            sum += orderDetails2.getSum();

            Order order = new Order();
            order.setDate("14-06-2021");
            order.setOrderDetails(Arrays.asList(orderDetails, orderDetails1,orderDetails2));
            order.setSum(sum);
        }
        return "cart";
    }

    public void fill(User user){
        double sum = 0;
        Product product = new Product();
        product.setProduct_name("divan");
        product.setPurchase_cost(100);
        product.setPrice(120);
        product.setQuantity(10);

        Product product1 = new Product();
        product1.setProduct_name("stol");
        product1.setPurchase_cost(90);
        product1.setPrice(110);
        product1.setQuantity(10);


        Stock stock = new Stock();
        stock.setName("opt");
        stock.setProductList(Arrays.asList(product,product1));

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProduct(product);
        orderDetails.setQuantity(2);
        orderDetails.setPrice(product.getPrice());
        orderDetails.setSum(product.getPrice()*orderDetails.getQuantity());

        product.setQuantity(product.getQuantity()-orderDetails.getQuantity());
        sum += orderDetails.getSum();

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setProduct(product1);
        orderDetails1.setQuantity(1);
        orderDetails1.setPrice(product1.getPrice());
        orderDetails1.setSum(product1.getPrice()*orderDetails1.getQuantity());

        product1.setQuantity(product1.getQuantity()-orderDetails1.getQuantity());
        sum += orderDetails1.getSum();

        OrderDetails orderDetails2 = new OrderDetails();
        orderDetails2.setProduct(product);
        orderDetails2.setQuantity(5);
        orderDetails2.setPrice(product.getPrice());
        orderDetails2.setSum(product.getPrice()*orderDetails2.getQuantity());

        product.setQuantity(product.getQuantity()-orderDetails2.getQuantity());
        sum += orderDetails2.getSum();

        Order order = new Order();
        order.setDate("14-06-2021");
        order.setOrderDetails(Arrays.asList(orderDetails, orderDetails1,orderDetails2));
        order.setSum(sum);


        Customer customer = new Customer();
        customer.setAddress("Lida");
        customer.setPhone_number("298565651");
        customer.setName("vital");
        customer.setUser(user);
        customer.setOrders(Arrays.asList(order));


        producrRepo.saveAll(Arrays.asList(product,product1));
        stockRepo.save(stock);
        orderDetailsRepo.saveAll(Arrays.asList(orderDetails, orderDetails1, orderDetails2));
        orderRepo.save(order);
        customerRepo.save(customer);
    }
}