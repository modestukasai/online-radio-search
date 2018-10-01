package com.mozdzo.ors.domain.radio.station.song.commands;

import com.mozdzo.ors.domain.DomainException;
import com.mozdzo.ors.domain.radio.station.RadioStations;
import com.mozdzo.ors.domain.radio.station.song.RadioStationSong;
import com.mozdzo.ors.domain.radio.station.song.RadioStationSongs;
import com.mozdzo.ors.domain.song.Songs;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

public class CreateRadioStationSong {

    private final long songId;

    private final long radioStationId;

    private final ZonedDateTime playedTime;

    public CreateRadioStationSong(long songId, long radioStationId, ZonedDateTime playedTime) {
        this.songId = songId;
        this.radioStationId = radioStationId;
        this.playedTime = playedTime;
    }

    private RadioStationSong toRadioStationSong() {
        return new RadioStationSong(radioStationId, songId, playedTime);
    }

    @Component
    public static class Handler {
        private final RadioStationSongs radioStationSongs;

        private final Validator validator;

        private final ApplicationEventPublisher applicationEventPublisher;

        public Handler(RadioStationSongs radioStationSongs,
                       Validator validator,
                       ApplicationEventPublisher applicationEventPublisher) {
            this.radioStationSongs = radioStationSongs;
            this.validator = validator;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        @Transactional
        public Result handle(CreateRadioStationSong command) {
            validator.validate(command);
            RadioStationSong radioStationSong = radioStationSongs.save(command.toRadioStationSong());
            //TODO:
//            applicationEventPublisher.publishEvent(
//                    new SongCreated(
//                            radioStationSong,
//                            new SongCreated.Data(
//                                    radioStationSong.getUniqueId(),
//                                    radioStationSong.getTitle()
//                            ))
//            );
            return new Result(radioStationSong.getId());
        }
    }

    @Component
    private static class Validator {
        private final RadioStations radioStations;

        private final Songs songs;

        public Validator(RadioStations radioStations, Songs songs) {
            this.radioStations = radioStations;
            this.songs = songs;
        }

        void validate(CreateRadioStationSong command) {
            if (!radioStations.findById(command.radioStationId).isPresent()) {
                throw new DomainException("FIELD_RADIO_STATION_ID_INCORRECT", "Radio station by id was not found");
            }
            if (!songs.findById(command.songId).isPresent()) {
                throw new DomainException("FIELD_SONG_ID_INCORRECT", "Song by id was not found");
            }
            if (command.playedTime == null) {
                throw new DomainException("FIELD_PLAYED_TIME_IS_NULL", "Field played time cannot be blank");
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