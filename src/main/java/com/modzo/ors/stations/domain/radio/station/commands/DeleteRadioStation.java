package com.modzo.ors.stations.domain.radio.station.commands;

import com.modzo.ors.stations.domain.DomainException;
import com.modzo.ors.stations.domain.radio.station.RadioStation;
import com.modzo.ors.stations.domain.radio.station.RadioStations;
import com.modzo.ors.stations.events.StationsDomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

public class DeleteRadioStation {
    private final long id;

    public DeleteRadioStation(long id) {
        this.id = id;
    }

    @Component
    public static class Handler {
        private final RadioStations radioStations;

        private final Validator validator;

        private final ApplicationEventPublisher applicationEventPublisher;

        Handler(RadioStations radioStations,
                Validator validator,
                ApplicationEventPublisher applicationEventPublisher) {
            this.radioStations = radioStations;
            this.validator = validator;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        @Transactional
        public void handle(DeleteRadioStation command) {
            validator.validate(command);

            RadioStation radioStation = radioStations.findById(command.id).get();

            radioStations.delete(radioStation);

            applicationEventPublisher.publishEvent(
                    new StationsDomainEvent(
                            radioStation,
                            StationsDomainEvent.Action.DELETED,
                            StationsDomainEvent.Type.RADIO_STATION,
                            radioStation.getId()
                    )
            );
        }
    }

    @Component
    private static class Validator {
        private final RadioStations radioStations;

        Validator(RadioStations radioStations) {
            this.radioStations = radioStations;
        }

        void validate(DeleteRadioStation command) {
            radioStations.findById(command.id)
                    .orElseThrow(() -> radioStationWithIdDoesNotExist(command));

            if (command.id <= 0) {
                throw new DomainException(
                        "FIELD_ID_SHOULD_BE_POSITIVE",
                        "id",
                        "Field id must be positive"
                );
            }
        }

        private DomainException radioStationWithIdDoesNotExist(DeleteRadioStation command) {
            return new DomainException(
                    "RADIO_STATION_WITH_ID_DOES_NOT_EXIST",
                    "id",
                    String.format("Radio station with id = `%s` does not exist", command.id)
            );
        }
    }
}
