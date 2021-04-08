package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.edu.phone.Models.Contact;
import ru.hse.edu.phone.Models.Contact.Builder;
import ru.hse.edu.phone.Models.ContactsLoader;

public class ContactsLoaderTest {

  @Test
  void phoneSerialization() {
    ContactsLoader contactsLoader = new ContactsLoader();

    ArrayList<Contact> contacts = new ArrayList<>();

    contacts
        .add(new Contact.Builder().setFirstName("FirstName").setSecondName("SecondName").build());
    contacts.add(new Builder().build());
    contacts.add(
        new Builder().setFirstName("FirstName").setSecondName("SecondName").setLastName("LastName")
            .setMobilePhone("8 800").setHomePhone("8900").setAddress("Pushkin Street")
            .setBirthday(LocalDate.now()).setComment("Hello world").build());
    contactsLoader.exportContacts(contacts, "Tests/resources/data.contacts");

    Assertions
        .assertEquals(contactsLoader.importContacts("Tests/resources/data.contacts").toString(),
            contacts.toString());
  }
}
