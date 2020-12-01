package com.digitalacademy.customer.service;

import com.digitalacademy.customer.model.Customer;
import com.digitalacademy.customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomerList(){
        return customerRepository.findAll();
    }

    public Customer createCustomer(Customer customer) {
        return  customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerReq) {
        Customer cs = customerRepository.findAllById(id);

        if(cs != null){
            return  customerRepository.save(customerReq);
        }else {
            return null;
        }
    }

    public boolean deleteById(Long id) {
        try {
            customerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e){
            System.out.println("Cannot delete by id: " + id);
            return  false;
        }
    }

    public Optional<Customer> getCustomerById(Long id){
        return customerRepository.findById(id);
    }
}
