package org.protobeans.docker.example.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import services.invoiceprocessor.Main;
import services.invoiceprocessor.model.Invoice;
import services.invoiceprocessor.service.InvoiceConsumer;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={Main.class})
public class InvoiceServiceTest {
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private InvoiceConsumer invoiceConsumer;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void shouldWork() throws JsonProcessingException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        invoiceConsumer.addListener(i -> latch.countDown());
        
        Invoice invoice = new Invoice("seller1", "customer1");
        
        kafkaTemplate.send("invoices", invoice.getSeller(), mapper.writeValueAsString(invoice));
        
        latch.await(5, TimeUnit.SECONDS);
    }
}
