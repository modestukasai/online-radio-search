package com.modzo.ors.stations.resources.admin.radio.station.stream.songs;

import com.modzo.ors.stations.services.stream.scrapper.songs.SongsUpdaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
class UpdateStreamSongsController {

    private final SongsUpdaterService streamSongsService;

    public UpdateStreamSongsController(SongsUpdaterService streamSongsService) {
        this.streamSongsService = streamSongsService;
    }

    @PostMapping("/admin/radio-stations/{radio-station-id}/streams/{stream-id}/songs")
    ResponseEntity<String> latestInfo(@PathVariable("radio-station-id") long radioStationId,
                              @PathVariable("stream-id") long streamId) {
        streamSongsService.update(radioStationId, streamId);
        return noContent().build();
    }
}
