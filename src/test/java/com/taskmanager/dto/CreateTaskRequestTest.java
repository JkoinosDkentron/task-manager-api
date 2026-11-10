
package com.taskmanager.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateTaskRequestTest {

    @Test
    void shouldRejectTitleShorterThanThreeCharacters() {

        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            CreateTaskRequest request = new CreateTaskRequest(
                    "AB",
                    null,
                    null,
                    null
            );

            var violations = validator.validate(request);

            assertTrue(
                    violations.stream().anyMatch(
                            violation -> violation
                                    .getPropertyPath()
                                    .toString()
                                    .equals("title")
                    )
            );
        }
    }
}
