package dwp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = User.UserBuilder.class)
public class User {
    @NotNull
    Long id;
    
    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @Email
    String email;
    
    String ipAddress;

    @NotNull
    Double latitude;
    
    @NotNull
    Double longitude;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserBuilder {
    }
}
