package com.obaccelerator.portal.redirecturl;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class RedirectUrlController {

    private final RedirectUrlGatewayService redirectUrlGatewayService;

    public RedirectUrlController(RedirectUrlGatewayService redirectUrlGatewayService) {
        this.redirectUrlGatewayService = redirectUrlGatewayService;
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @GetMapping("/redirect-urls")
    public List<RedirectUrlResponse> getRedirectUrlsForOrganization(PortalUser portalUser) {
        return redirectUrlGatewayService.findAllForOrganization(portalUser.getOrganizationId());
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @PostMapping("/redirect-urls")
    public RedirectUrlResponse createRedirectUrl(@RequestBody @Valid CreateRedirectUrlRequest createRedirectUrlRequest, PortalUser portalUser) {
        return redirectUrlGatewayService.create(portalUser.getOrganizationId(), createRedirectUrlRequest);
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @DeleteMapping("/redirect-urls/{redirectUrlId}")
    public void deleteRedirectUrl(@PathVariable("redirectUrlId") UUID redirectUrlId, PortalUser portalUser) {
        redirectUrlGatewayService.delete(portalUser.getOrganizationId(), redirectUrlId);
    }
}
