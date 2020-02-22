package com.modzo.ors.stations.services.stream.scrapper.stream;

import com.modzo.ors.stations.services.stream.scrapper.WebPageReader;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

@Component
public class StreamScrapper {

    private final WebPageReader siteReader;

    private final StreamInfoUrlGenerator generator;

    public StreamScrapper(WebPageReader siteReader,
                          StreamInfoUrlGenerator generator) {
        this.siteReader = siteReader;
        this.generator = generator;
    }

    public Optional<Response> scrap(Request request) {
        return generator.generateUrls(request.url)
                .stream()
                .map(this.siteReader::read)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(WebPageReader.Response::getBody)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::extract)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Response> extract(String body) {
        Document document = Jsoup.parse(body);
        List<Element> tables = new ArrayList<>(document.getElementsByTag("table"));
        List<Element> trs = tables.stream()
                .map(element -> element.getElementsByTag("tr"))
                .flatMap(Collection::stream)
                .collect(toList());
        Map<String, String> requiredTrs = tableValues(trs);
        return Optional.of(
                new Response(
                        requiredTrs.getOrDefault("Listing Status:", ""),
                        Response.Format.findFormat(requiredTrs.getOrDefault("Stream Status:", "")),
                        bitRate(requiredTrs.getOrDefault("Stream Status:", "")),
                        listenerPeak(requiredTrs.getOrDefault("Listener Peak:", "")),
                        requiredTrs.getOrDefault("Stream Name:", ""),
                        genres(requiredTrs.getOrDefault("Stream Genre(s):", "")),
                        requiredTrs.getOrDefault("Stream Website:", "")
                )
        );
    }

    private int listenerPeak(String line) {
        String cleanLine = line.trim();
        if (isCreatable(cleanLine)) {
            return parseInt(cleanLine);
        }
        return 0;
    }

    private List<String> genres(String genresLine) {
        return Stream.of(genresLine.split(" , "))
                .map(String::trim)
                .collect(toList());
    }

    private int bitRate(String line) {
        return Stream.of(line.split(" "))
                .filter(NumberUtils::isCreatable)
                .map(Integer::valueOf)
                .findFirst().orElse(0);
    }

    private Map<String, String> tableValues(List<Element> elements) {
        return elements.stream()
                .filter(element -> getTd(element).size() == 2)
                .collect(toMap(element -> getTd(element).get(0).text(), element -> getTd(element).get(1).text()));
    }

    private Elements getTd(Element element) {
        return element.getElementsByTag("td");
    }

    public static class Request {

        private final String url;

        public Request(String url) {
            this.url = url;
        }
    }

    public static class Response {
        private final String listingStatus;
        private final Format format;
        private final int bitrate;
        private final int listenerPeak;
        private final String streamName;
        private final List<String> genres;
        private final String website;

        Response(String listingStatus, Format format, int bitrate,
                 int listenerPeak, String streamName, List<String> genres, String website) {
            this.listingStatus = listingStatus;
            this.format = format;
            this.bitrate = bitrate;
            this.listenerPeak = listenerPeak;
            this.streamName = streamName;
            this.genres = genres;
            this.website = website;
        }

        public String getListingStatus() {
            return listingStatus;
        }

        public Format getFormat() {
            return format;
        }

        public int getBitrate() {
            return bitrate;
        }

        public int getListenerPeak() {
            return listenerPeak;
        }

        public String getStreamName() {
            return streamName;
        }

        public List<String> getGenres() {
            return genres;
        }

        public String getWebsite() {
            return website;
        }

        public enum Format {
            MP3, AAC, MPEG, UNKNOWN;

            public static Format findFormat(String line) {
                String uppercaseLine = line.toUpperCase();
                for (Format format : values()) {
                    if (uppercaseLine.contains(format.name())) {
                        return format;
                    }
                }
                return UNKNOWN;
            }
        }
    }
}