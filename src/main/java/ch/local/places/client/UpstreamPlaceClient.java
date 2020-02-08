package ch.local.places.client;

import ch.local.places.client.model.UpstreamPlace;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "upstream-place-api", url = "${client.upstream.url}")
public interface UpstreamPlaceClient {

    @GetMapping(path = "/{id}")
    UpstreamPlace getPlaceById(@PathVariable String id);

}
