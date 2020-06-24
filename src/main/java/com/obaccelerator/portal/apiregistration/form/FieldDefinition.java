package com.obaccelerator.portal.apiregistration.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.obaccelerator.common.form.FieldType;
import com.obaccelerator.common.form.LabelExplanation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This class is used to deserialize different type of fields into. It has a superset of all field fields
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDefinition {
    protected String key;
    protected LabelExplanation labelExplanation;
    private String buttonText;
    private List<CheckBoxValue> checkBoxValues;
    private CheckBoxesMinSelectedValidator checkBoxesMinSelectedValidator;
    private boolean required;
    private String description;
    private List<String> values;
    private List<LabelValue> options;
    private String regex;
    private Integer minLength;
    private Integer maxLength;
    private FieldType type;
}
