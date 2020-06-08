package com.obaccelerator.portal.redirecturl;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class RedirectUrlController {

    private final RedirectUrlGatewayService redirectUrlGatewayService;

    public RedirectUrlController(RedirectUrlGatewayService redirectUrlGatewayService) {
        this.redirectUrlGatewayService = redirectUrlGatewayService;
    }

    @GetMapping("/redirect-urls")
    public List<RedirectUrlResponse> getRedirectUrlsForOrganization(PortalUser portalUser) {
        return redirectUrlGatewayService.findAllForOrganization(portalUser.getOrganizationId());
    }

    @PostMapping("/redirect-urls")
    public RedirectUrlResponse createRedirectUrl(@RequestBody @Valid CreateRedirectUrlRequest createRedirectUrlRequest, PortalUser portalUser) {
        return redirectUrlGatewayService.create(portalUser.getOrganizationId(), createRedirectUrlRequest);
    }

    @DeleteMapping("/redirect-urls/{redirectUrlId}")
    public void deleteRedirectUrl(@PathVariable("redirectUrlId") UUID redirectUrlId, PortalUser portalUser) {
        redirectUrlGatewayService.delete(portalUser.getOrganizationId(), redirectUrlId);
    }
}
