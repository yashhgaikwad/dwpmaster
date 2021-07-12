package dwp.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Location {
    Double latitude;
    Double longitude;
}
