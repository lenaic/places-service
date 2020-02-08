package ch.local.places.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpstreamPlace {

    @JsonProperty("local_entry_id")
    private String id;

    @JsonProperty("displayed_what")
    private String name;

    @JsonProperty("displayed_where")
    private String address;

    @JsonProperty("opening_hours")
    private OpeningHours openingHours;

}
