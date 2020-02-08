package ch.local.places.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class Place {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String address;

    @NonNull
    private final List<WeekDayInterval> openingHours;

}
