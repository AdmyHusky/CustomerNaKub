package com.digitalacademy.customer.controller;

import com.digitalacademy.customer.model.Customer;
import com.digitalacademy.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(path ="/customer")
public class CustomerController {

    private CustomerService customService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customService = customerService;
    }

    @GetMapping("/list")
    public List<Customer> customerList(){
        //public ArrayList<Map> customerList(){
//        ArrayList<Map> customers = new ArrayList<>();
//        Map<String, String> customer = new HashMap<>();
//        customer.put("1", "Gique");
//        customer.put("2", "Netto");
//        customer.put("3", "First");
//        customers.add(customer);
//
//        return customers;
        // updete 2 //
//        List<Customer> css = new ArrayList<>();
//
//        Customer cs = new Customer();
//        cs.setId(1L);
//        cs.setFirstName("Kanetto");
//        cs.setLastName("Kampiranon");
//        cs.setEmail("test@hotmail.com");
//        cs.setPhoneNo("5555555555");
//        cs.setAge(22);
//        css.add(cs);
//
//        cs = new Customer();
//        cs.setId(2L);
//        cs.setFirstName("Momo");
//        cs.setLastName("Ayami");
//        cs.setEmail("Momo@hotmail.com");
//        cs.setPhoneNo("5000555555");
//        cs.setAge(22);
//        css.add(cs);
//
//        return css;
        // update 2 //
        return customService.getCustomerList();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer body){
        Customer customer = customService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,
                                            @Valid @RequestBody Customer body){
        body.setId(id);
        Customer customer = customService.updateCustomer(id, body);
        return customer != null ? ResponseEntity.ok(customer)
                :ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer (@PathVariable Long id){
        return customService.deleteById(id)? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public Optional<Customer> findByIdCustomer(@PathVariable Long id){
        return customService.getCustomerById(id);
    }

}
