package ru.hse.edu.phone.Models;

import java.util.EventListener;

public interface ValidationListener extends EventListener {
  void onValidationEvent(ValidationEvent event);
}
