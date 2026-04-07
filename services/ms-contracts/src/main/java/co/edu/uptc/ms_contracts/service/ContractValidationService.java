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
                    "El presupuesto debe ser mayor o igual a 0");
        }

        if (req.getStartDate() == null || req.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "Las fechas de inicio y fin son obligatorias");
        }

        if (!req.getStartDate().isBefore(req.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "La fecha de inicio debe ser anterior a la fecha de fin");
        }

        if (req.getSubject() == null || req.getSubject().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "El objeto del contrato es obligatorio");
        }

        String[] palabras = req.getSubject().trim().split("\\s+");
        if (palabras.length > 200) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "El objeto del contrato no puede superar las 200 palabras (actual: " + palabras.length + ")");
        }
    }
}