package com.mozdzo.ors.search

import com.mozdzo.ors.search.domain.SongDocument
import com.mozdzo.ors.search.domain.SongsRepository
import org.springframework.stereotype.Component

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@Component
class TestSongDocument {

    private final SongsRepository songsRepository

    TestSongDocument(SongsRepository songsRepository) {
        this.songsRepository = songsRepository
    }

    SongDocument create(String title = randomTitle()) {
        SongDocument songDocument = new SongDocument(
                randomAlphanumeric(100),
                title
        )
        songsRepository.save(songDocument)
    }

    private static String randomTitle() {
        randomAlphanumeric(100)
    }
}
