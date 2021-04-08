/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.Models;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.util.Pair;
import ru.hse.edu.phone.Models.Contact.Builder;
import ru.hse.edu.phone.Settings;

/**
 * Основной класс хранения, загрузки, обработки сохранения и изменения контактов.
 */
public class ContactsHolder {

  private final ContactsLoader contactsLoader;

  List<Contact> contacts = new ArrayList<>();

  public ContactsHolder() {
    contactsLoader = new ContactsLoader();
  }

  /**
   * Метод загрузки данных.
   */
  public void loadData() {
    File dataFile = new File(Settings.DATA_FOLDER, Settings.DATA_FILE_NAME + Settings.EXTENSION);
    contacts = new ArrayList<>(contactsLoader.importContacts(dataFile.getPath()));
  }

  public Collection<Contact> getContacts() {
    return new ArrayList<>(contacts);
  }

  public Collection<Contact> getContacts(Predicate<? super Contact> predicate) {
    return contacts.stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Метод импорта данных.
   * @param path путь к файлу с данными.
   * @return две коллекции: контакты которые не прошли валидацию и контакты которые уже были загружены.
   */
  public Pair<Collection<Contact>, Collection<Contact>> importData(String path) {
    Collection<Contact> loadedPhones = contactsLoader.importContacts(path);

    Collection<Contact> validationFailed = new ArrayList<>();
    Collection<Contact> overwriteFailed = new ArrayList<>();
    for (Contact contact : loadedPhones) {
      if (!new Builder(contact).validate())
        validationFailed.add(contact);
      else if (contacts.contains(contact))
        overwriteFailed.add(contact);
      else
        contacts.add(contact);
    }
    try {
      saveData();
    }
    catch (ContactsLoadException ex) {
      ex.printStackTrace();
    }
    return new Pair<>(validationFailed, overwriteFailed);
  }

  /**
   * Метод экспорта данных.
   * @param path путь к файлу в который будут выгружены данные.
   * @param overwrite если файл существует нужно ли его перезаписать?
   * @return были ли выгружены данные.
   */
  public boolean exportData(String path, boolean overwrite) {
    try {
      if (!overwrite && new File(path).exists()) {
        return false;
      }
    } catch (SecurityException ex) {
      return false;
    }
    contactsLoader.exportContacts(contacts, path);
    return true;
  }

  /**
   * Метод сохранения всех текущих данных.
   */
  public void saveData() {
    File dataDir = new File(Settings.DATA_FOLDER);
    try {
      if (!dataDir.exists() && !dataDir.mkdir()) {
        throw new ContactsLoadException("Не удается сохранить данные", "Ошибка сохранения.");
      }
    } catch (SecurityException ex) {
      throw new ContactsLoadException("Не удается сохранить данные", "Ошибка сохранения.");
    }
    File dataFile = new File(dataDir, Settings.DATA_FILE_NAME + Settings.EXTENSION);
    contactsLoader.exportContacts(contacts, dataFile.getPath());
  }

  /**
   * Метод добавления нового контакта.
   * @param contactBuilder - контакт который планируется добавить.
   */
  public void addContact(Builder contactBuilder) {
    if (!contactBuilder.validate())
      throw new IllegalArgumentException("Ошибка валидации!");
    Contact newPhone = contactBuilder.build();
    if (contacts.contains(newPhone))
      throw new IllegalArgumentException("Данный контакт уже существует!");

    contacts.add(newPhone);
    try {
      saveData();
    }
    catch (ContactsLoadException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Метод замены (редактирования) контакта
   * @param oldContact - старый контакт.
   * @param newContact - новый контакт.
   */
  public void editContact(Contact oldContact, Builder newContact) {
    if (!newContact.validate()) {
      throw new IllegalArgumentException("Ошибка валидации");
    }
    if (oldContact == null || !contacts.contains(oldContact)) {
      throw new IllegalArgumentException("Редактируемый контакт был удален!");
    }

    Collections.replaceAll(contacts, oldContact, newContact.build());
    try {
      saveData();
    } catch (ContactsLoadException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Метод удаления контакта.
   * @param contact - контакт для удаления.
   */
  public void deleteContact(Contact contact) {
    if (contact == null || !contacts.contains(contact))
      throw new IllegalArgumentException("Данный контакт отсутствует");
    contacts.remove(contact);
    try {
      saveData();
    } catch (ContactsLoadException ex) {
      ex.printStackTrace();
    }
  }
}
