package Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.edu.phone.Models.Contact;
import ru.hse.edu.phone.Models.Contact.Builder;
import ru.hse.edu.phone.Models.ContactsHolder;

public class ContactsHolderTest {

  @Test
  void addTest() {
    ContactsHolder contactsHolder = new ContactsHolder();
    Builder newContact = new Builder().setFirstName("FN").setSecondName("SN").setLastName("LN")
        .setMobilePhone("qwe");
    contactsHolder.addContact(newContact);
    Assertions.assertTrue(contactsHolder.getContacts().contains(newContact.build()));

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> contactsHolder.addContact(new Builder()));
    Assertions.assertThrows(IllegalArgumentException.class, () -> contactsHolder
        .addContact(new Builder().setFirstName("FN").setLastName("LN").setSecondName("SN")));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> contactsHolder.addContact(new Builder().setFirstName("FN").setMobilePhone("8 800")));
    Assertions
        .assertThrows(IllegalArgumentException.class, () -> contactsHolder.addContact(newContact));
  }

  @Test
  void editTest() {
    ContactsHolder contactsHolder = new ContactsHolder();
    Builder newContact = new Builder().setFirstName("FN").setSecondName("SN").setLastName("LN")
        .setMobilePhone("qwe");
    contactsHolder.addContact(newContact);

    contactsHolder.editContact((Contact) contactsHolder.getContacts().toArray()[0], newContact.setMobilePhone("8 800"));

    Assertions.assertEquals(
        contactsHolder.getContacts().toArray()[0].toString(), newContact.build().toString());
  }

  @Test
  void deleteTest() {
    ContactsHolder contactsHolder = new ContactsHolder();
    Builder newContact = new Builder().setFirstName("FN").setSecondName("SN").setLastName("LN")
        .setMobilePhone("qwe");
    contactsHolder.addContact(newContact);

    contactsHolder.deleteContact((Contact) contactsHolder.getContacts().toArray()[0]);

    Assertions.assertEquals(contactsHolder.getContacts().size(), 0);
  }

  @Test
  void filterTest() {
    ContactsHolder contactsHolder = new ContactsHolder();
    Builder newContact = new Builder().setFirstName("1").setSecondName("2").setLastName("3")
        .setMobilePhone("4");
    contactsHolder.addContact(newContact);
    contactsHolder.addContact(
        newContact.setFirstName("11").setSecondName("22").setLastName("33").setMobilePhone("44"));
    contactsHolder.addContact(newContact.setFirstName("111").setSecondName("222").setLastName("333")
        .setMobilePhone("444"));
    contactsHolder.addContact(
        newContact.setFirstName("1111").setSecondName("2222").setLastName("3333")
            .setMobilePhone("4444"));

    Assertions.assertEquals(
        contactsHolder.getContacts(contact -> contact.getFirstName().contains("1")).toString(),
        contactsHolder.getContacts().toString());
    Assertions.assertEquals(
        contactsHolder.getContacts(contact -> contact.getFirstName().contains("11")).toString(),
        contactsHolder.getContacts(contact -> contact.getSecondName().contains("22")).toString());
    Assertions.assertEquals(
        contactsHolder.getContacts(contact -> contact.getSecondName().contains("222")).toString(),
        contactsHolder.getContacts(contact -> contact.getLastName().contains("333")).toString());
    Assertions.assertEquals(
        contactsHolder.getContacts(contact -> contact.getLastName().contains("3333")).toString(),
        contactsHolder.getContacts(contact -> contact.getMobilePhone().contains("4444")).toString());

  }

  @Test
  void saveTest() {
    ContactsHolder contactsHolder = new ContactsHolder();
    Builder newContact = new Builder().setFirstName("1").setSecondName("2").setLastName("3")
        .setMobilePhone("4");
    contactsHolder.addContact(newContact);
    contactsHolder.addContact(
        newContact.setFirstName("11").setSecondName("22").setLastName("33").setMobilePhone("44"));
    contactsHolder.addContact(newContact.setFirstName("111").setSecondName("222").setLastName("333")
        .setMobilePhone("444"));
    contactsHolder.addContact(
        newContact.setFirstName("1111").setSecondName("2222").setLastName("3333")
            .setMobilePhone("4444"));

    ContactsHolder newHolder = new ContactsHolder();
    newHolder.loadData();

    Assertions.assertEquals(contactsHolder.getContacts().toString(), newHolder.getContacts().toString());
  }
}
