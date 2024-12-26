package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class AuthorizedUser {
    @JsonProperty("username")
    private String name;
    private String password;
    @JsonProperty("userId")
    private UUID id;
    private String token;
    @JsonFormat
    private LocalDateTime expires;
}
