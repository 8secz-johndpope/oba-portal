package com.obaccelerator.portal.page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    private String uniqueUrlName;
    private String title;
    private String publicationDate;
    private String htmlIntroduction;
    private String htmlBody;

}
