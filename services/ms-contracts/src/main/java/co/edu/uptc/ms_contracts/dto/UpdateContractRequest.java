package co.edu.uptc.ms_contracts.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContractRequest {
    
    private String status;
    private BigDecimal budget;

    
    
}
