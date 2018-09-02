package com.mozdzo.ors.domain.radio.station.commands;

import com.mozdzo.ors.domain.DomainException;
import com.mozdzo.ors.domain.radio.station.RadioStation;
import com.mozdzo.ors.domain.radio.station.RadioStations;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CreateRadioStation {
    private final String title;

    public CreateRadioStation(String title) {
        this.title = title;
    }

    private RadioStation toRadioStation() {
        return new RadioStation(this.title);
    }

    @Component
    public static class Handler {
        private final RadioStations radioStations;

        private final Validator validator;

        Handler(RadioStations radioStations, Validator validator) {
            this.radioStations = radioStations;
            this.validator = validator;
        }

        public Result handle(CreateRadioStation command) {
            validator.validate(command);
            RadioStation savedRadioStation = radioStations.save(command.toRadioStation());
            return new Result(savedRadioStation.getId());
        }
    }

    @Component
    private static class Validator {
        void validate(CreateRadioStation command) {
            if (isBlank(command.title)) {
                throw new DomainException("FIELD_TITLE_NOT_BLANK", "Field title cannot be blank");
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
