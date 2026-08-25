package com.pickyeaters.logic.view;

public interface ViewSubject {
    void addObserver(ViewObserver observer);
    void removeObserver(ViewObserver observer);
    void notifyAllObserver();
}
