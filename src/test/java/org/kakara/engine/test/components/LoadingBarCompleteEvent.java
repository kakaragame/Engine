package org.kakara.engine.test.components;

import org.kakara.engine.ui.events.UActionEvent;

public interface LoadingBarCompleteEvent extends UActionEvent {
    /*
        To make an event for a component make an interface that extends UActionEvent.
        Then make ONE method that will be your event method (More than one methods are not supported.)
        You can put any parameters in method as you want. In this example I am putting a percent parameter.
        (Yes the parameter is useless, This is just for an example).
     */
    void LoadingBarCompleteEvent(float percent);
}
