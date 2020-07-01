package com.bohhom.auth.controller;

import com.bohhom.auth.controller.entity.CustomerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

/**
 * @author cruz
 * @date 2020-07-01
 */
@RestController
public class TestController {

    @Autowired
    private TokenStore tokenStore;

    @PreAuthorize("hasRole('admin') or #oauth2.clientHasRole('ROLE_APP')")
    @GetMapping("/admin/hello/{id}")
    public String admin(Principal principal, @PathVariable("id") int id) {
        System.out.println("username:" + principal.getName());
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("app", principal.getName());
        tokens.forEach(o -> tokenStore.removeAccessToken(o));
        return "success: " + id;
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
