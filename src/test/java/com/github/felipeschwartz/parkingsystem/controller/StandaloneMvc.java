package com.github.felipeschwartz.parkingsystem.controller;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Builds a MockMvc around a single controller, without Spring context or security, so controller tests
 * exercise routing, status codes and (de)serialization only. Bean validation is switched off here because
 * it belongs to the DTOs and is covered where the constraints are declared.
 */
final class StandaloneMvc {

    private static final Validator NO_VALIDATION = new Validator() {
        @Override
        public boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        public void validate(Object target, Errors errors) {
        }
    };

    private StandaloneMvc() {
    }

    static MockMvc of(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller).setValidator(NO_VALIDATION).build();
    }
}
