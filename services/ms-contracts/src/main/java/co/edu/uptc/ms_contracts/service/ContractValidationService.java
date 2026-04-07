package co.edu.uptc.ms_contracts.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import co.edu.uptc.ms_contracts.dto.CreateContractRequest;

import java.math.BigDecimal;

@Service
public class ContractValidationService {

    public void validate(CreateContractRequest req) {
        if (req.getBudget() == null || req.getBudget().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "The budget must be greater than or equal to 0");
        }

        if (req.getStartDate() == null || req.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "The start and end dates are required");
        }

        if (!req.getStartDate().isBefore(req.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "The start date must be before the end date");
        }

        if (req.getSubject() == null || req.getSubject().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "The contract subject is required");
        }

        String[] words = req.getSubject().trim().split("\\s+");
        if (words.length > 200) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "The contract subject cannot exceed 200 words (current: " + words.length + ")");
        }
    }
}