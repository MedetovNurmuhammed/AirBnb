package enjoying.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record SimpleResponse(HttpStatus httpStatus,
                             String message) {
}