package services.invoiceprocessor.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class InvoiceCounter {
    private AtomicLong counter = new AtomicLong();
    
    public void increment() {
        counter.incrementAndGet();
    }
    
    public long get() {
        return counter.get();
    }
}
