package com.yash.eventsphere.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yash.eventsphere.entity.QRCode;
import com.yash.eventsphere.entity.Ticket;
import com.yash.eventsphere.repository.QRCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QRCodeService {
    private static final int QR_CODE_SIZE = 250;
    private static final String IMAGE_FORMAT = "PNG";
    private final QRCodeRepository qrCodeRepository;
    public QRCode generateForTicket(Ticket ticket) {
        String encodedValue = generateUniqueEncodedValue();
        String imageData = generateQRCodeImage(encodedValue);
        QRCode qrCode = QRCode.builder()
                .ticket(ticket)
                .encodedValue(encodedValue)
                .imageData(imageData)
                .build();
        log.info("Generated QR code for ticket: {}", ticket.getId());
        return qrCode;
    }
    private String generateUniqueEncodedValue() {
        String value;
        do {
            value = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        } while (qrCodeRepository.existsByEncodedValue(value));
        return value;
    }
    private String generateQRCodeImage(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = Map.of(
                    EncodeHintType.MARGIN, 1,
                    EncodeHintType.CHARACTER_SET, "UTF-8"
            );
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE,
                    hints
            );
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, IMAGE_FORMAT, outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code image", e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
    public QRCode regenerateImage(QRCode qrCode) {
        String imageData = generateQRCodeImage(qrCode.getEncodedValue());
        qrCode.setImageData(imageData);
        return qrCodeRepository.save(qrCode);
    }
}
