/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.Models;

public class ContactsLoadException extends RuntimeException {

  private final String exceptionHeader;

  public ContactsLoadException(String message, String header) {
    super(message);
    exceptionHeader = header;
  }

  public String getExceptionHeader() {
    return exceptionHeader;
  }
}
