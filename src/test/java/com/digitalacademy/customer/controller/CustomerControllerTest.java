package com.digitalacademy.customer.controller;

import com.digitalacademy.customer.model.Customer;
import com.digitalacademy.customer.service.CustomerService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    private MockMvc mvc;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerService);
        mvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @DisplayName("Test get customer info by id equals 1 should return customer information")
    @Test
    void testGetCustomerInfoByIdEquals1() throws Exception{
        Long reqParam = 1L;

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirst_name("Momo");
        customer.setLast_name("Ayami");
        customer.setEmail("test@hotmail.com");
        customer.setPhoneNo("0233333333");
        customer.setAge(22);

        when(customerService.getCustomerById(reqParam)).thenReturn(customer);

        MvcResult mvcResult = mvc.perform(get("/customer/" + reqParam)).andReturn();

        JSONObject resp = new JSONObject(mvcResult.getResponse().getContentAsString());

        //data
        assertEquals(1,resp.get("id"));
        assertEquals("Momo",resp.get("firstName"));
        assertEquals("Ayami",resp.get("lastName"));
        assertEquals("test@hotmail.com",resp.get("email"));
        assertEquals("0233333333",resp.get("phoneNo"));
        assertEquals(22,resp.get("age"));

    }

}
