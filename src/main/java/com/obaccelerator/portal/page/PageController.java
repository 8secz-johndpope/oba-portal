package com.obaccelerator.portal.page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PageController {

    private PageService pageService;

    public PageController(final PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/pages/{uniqueUrlName}")
    public ResponseEntity<Page> page(@PathVariable("uniqueUrlName") String uniqueUrlName) {
        Optional<Page> pageOptional = pageService.findPage(uniqueUrlName);
        if (!pageOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pageOptional.get(), HttpStatus.OK);
    }
}
