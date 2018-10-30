package com.mozdzo.ors.search.parsedevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.ZonedDateTime;

@Document(indexName = "parsed_events", type = "parsed_event")
public class ParsedEvent {
    @Id
    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("date")
    private ZonedDateTime date = ZonedDateTime.now();

    public ParsedEvent() {
    }

    public ParsedEvent(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
