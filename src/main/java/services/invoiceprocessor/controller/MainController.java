package services.invoiceprocessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import services.invoiceprocessor.service.InvoiceCounter;

@RestController
@RequestMapping("/")
public class MainController {
    @Autowired
    private InvoiceCounter invoiceCounter;
    
    @GetMapping
    String status()  {
        return "ok";
    }
    
    @GetMapping("/count")
    long count()  {
        return invoiceCounter.get();
    }
}
