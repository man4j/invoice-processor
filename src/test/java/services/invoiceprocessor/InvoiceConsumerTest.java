package services.invoiceprocessor;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import services.invoiceprocessor.model.Invoice;
import services.invoiceprocessor.service.InvoiceConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={Main.class})
public class InvoiceConsumerTest {    
    @Autowired
    private InvoiceConsumer invoiceConsumer;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void shouldWork() throws JsonProcessingException, InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        
        AtomicBoolean status = new AtomicBoolean();
        
        invoiceConsumer.addListener(i -> {status.set(true); latch.countDown();});
            
        Invoice invoice = new Invoice(UUID.randomUUID().toString().replace("-", ""), UUID.randomUUID().toString().replace("-", ""));
        
        System.out.println("Send invoice from:" + invoice.getSeller());

        kafkaTemplate.send("invoices", invoice.getSeller(), new ObjectMapper().writeValueAsString(invoice)).get();
        
        latch.await(15, TimeUnit.SECONDS);
        
        Assert.assertTrue(status.get());
    }
}
