package de.milke.ecost.service;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
public class ReminderGenerationService {

    @Schedule(minute = "*/3", hour = "*")
    public void checkReminder() {

    }
}
