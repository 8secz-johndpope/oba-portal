package com.obaccelerator.portal.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {

    private ObjectMapper mapper = new ObjectMapper();
    private static final String PAGES_DIR = "/pages/";
    private static final String PAGE_FILE_EXTENSION = ".json";

    public Optional<Page> findPage(final String uniqueUrlName) {
        String path = PAGES_DIR + uniqueUrlName + PAGE_FILE_EXTENSION;
        try (InputStream pageResource = getClass().getResourceAsStream(path)) {
            return Optional.ofNullable(mapper.readValue(pageResource, Page.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
