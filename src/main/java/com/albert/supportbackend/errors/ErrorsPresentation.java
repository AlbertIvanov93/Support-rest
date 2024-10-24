package com.albert.supportbackend.errors;

import java.util.List;

public class ErrorsPresentation {

    private List<String> errors;

    public ErrorsPresentation(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
