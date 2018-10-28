package com.mozdzo.ors.search

import com.mozdzo.ors.resources.IntegrationSpec
import org.springframework.beans.factory.annotation.Autowired
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils

class RadioStationsRepositorySpec extends IntegrationSpec {
    @Autowired
    RadioStationsRepository repository

    void 'should create radio station document in repository'() {
        when:
            RadioStationDocument document = repository.save(
                    new RadioStationDocument(
                            RandomStringUtils.randomAlphanumeric(40),
                            RandomStringUtils.randomAlphanumeric(10)
                    )
            )
        then:
            repository.findByUniqueId(document.uniqueId).get().title == document.title
    }
}