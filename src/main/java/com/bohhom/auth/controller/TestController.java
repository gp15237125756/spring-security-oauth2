package com.bohhom.auth.controller;

import com.bohhom.auth.controller.entity.CustomerData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医院配置Controller
 * 
 * @author gaoping
 * @date 2020-06-01
 */
@RestController
public class TestController
{

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/customer/{userId}")
    public CustomerData getCustomerProfile(@PathVariable("userId") String userId) {
        return getCustomer(userId);
    }


    private CustomerData getCustomer(final String userId) {
        CustomerData customer = new CustomerData();
        customer.setEmail("contact-us@javadevjournal.com");
        customer.setFirstName("Demo");
        customer.setLastName("User");
        customer.setAge(21);
        customer.setId(userId);
        return customer;
    }


}
