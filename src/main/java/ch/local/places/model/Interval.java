package ch.local.places.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Interval {

    @NonNull
    private final String from;

    @NonNull
    private final String to;

}
