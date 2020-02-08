package ch.local.places.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpeningHours {

    @JsonProperty("days")
    private Map<String, List<Interval>> dayToOpenIntervalsMap;

    @JsonProperty("closed_on_holidays")
    private boolean closedOnHolidays;

    @JsonProperty("open_by_arrangement")
    private boolean openByArrangement;

}
