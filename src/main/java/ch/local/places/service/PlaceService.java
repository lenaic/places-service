package ch.local.places.service;

import ch.local.places.client.UpstreamPlaceClient;
import ch.local.places.client.model.UpstreamPlace;
import ch.local.places.model.Interval;
import ch.local.places.model.Place;
import ch.local.places.model.WeekDayInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private static final Map<String, DayOfWeek> LABEL_TO_DAY_OF_WEEK_MAP = new HashMap<String, DayOfWeek>() {{
        put("monday", DayOfWeek.MONDAY);
        put("tuesday", DayOfWeek.TUESDAY);
        put("wednesday", DayOfWeek.WEDNESDAY);
        put("thursday", DayOfWeek.THURSDAY);
        put("friday", DayOfWeek.FRIDAY);
        put("saturday", DayOfWeek.SATURDAY);
        put("sunday", DayOfWeek.SUNDAY);
    }};

    @Autowired
    private UpstreamPlaceClient upstreamPlaceClient;

    public Place getPlaceById(String id) {
        UpstreamPlace upstreamPlace = upstreamPlaceClient.getPlaceById(id);
        List<WeekDayInterval> openingHours = upstreamPlace.getOpeningHours().getDayToOpenIntervalsMap().entrySet().stream()
            .map(entry -> {
                DayOfWeek dayOfWeek = LABEL_TO_DAY_OF_WEEK_MAP.get(entry.getKey());
                List<Interval> intervals = entry.getValue().stream().map(interval -> new Interval(interval.getStart(), interval.getEnd())).collect(Collectors.toList());
                return new WeekDayInterval(dayOfWeek, intervals);
            })
            .collect(Collectors.toList());

        return new Place(upstreamPlace.getId(), upstreamPlace.getName(), upstreamPlace.getAddress(), openingHours);
    }
}
