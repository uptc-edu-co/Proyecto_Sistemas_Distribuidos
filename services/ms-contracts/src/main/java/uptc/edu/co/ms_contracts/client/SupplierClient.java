package uptc.edu.co.ms_contracts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uptc.edu.co.ms_contracts.client.dto.SupplierClientResponse;
import uptc.edu.co.ms_contracts.config.SupplierFeignConfig;

import java.util.UUID;

@FeignClient(name = "ms-suppliers", configuration = SupplierFeignConfig.class)
public interface SupplierClient {

    @GetMapping("/suppliers/{id}")
    SupplierClientResponse getSupplierById(@PathVariable("id") UUID id);
}