package com.obaccelerator.portal.apiregistration.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.obaccelerator.common.form.FieldType;
import com.obaccelerator.common.form.LabelExplanation;
import com.obaccelerator.common.form.RadioButtonsField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This class is used to deserialize different type of fields into. It is a superset of all FieldDefinition fields.
 * Some people would say it is better to model the actual data structures instead of having a flat list of everything.
 * However, this data is only passed on to the front-end. No processing is and should be done whatsoever by oba-portal.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDefinition {
    protected String key;
    protected LabelExplanation labelExplanation;
    private String buttonText;
    private List<LabelValue> checkBoxValues;
    private List<LabelValue> radioButtonValues;
    private CheckBoxesMinSelectedValidator checkBoxesMinSelectedValidator;
    private boolean required;
    private String description;
    private List<String> values;
    private List<SelectOption> options;
    private String regex;
    private Integer minLength;
    private Integer maxLength;
    private FieldType type;
    private boolean secret;
    private Integer rows;

}
