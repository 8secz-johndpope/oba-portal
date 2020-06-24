package com.obaccelerator.portal.apiregistration.form;

import com.obaccelerator.common.form.SubmittedForm;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FormDefinition {
    private String title;
    private String explanation;
    private List<FieldLayoutGroup> fieldLayoutGroups;

    public void validate(SubmittedForm submittedForm) {

    }
}
