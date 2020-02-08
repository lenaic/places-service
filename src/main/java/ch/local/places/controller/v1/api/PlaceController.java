package ch.local.places.controller.v1.api;

import ch.local.places.service.PlaceService;
import ch.local.places.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/{id}")
    public Place getPlaceById(@PathVariable String id) {
        return placeService.getPlaceById(id);
    }

}
