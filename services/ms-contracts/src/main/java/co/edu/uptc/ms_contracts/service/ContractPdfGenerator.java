package co.edu.uptc.ms_contracts.service;

import co.edu.uptc.ms_contracts.client.dto.SupplierClientResponse;
import co.edu.uptc.ms_contracts.model.Contract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class ContractPdfGenerator {

    private static final float MARGIN_X = 50f;
    private static final float START_Y = 750f;
    private static final float LINE_HEIGHT = 16f;
    private static final float PAGE_WIDTH = 595f; // A4 aprox
    private static final float RIGHT_MARGIN = 50f;
    private static final float MAX_WIDTH = PAGE_WIDTH - MARGIN_X - RIGHT_MARGIN;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale MONEY_LOCALE = new Locale("es", "CO");

    public byte[] generate(Contract contract, SupplierClientResponse supplier) throws IOException {
            try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                    float y = START_Y;
                    PDFont font = PDType1Font.HELVETICA;
                    PDFont fontBold = PDType1Font.HELVETICA_BOLD;

                    y = writeLine(content, fontBold, 16, y, "CONTRATO");
                    y = writeLine(content, font, 12, y - 8, "");

                    y = writeLine(content, fontBold, 12, y, "Información del contrato");
                    y = writeLine(content, font, 12, y, "ID del contrato: " + contract.getId());
                    y = writeLine(content, font, 12, y, "Número de contrato: " + contract.getContractNumber());
                    y = writeLine(content, font, 12, y, "Fecha de creación: " + formatDateTime(contract.getCreatedAt()));

                    y = writeLine(content, font, 12, y - 4, "");
                    y = writeLine(content, fontBold, 12, y, "Información del proveedor(a)");
                    if (supplier != null) {
                        y = writeLine(content, font, 12, y, "ID del proveedor(a): " + supplier.getId());
                        y = writeLine(content, font, 12, y, "Nombre: " + supplier.getName());
                        y = writeLine(content, font, 12, y, "NIT: " + supplier.getNit());
                        y = writeLine(content, font, 12, y, "Correo: " + supplier.getEmail());
                        y = writeLine(content, font, 12, y, "Teléfono: " + supplier.getPhone());
                    } else {
                        y = writeLine(content, font, 12, y, "Información del proveedor(a) no disponible");
                    }

                    y = writeLine(content, font, 12, y - 4, "");
                    y = writeLine(content, fontBold, 12, y, "Términos");
                    y = writeLine(content, font, 12, y, "Objeto: " + contract.getSubject());
                    y = writeLine(content, font, 12, y, "Fecha de inicio: " + formatDate(contract.getStartDate()));
                    y = writeLine(content, font, 12, y, "Fecha de fin: " + formatDate(contract.getEndDate()));
                    y = writeLine(content, font, 12, y, "Presupuesto: " + formatBudget(contract.getBudget()));
                    writeLine(content, font, 12, y, "Estado: " + contract.getStatus());
                }

                document.save(output);
                return output.toByteArray();
            }
        }

        private float writeLine(PDPageContentStream content,
                            PDFont font,
                            int size,
                            float y,
                            String text) throws IOException {

        if (text == null) {
            text = "";
        }

        // Soporta saltos de línea manuales
        String[] paragraphs = text.split("\\R");

        for (String paragraph : paragraphs) {

            StringBuilder line = new StringBuilder();

            for (String word : paragraph.split(" ")) {

                String testLine = line.isEmpty()
                        ? word
                        : line + " " + word;

                float textWidth = font.getStringWidth(testLine) / 1000 * size;

                // Si supera el ancho permitido, escribir línea actual
                if (textWidth > MAX_WIDTH) {

                    content.beginText();
                    content.setFont(font, size);
                    content.newLineAtOffset(MARGIN_X, y);
                    content.showText(line.toString());
                    content.endText();

                    y -= LINE_HEIGHT;

                    line = new StringBuilder(word);

                } else {
                    line = new StringBuilder(testLine);
                }
            }

            // Escribir última línea del párrafo
            if (!line.isEmpty()) {
                content.beginText();
                content.setFont(font, size);
                content.newLineAtOffset(MARGIN_X, y);
                content.showText(line.toString());
                content.endText();

                y -= LINE_HEIGHT;
            }
        }

        return y;
    }

    private String formatDate(java.time.LocalDate date) {
        return date == null ? "" : DATE_FORMAT.format(date);
    }

    private String formatDateTime(java.time.LocalDateTime date) {
        return date == null ? "" : DATE_TIME_FORMAT.format(date);
    }

    private String formatBudget(java.math.BigDecimal budget) {
        if (budget == null) {
            return "";
        }
        NumberFormat format = NumberFormat.getNumberInstance(MONEY_LOCALE);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return "COP " + format.format(budget);
    }
}
