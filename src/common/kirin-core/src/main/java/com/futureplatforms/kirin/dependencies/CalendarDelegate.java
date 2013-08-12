package com.futureplatforms.kirin.dependencies;

import java.util.Date;

public interface CalendarDelegate {
    public void createEvent(Date start, Date end, String name);
}
