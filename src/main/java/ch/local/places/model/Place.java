package ch.local.places.model;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class Place {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String address;

    private final boolean open;

    private final String nextOpeningDateTime;

    @NonNull
    private final List<WeekDayInterval> openingHours;

}
