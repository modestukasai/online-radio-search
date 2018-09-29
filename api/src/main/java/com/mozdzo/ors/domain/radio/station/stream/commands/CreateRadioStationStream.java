package com.mozdzo.ors.domain.radio.station.stream.commands;

import com.mozdzo.ors.domain.DomainException;
import com.mozdzo.ors.domain.events.StreamCreated;
import com.mozdzo.ors.domain.radio.station.RadioStations;
import com.mozdzo.ors.domain.radio.station.stream.RadioStationStream;
import com.mozdzo.ors.domain.radio.station.stream.RadioStationStreams;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CreateRadioStationStream {
    private final long radioStationId;

    private final String url;

    public CreateRadioStationStream(long radioStationId, String url) {
        this.radioStationId = radioStationId;
        this.url = url;
    }

    public long getRadioStationId() {
        return radioStationId;
    }

    public String getUrl() {
        return url;
    }

    private RadioStationStream toRadioStationStream() {
        return new RadioStationStream(this.radioStationId, this.url);
    }

    @Component
    public static class Handler {
        private final RadioStationStreams radioStationStreams;

        private final Validator validator;

        private final ApplicationEventPublisher applicationEventPublisher;

        public Handler(RadioStationStreams radioStationStreams,
                       Validator validator,
                       ApplicationEventPublisher applicationEventPublisher) {
            this.radioStationStreams = radioStationStreams;
            this.validator = validator;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        @Transactional
        public Result handle(CreateRadioStationStream command) {
            validator.validate(command);
            RadioStationStream savedStream = radioStationStreams.save(command.toRadioStationStream());
            applicationEventPublisher.publishEvent(new StreamCreated(savedStream,
                    new StreamCreated.Data(savedStream.getUniqueId(), savedStream.getUrl())));
            return new Result(savedStream.getId());
        }
    }

    @Component
    private static class Validator {

        private final RadioStations radioStations;

        public Validator(RadioStations radioStations) {
            this.radioStations = radioStations;
        }

        void validate(CreateRadioStationStream command) {
            if (command.radioStationId <= 0) {
                throw new DomainException("FIELD_RADIO_STATION_ID_IS_NOT_POSITIVE",
                        "Field radio station id should be positive");
            }
            if (!radioStations.findById(command.radioStationId).isPresent()) {
                throw new DomainException("FIELD_RADIO_STATION_ID_IS_INCORRECT",
                        "Radio station with id is not available");
            }
            if (isBlank(command.url)) {
                throw new DomainException("FIELD_URL_NOT_BLANK", "Field url cannot be blank");
            }
        }
    }

    public static class Result {
        public final long id;

        Result(long id) {
            this.id = id;
        }
    }
}
