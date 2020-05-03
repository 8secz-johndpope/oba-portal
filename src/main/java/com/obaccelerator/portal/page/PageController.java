package com.obaccelerator.portal.page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@RestController
public class PageController {

    private PageService pageService;

    public PageController(final PageService pageService) {
        this.pageService = pageService;
    }


    @PreAuthorize("hasAuthority('ORGANIZATION_ADMIN')")
    @GetMapping("/pages/{uniqueUrlName}")
    public ResponseEntity<Page> page(@PathVariable("uniqueUrlName") String uniqueUrlName) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Optional<Page> pageOptional = pageService.findPage(uniqueUrlName);
        return pageOptional.map(page -> new ResponseEntity<>(page, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
