package services.invoiceprocessor;

import org.apache.kafka.clients.admin.NewTopic;
import org.protobeans.kafka.annotation.EnableKafkaMessaging;
import org.protobeans.mvc.MvcEntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import services.invoiceprocessor.controller.MainController;
import services.invoiceprocessor.service.InvoiceConsumer;

@EnableUndertow
@EnableMvc
@EnableKafkaMessaging(brokerList = "s:brokerList", autoOffsetReset = "s:autoOffsetReset")
@ComponentScan(basePackageClasses = {InvoiceConsumer.class, MainController.class})
public class Main {
    @Bean
    public NewTopic invoicesTopic() {
        return new NewTopic("invoices", 24, (short) 2);
    }
    
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}
