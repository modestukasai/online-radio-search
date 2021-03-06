package com.modzo.ors.stations.resources.admin.radio.station.stream.update

import com.modzo.ors.HttpEntityBuilder
import com.modzo.ors.TestUsers
import com.modzo.ors.stations.domain.radio.station.RadioStation
import com.modzo.ors.stations.domain.radio.station.stream.RadioStationStream
import com.modzo.ors.stations.resources.IntegrationSpec
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpMethod.PATCH
import static org.springframework.http.HttpStatus.ACCEPTED
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric

class UpdateRadioStationStreamControllerSpec extends IntegrationSpec {

    void 'admin should update radio station stream'() {
        given:
            RadioStation radioStation = testRadioStation.create()
        and:
            RadioStationStream stream = testRadioStationStream.create(radioStation.id)
        and:
            UpdateRadioStationStreamRequest request = new UpdateRadioStationStreamRequest(
                    url: "http://www.${randomAlphanumeric(14)}.com",
                    bitRate: 195,
                    type: RadioStationStream.Type.MPEG,
                    working: true
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    "/admin/radio-stations/${radioStation.id}/streams/${stream.id}",
                    PATCH,
                    HttpEntityBuilder.builder()
                            .bearer(token(TestUsers.ADMIN))
                            .body(request)
                            .build(),
                    String
            )
        then:
            response.statusCode == ACCEPTED
        and:
            with(radioStationStreams.findById(stream.id).get()) {
                url == request.url
                bitRate == request.bitRate
                type.get() == request.type
                working == request.working
            }
    }

    void 'admin should update radio station stream with minimal data'() {
        given:
            RadioStation radioStation = testRadioStation.create()
        and:
            RadioStationStream stream = testRadioStationStream.create(radioStation.id)
        and:
            UpdateRadioStationStreamRequest request = new UpdateRadioStationStreamRequest(
                    url: "http://www.${randomAlphanumeric(14)}.com",
                    working: true
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    "/admin/radio-stations/${radioStation.id}/streams/${stream.id}",
                    PATCH,
                    HttpEntityBuilder.builder()
                            .bearer(token(TestUsers.ADMIN))
                            .body(request)
                            .build(),
                    String
            )
        then:
            response.statusCode == ACCEPTED
        and:
            with(radioStationStreams.findById(stream.id).get()) {
                url == request.url
                bitRate == null
                type.get() == RadioStationStream.Type.UNKNOWN
                working == request.working
            }
    }
}
