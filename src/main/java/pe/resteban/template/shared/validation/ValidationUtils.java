package pe.resteban.template.shared.validation;

import pe.resteban.template.shared.exception.BusinessException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new BusinessException(fieldName + " must not be null");
        }
    }
}
