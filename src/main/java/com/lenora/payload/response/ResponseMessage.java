package com.lenora.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseMessage<T> {

    private String message;           // Başarı veya hata mesajı
    private HttpStatus httpStatus;    // HTTP durumu
    private T object;                 // Dönen veri (örneğin DoctorResponse)

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time = LocalDateTime.now(); // Yanıtın oluşturulduğu zaman
}
