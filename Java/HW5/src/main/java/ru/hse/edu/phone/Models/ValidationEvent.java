/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.Models;

import java.util.EventObject;

/**
 * Событие валидации поля билдера контакта.
 */
public class ValidationEvent extends EventObject {

  public enum ValidationResult {
    CORRECT,
    WARNING,
    ERROR,
  }

  private final String validationMessage;
  private final ValidationResult validationResult;

  public ValidationEvent(Object source, ValidationResult validationResult, String validationMessage) {
    super(source);
    this.validationMessage = validationMessage;
    this.validationResult = validationResult;
  }

  public String getValidationMessage() {
    return validationMessage;
  }

  public ValidationResult getValidationResult() {
    return validationResult;
  }
}
