package uptc.edu.co.ms_suppliers.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import uptc.edu.co.ms_suppliers.model.Supplier;
import uptc.edu.co.ms_suppliers.service.SupplierService;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * Crea un nuevo proveedor.
     *
     * @param supplier los datos del nuevo proveedor
     * @return el proveedor creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    /**
     * Obtiene un proveedor por su UUID.
     *
     * @param id el UUID del proveedor
     * @return el proveedor encontrado
     */
    @GetMapping("/{id}")
    public Supplier getSupplier(@PathVariable UUID id) {
        return supplierService.getSupplier(id);
    }

    /**
     * Actualiza un proveedor existente.
     * No permite cambiar el NIT.
     *
     * @param id el UUID del proveedor a actualizar
     * @param newData los nuevos datos del proveedor
     * @return el proveedor actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update:supplier')")
    @ResponseStatus(HttpStatus.OK)
    public Supplier updateSupplier(@PathVariable UUID id, @Valid @RequestBody Supplier newData) {
        return supplierService.updateSupplier(id, newData);
    }

    /**
     * Manejador global para excepciones de tipo IllegalArgumentException.
     * Retorna 400 Bad Request cuando no se permite modificar el NIT.
     *
     * @param e la excepción capturada
     * @return respuesta con estado 400 y mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        errorResponse.put("status", "BAD_REQUEST");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
