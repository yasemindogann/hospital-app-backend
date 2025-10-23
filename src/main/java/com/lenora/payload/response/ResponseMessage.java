package com.lenora.payload.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseMessage<T> {

    private LocalDateTime time = LocalDateTime.now();
    private String message;
    private HttpStatus httpStatus;
    private T object;
}
