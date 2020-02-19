package ch.local.places.service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.local.places.client.UpstreamPlaceClient;
import ch.local.places.client.model.UpstreamPlace;
import ch.local.places.model.Interval;
import ch.local.places.model.Place;
import ch.local.places.model.WeekDayInterval;

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

        boolean open = false;
        ZonedDateTime localNow = Instant.now().atZone(ZoneId.systemDefault());
        DayOfWeek currentDayOfWeek = localNow.getDayOfWeek();
        for (WeekDayInterval dayInterval : openingHours) {
            if (!dayInterval.getDayOfWeek().equals(currentDayOfWeek))
                continue;

            for (Interval formattedInterval : dayInterval.getIntervals()) {
                final ZonedDateTime from = ZonedDateTime.of(localNow.toLocalDate(), LocalTime.parse(formattedInterval.getFrom()), localNow.getZone());
                ZonedDateTime to = ZonedDateTime.of(localNow.toLocalDate(), LocalTime.parse(formattedInterval.getTo()), localNow.getZone());
                if (to.isBefore(from))
                    to = to.plusDays(1);

                org.threeten.extra.Interval interval = org.threeten.extra.Interval.of(from.toInstant(), to.toInstant());
                if (interval.contains(localNow.toInstant())) {
                    open = true;
                    break;
                }
            }
        }

        String nextOpeningDateTime = "now";
        if (!open) {
            boolean foundNext = false;
            for (WeekDayInterval dayInterval : openingHours) {
                if (!dayInterval.getDayOfWeek().equals(currentDayOfWeek))
                    continue;

                for (Interval formattedInterval : dayInterval.getIntervals()) {
                    final ZonedDateTime from = ZonedDateTime.of(localNow.toLocalDate(), LocalTime.parse(formattedInterval.getFrom()), localNow.getZone());
                    if (!from.isBefore(localNow)) {
                        nextOpeningDateTime = String.format("Today at %s", formattedInterval.getFrom());
                        foundNext = true;
                    }
                }
            }

            if (!foundNext) {
                for (WeekDayInterval dayInterval : openingHours) {
                    if (dayInterval.getDayOfWeek().getValue() > currentDayOfWeek.getValue())
                        continue;

                    for (Interval formattedInterval : dayInterval.getIntervals()) {
                        final ZonedDateTime from = ZonedDateTime.of(localNow.toLocalDate(), LocalTime.parse(formattedInterval.getFrom()), localNow.getZone());
                        nextOpeningDateTime = String.format("%s at %s", dayInterval.getDayOfWeek().toString(), formattedInterval.getFrom());
                        foundNext = true;
                        break;
                    }

                    if (foundNext)
                        break;
                }
            }

            if (!foundNext) {
                for (WeekDayInterval dayInterval : openingHours) {
                    if (dayInterval.getDayOfWeek().getValue() < currentDayOfWeek.getValue())
                        continue;

                    for (Interval formattedInterval : dayInterval.getIntervals()) {
                        final ZonedDateTime from = ZonedDateTime.of(localNow.toLocalDate(), LocalTime.parse(formattedInterval.getFrom()), localNow.getZone());
                        nextOpeningDateTime = String.format("%s at %s", dayInterval.getDayOfWeek().toString(), formattedInterval.getFrom());
                        foundNext = true;
                        break;
                    }

                    if (foundNext)
                        break;
                }
            }

        }

        return new Place(upstreamPlace.getId(), upstreamPlace.getName(), upstreamPlace.getAddress(), open, nextOpeningDateTime, openingHours);
    }
}
