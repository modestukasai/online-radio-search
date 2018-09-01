package com.mozdzo.ors.resources.radio.station;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mozdzo.ors.domain.radio.station.RadioStation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class RadioStationResource extends ResourceSupport {

    private final RadioStationResponse radioStation;

    @JsonCreator
    private RadioStationResource(@JsonProperty("radioStation") RadioStationResponse radioStation,
                                 @JsonProperty("_links") Map<String, Link> links) {
        this.radioStation = radioStation;
        links.forEach((label, link) -> add(link.withRel(label)));
    }

    static RadioStationResource create(RadioStation radioStation) {
        RadioStationResponse response = RadioStationResponse.create(radioStation);
        Link link = linkTo(methodOn(RadioStationController.class)
                .getRadioStation(response.getId())).withSelfRel();
        return new RadioStationResource(response, singletonMap(link.getRel(), link));
    }

    public RadioStationResponse getRadioStation() {
        return radioStation;
    }
}