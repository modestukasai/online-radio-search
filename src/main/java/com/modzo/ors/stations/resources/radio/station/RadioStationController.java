package com.modzo.ors.stations.resources.radio.station;

import com.modzo.ors.stations.domain.radio.station.RadioStation;
import com.modzo.ors.stations.domain.radio.station.commands.GetRadioStation;
import com.modzo.ors.stations.domain.radio.station.commands.GetRadioStations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.modzo.ors.stations.resources.radio.station.RadioStationModel.create;
import static org.springframework.http.ResponseEntity.ok;

@RestController
class RadioStationController {

    private final GetRadioStation.Handler radioStationHandler;

    private final GetRadioStations.Handler radioStationsHandler;

    public RadioStationController(GetRadioStation.Handler radioStationHandler,
                                  GetRadioStations.Handler radioStationsHandler
    ) {
        this.radioStationHandler = radioStationHandler;
        this.radioStationsHandler = radioStationsHandler;
    }

    @GetMapping("/radio-stations")
    ResponseEntity<RadioStationsModel> getRadioStations(RadioStationsFilter filterRequest, Pageable pageable) {
        GetRadioStations.Filter filter = new GetRadioStations.Filter(
                filterRequest.getId(),
                filterRequest.getUniqueId(),
                filterRequest.getEnabled(),
                filterRequest.getTitle(),
                filterRequest.getSongId(),
                filterRequest.getGenreId()
        );
        Page<RadioStation> foundRadioStation = radioStationsHandler.handle(
                new GetRadioStations(filter, pageable)
        );
        return ok(RadioStationsModel.create(filterRequest, foundRadioStation, pageable));
    }

    @GetMapping("/radio-stations/{id}")
    ResponseEntity<RadioStationModel> getRadioStation(@PathVariable("id") long id) {
        RadioStation foundRadioStation = radioStationHandler.handle(new GetRadioStation(id));
        return ok(create(foundRadioStation));
    }

}