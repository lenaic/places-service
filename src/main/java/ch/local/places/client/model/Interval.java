package ch.local.places.client.model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Interval {

    @NonNull
    private String start;

    @NonNull
    private String end;

}
