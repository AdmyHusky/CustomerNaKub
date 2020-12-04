package com.digitalacademy.customer.controller;

import com.digitalacademy.customer.customer.CustomerSupportTest;
import com.digitalacademy.customer.model.Customer;
import com.digitalacademy.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
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

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @DisplayName("Test get all customer should return list of customer")
    @Test
    void testGetCustomerList() throws Exception{
        when(customerService.getCustomerList())
                .thenReturn(CustomerSupportTest.getListCustomer());

        MvcResult mvcResult = mvc.perform(get("/customer/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONArray resp = new JSONArray(mvcResult.getResponse().getContentAsString());

        //data
        assertEquals(1,resp.getJSONObject(0).get("id"));
        assertEquals("Ryan",resp.getJSONObject(0).get("firstName"));
        assertEquals("Giggs",resp.getJSONObject(0).get("lastName"));
        assertEquals("gique@gique.com",resp.getJSONObject(0).get("email"));
        assertEquals("66818884484",resp.getJSONObject(0).get("phoneNo"));
        assertEquals(32,resp.getJSONObject(0).get("age"));

        assertEquals(2,resp.getJSONObject(1).get("id"));
        assertEquals("David",resp.getJSONObject(1).get("firstName"));
        assertEquals("Beckham",resp.getJSONObject(1).get("lastName"));
        assertEquals("david@david.com",resp.getJSONObject(1).get("email"));
        assertEquals("66818884999",resp.getJSONObject(1).get("phoneNo"));
        assertEquals(45,resp.getJSONObject(1).get("age"));
        
        verify(customerService, times(1)).getCustomerList();
    }

    @DisplayName("Test get customer by id equals 1 should return customer information")
    @Test
    void testGetCustomerByIdEquals1() throws Exception{
        Long reqParam = 1L;

//        Customer customer = new Customer();
//        customer.setId(1L);
//        customer.setFirst_name("Momo");
//        customer.setLast_name("Ayami");
//        customer.setEmail("test@hotmail.com");
//        customer.setPhoneNo("0233333333");
//        customer.setAge(22);

//        when(customerService.getCustomerById(reqParam)).thenReturn(customer);
        when(customerService.getCustomerById(reqParam)).thenReturn(CustomerSupportTest.getListCustomer().get(0));

        MvcResult mvcResult = mvc.perform(get("/customer/" + reqParam))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject resp = new JSONObject(mvcResult.getResponse().getContentAsString());

        //data
        assertEquals(1,resp.get("id"));
        assertEquals("Ryan",resp.get("firstName"));
        assertEquals("Giggs",resp.get("lastName"));
        assertEquals("gique@gique.com",resp.get("email"));
        assertEquals("66818884484",resp.get("phoneNo"));
        assertEquals(32,resp.get("age"));

    }

    @DisplayName("Test get customer by id should return not found")
    @Test
    void testGetCustomerByIdNotFound() throws Exception{
        Long reqParam = 5L;

        mvc.perform(get("/customer/" + reqParam))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DisplayName("Test create customer should return success")
    @Test
    void testCreateCustomer() throws Exception{
        Customer customerReq = CustomerSupportTest.getCreateCustomer();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.createCustomer(customerReq)).thenReturn(CustomerSupportTest.getCreatedCustomer());

        MvcResult mvcResult = mvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isCreated())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(6,jsonObject.get("id"));
        assertEquals("New",jsonObject.get("firstName"));
        assertEquals("NewNew",jsonObject.get("lastName"));
        assertEquals("new@new.com",jsonObject.get("email"));
        assertEquals("66818884477",jsonObject.get("phoneNo"));
        assertEquals(10,jsonObject.get("age"));

    }

    @DisplayName("Test create customer with first name is empty")
    @Test
    void testCreateCustomerWithNameEmpty() throws Exception{
        Customer cs = CustomerSupportTest.getCreateCustomer();
        cs.setFirst_name("");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(cs);

        when(customerService.createCustomer(cs))
                .thenReturn(CustomerSupportTest.getCreatedCustomer());

        MvcResult mvcResult = mvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Validation failed for argument [0] in public org.springframework.http.ResponseEntity<?> " +
                        "com.digitalacademy.customer.controller.CustomerController.createCustomer(com.digitalacademy.customer.model.Customer): " +
                        "[Field error in object 'customer' on field 'first_name': rejected value []; codes " +
                        "[Size.customer.first_name,Size.first_name,Size.java.lang.String,Size]; arguments " +
                        "[org.springframework.context.support.DefaultMessageSourceResolvable: codes [customer.first_name,first_name]; " +
                        "arguments []; default message [first_name],100,1]; default message [Please enter your first name between 1-100 ]] ",
                mvcResult.getResolvedException().getMessage());
    }

    @DisplayName("Test update customer should return success")
    @Test
    void testUpdateCustomer() throws Exception{
        Customer customerReq = CustomerSupportTest.getBeforeUpdateCustomer();
        Long reqId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.updateCustomer(reqId, customerReq))
                .thenReturn(CustomerSupportTest.getBeforeUpdateCustomer());

        MvcResult mvcResult = mvc.perform(put("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(3,jsonObject.get("id"));
        assertEquals("Old",jsonObject.get("firstName"));
        assertEquals("OldOld",jsonObject.get("lastName"));
        assertEquals("old@old.com",jsonObject.get("email"));
        assertEquals("66818884477",jsonObject.get("phoneNo"));
        assertEquals(50,jsonObject.get("age"));

    }

    @DisplayName("Test update customer should return id not found")
    @Test
    void testUpdateCustomerIdNotFound() throws Exception{
        Customer customerReq = CustomerSupportTest.getBeforeUpdateCustomer();
        Long reqId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerReq);

        when(customerService.updateCustomer(reqId, customerReq))
                .thenReturn(null);

        mvc.perform(put("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isNotFound())
                .andReturn();

        verify(customerService, times(1)).updateCustomer(reqId, customerReq);
    }

    @DisplayName("Test delete customer should success")
    @Test
    void testDeleteCustomer() throws Exception{
        Long reqId = 10L;
        when(customerService.deleteById(reqId)).thenReturn(true);

        mvc.perform(delete("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();

        verify(customerService, times(1)).deleteById(reqId);
    }

    @DisplayName("Test delete customer should not found")
    @Test
    void testDeleteCustomerShouldReturnNotFound() throws Exception{
        Long reqId = 10L;
        when(customerService.deleteById(reqId)).thenReturn(false);

        mvc.perform(delete("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn();

        verify(customerService, times(1)).deleteById(reqId);
    }

}
