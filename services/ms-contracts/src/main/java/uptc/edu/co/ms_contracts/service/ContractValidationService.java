package uptc.edu.co.ms_contracts.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uptc.edu.co.ms_contracts.dto.CreateContractRequest;

import java.math.BigDecimal;

@Service
public class ContractValidationService {

    public void validate(CreateContractRequest req) {
        if (req.getPresupuesto() == null || req.getPresupuesto().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "El presupuesto debe ser mayor o igual a 0");
        }

        if (req.getFechaInicio() == null || req.getFechaFin() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "Las fechas de inicio y fin son obligatorias");
        }

        if (!req.getFechaInicio().isBefore(req.getFechaFin())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "La fecha de inicio debe ser anterior a la fecha de fin");
        }

        if (req.getObjeto() == null || req.getObjeto().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "El objeto del contrato es obligatorio");
        }

        String[] palabras = req.getObjeto().trim().split("\\s+");
        if (palabras.length > 200) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "El objeto del contrato no puede superar las 200 palabras (actual: " + palabras.length + ")");
        }
    }
}