package com.obaccelerator.portal.apiregistration.form;

import com.obaccelerator.common.form.LayoutDirection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FieldLayoutGroup {
    private String groupTitle;
    private LayoutDirection layoutDirection;
    private List<FieldDefinition> fields;

}
