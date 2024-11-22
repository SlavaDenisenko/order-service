package com.denisenko.client;

import com.denisenko.dto.BillingRequest;
import com.denisenko.dto.BillingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "billing-service", url = "${order-service.billing-service-url}")
public interface BillingClient {

    @PostMapping("/accounts/{userId}/debit")
    BillingResponse debit(@PathVariable Integer userId, @RequestBody BillingRequest billingRequest);
}
