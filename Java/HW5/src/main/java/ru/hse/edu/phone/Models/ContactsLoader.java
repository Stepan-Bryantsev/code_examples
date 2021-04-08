/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.Models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

public class ContactsLoader {

  /**
   * Метод загруки и десериализации массива контактов.
   * @param path путь к файлу.
   * @return дессериализорованная коллекция контактов.
   */
  @SuppressWarnings("unchecked")
  public Collection<Contact> importContacts(String path) {
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
      return (Collection<Contact>) inputStream.readObject();
    } catch (FileNotFoundException ex) {
      throw new ContactsLoadException("Проверьте наличие файла:" + path, "Файл не найден!");
    } catch (SecurityException ex) {
      throw new ContactsLoadException("Ошибка нарушения безопасности системы.",
          "Нарушение безопасности!");
    } catch (IOException ex) {
      throw new ContactsLoadException("Возникла непредвиденная ошибка при загрузке файла.",
          "Пробылемы с файлом.");
    } catch (ClassCastException ex) {
      throw new ContactsLoadException("Данные из файла: " + path + "не могут быть импортированы!",
          "Ошибка загрузки");
    } catch (ClassNotFoundException ex) {
      throw new ContactsLoadException("Сбой в работе приложения.", "Ошибка!");
    }
  }

  /**
   * Метод сериализации и сохранения контактов.
   * @param contactsCollection коллекция контактов которая будет сохранена.
   * @param path путь к файлу.
   */
  public void exportContacts(Collection<Contact> contactsCollection, String path) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
      outputStream.writeObject(contactsCollection);
    } catch (SecurityException ex) {
      throw new ContactsLoadException("Ошибка нарушения безопасности системы.",
          "Нарушение безопасности системы!");
    } catch (IOException | NullPointerException ex) {
      throw new ContactsLoadException("Возникла непредвиденная ошибка при сохранении файла.",
          "Пробылемы с файлом.");
    }
  }
}
