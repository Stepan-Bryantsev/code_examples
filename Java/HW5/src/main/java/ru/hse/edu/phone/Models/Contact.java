/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import ru.hse.edu.phone.Models.ValidationEvent.ValidationResult;

/**
 * Основная модель контакта. Для создания экземпляра импользуется патерн Builder.
 */
public class Contact implements Serializable {

  private final String lastName;
  private final String firstName;
  private final String secondName;
  private final String mobilePhone;
  private final String homePhone;
  private final String address;
  private final LocalDate birthday;
  private final String comment;

  private Contact(Builder builder) {
    this.lastName = builder.lastName;
    this.firstName = builder.firstName;
    this.secondName = builder.secondName;
    this.mobilePhone = builder.mobilePhone;
    this.homePhone = builder.homePhone;
    this.address = builder.address;
    this.birthday = builder.birthday;
    this.comment = builder.comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Contact that = (Contact) o;
    return Objects.equals(lastName, that.lastName) && Objects
        .equals(firstName, that.firstName) && Objects.equals(secondName, that.secondName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastName, firstName, secondName);
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public String getAddress() {
    return address;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    return "Contact{" +
        "lastName='" + lastName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", secondName='" + secondName + '\'' +
        ", mobilePhone='" + mobilePhone + '\'' +
        ", homePhone='" + homePhone + '\'' +
        ", address='" + address + '\'' +
        ", birthday=" + birthday +
        ", comment='" + comment + '\'' +
        '}';
  }

  /**
   * Патерн Builder. При изменении каждого поля слушатели получают событие с результатом валидации.
   */
  public static class Builder {

    private final List<ValidationListener> lastNameValidationListeners = new LinkedList<>();
    private final List<ValidationListener> firstNameValidationListeners = new LinkedList<>();
    private final List<ValidationListener> secondNameValidationListeners = new LinkedList<>();
    private final List<ValidationListener> mobilePhoneValidationListeners = new LinkedList<>();
    private final List<ValidationListener> homePhoneValidationListeners = new LinkedList<>();
    private final List<ValidationListener> addressValidationListeners = new LinkedList<>();
    private final List<ValidationListener> birthdayValidationListeners = new LinkedList<>();
    private final List<ValidationListener> commentValidationListeners = new LinkedList<>();
    private String lastName;
    private String firstName;
    private String secondName;
    private String mobilePhone;
    private String homePhone;
    private String address;
    private LocalDate birthday;
    private String comment;

    public Builder() {
    }

    public Builder(Contact phone) {
      lastName = phone.getLastName();
      firstName = phone.getFirstName();
      secondName = phone.getSecondName();
      mobilePhone = phone.getMobilePhone();
      homePhone = phone.getHomePhone();
      address = phone.getAddress();
      birthday = phone.getBirthday();
      comment = phone.getComment();
    }

    public void addLastNameValidationListener(ValidationListener validationListener) {
      lastNameValidationListeners.add(validationListener);
    }

    public void addFirstNameValidationListener(ValidationListener validationListener) {
      firstNameValidationListeners.add(validationListener);
    }

    public void addSecondNameValidationListener(ValidationListener validationListener) {
      secondNameValidationListeners.add(validationListener);
    }

    public void addMobilePhoneValidationListener(ValidationListener validationListener) {
      mobilePhoneValidationListeners.add(validationListener);
    }

    public void addHomePhoneValidationListener(ValidationListener validationListener) {
      homePhoneValidationListeners.add(validationListener);
    }

    public void addAddressValidationListener(ValidationListener validationListener) {
      addressValidationListeners.add(validationListener);
    }

    public void addBirthdayValidationListener(ValidationListener validationListener) {
      birthdayValidationListeners.add(validationListener);
    }

    public void addCommentValidationListener(ValidationListener validationListener) {
      commentValidationListeners.add(validationListener);
    }

    public Builder setLastName(String lastName) {
      keyFieldValidation(lastName, lastNameValidationListeners);
      this.lastName = lastName;
      return this;
    }

    public Builder setFirstName(String firstName) {
      keyFieldValidation(firstName, firstNameValidationListeners);
      this.firstName = firstName;
      return this;
    }

    public Builder setSecondName(String secondName) {
      additionalFieldValidation(secondName, secondNameValidationListeners);
      this.secondName = secondName;
      return this;
    }

    public Builder setMobilePhone(String mobilePhone) {
      phoneFieldValidation(mobilePhone, mobilePhoneValidationListeners);
      this.mobilePhone = mobilePhone;
      if (!hasPhone()) {
        notifyEventListeners(homePhoneValidationListeners,
            new ValidationEvent(this, ValidationResult.ERROR, "No phone"));
        notifyEventListeners(mobilePhoneValidationListeners,
            new ValidationEvent(this, ValidationResult.ERROR, "No phone"));
      }
      return this;
    }

    public Builder setHomePhone(String homePhone) {
      phoneFieldValidation(homePhone, homePhoneValidationListeners);
      this.homePhone = homePhone;
      if (!hasPhone()) {
        notifyEventListeners(homePhoneValidationListeners,
            new ValidationEvent(this, ValidationResult.ERROR, "No phone"));
        notifyEventListeners(mobilePhoneValidationListeners,
            new ValidationEvent(this, ValidationResult.ERROR, "No phone"));
      }
      return this;
    }

    public Builder setAddress(String address) {
      additionalFieldValidation(address, addressValidationListeners);
      this.address = address;
      return this;
    }

    public Builder setBirthday(LocalDate birthday) {
      dateFieldValidation(birthday, birthdayValidationListeners);
      this.birthday = birthday;
      return this;
    }

    public Builder setComment(String comment) {
      additionalFieldValidation(comment, commentValidationListeners);
      this.comment = comment;
      return this;
    }

    public Contact build() {
      return new Contact(this);
    }

    public boolean validate() {
      return (firstName != null && !firstName.isEmpty()) &&
          (lastName != null && !lastName.isEmpty()) &&
          hasPhone();
    }

    /**
     * Метод проверяет наличие хотябы одного телефонного номера.
     *
     * @return Есть ли у контакта хотябы один номер.
     */
    private boolean hasPhone() {
      return (mobilePhone != null && !mobilePhone.isBlank()) ||
          (homePhone != null && !homePhone.isBlank());
    }

    private void notifyEventListeners(Collection<ValidationListener> listeners,
        ValidationEvent event) {
      for (ValidationListener listener : listeners) {
        listener.onValidationEvent(event);
      }
    }

    private void keyFieldValidation(String field, List<ValidationListener> listeners) {
      if (field == null || field.isEmpty()) {
        notifyEventListeners(listeners,
            new ValidationEvent(this, ValidationResult.ERROR, "Пустое поле"));
      } else {
        notifyEventListeners(listeners, new ValidationEvent(this, ValidationResult.CORRECT, "OK"));
      }
    }

    private void additionalFieldValidation(String field, List<ValidationListener> listeners) {
      if (field == null || field.isEmpty() || field.isBlank()) {
        notifyEventListeners(listeners,
            new ValidationEvent(this, ValidationResult.WARNING, "Пустое поле"));
      } else {
        notifyEventListeners(listeners, new ValidationEvent(this, ValidationResult.CORRECT, "OK"));
      }
    }

    private void phoneFieldValidation(String phone, List<ValidationListener> listeners) {
      if (phone == null || phone.isEmpty() || phone.isBlank()) {
        notifyEventListeners(listeners,
            new ValidationEvent(this, ValidationResult.WARNING, "Пустое поле"));
      } else if (!phone.matches("[0-9\\s()\\-]+")) {
        notifyEventListeners(listeners,
            new ValidationEvent(this, ValidationResult.WARNING, "Неожиданные символы"));
      } else {
        notifyEventListeners(listeners, new ValidationEvent(this, ValidationResult.CORRECT, "OK"));
      }
    }

    private void dateFieldValidation(LocalDate date, List<ValidationListener> listeners) {
      if (date == null) {
        notifyEventListeners(listeners,
            new ValidationEvent(this, ValidationResult.WARNING, "Пустое поле"));
      } else if (LocalDate.now().compareTo(date) < 0) {
        notifyEventListeners(listeners,
            new ValidationEvent(this, ValidationResult.WARNING, "Вы из будущего?"));
      } else {
        notifyEventListeners(listeners, new ValidationEvent(this, ValidationResult.CORRECT, "OK"));
      }
    }
  }
}

