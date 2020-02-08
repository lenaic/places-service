package ch.local.places.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
public class WeekDayInterval {

    @NonNull
    private final DayOfWeek dayOfWeek;

    @NonNull
    private final List<Interval> intervals;

}
