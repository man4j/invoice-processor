package services.invoiceprocessor;

import org.apache.kafka.clients.admin.NewTopic;
import org.protobeans.core.EntryPoint;
import org.protobeans.kafka.annotation.EnableKafkaMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import services.invoiceprocessor.service.InvoiceConsumer;

@EnableKafkaMessaging(brokerList = "s:brokerList", autoOffsetReset = "s:autoOffsetReset")
@ComponentScan(basePackageClasses = {InvoiceConsumer.class})
public class Main {
    @Bean
    public NewTopic invoicesTopic() {
        return new NewTopic("invoices", 24, (short) 2);
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
