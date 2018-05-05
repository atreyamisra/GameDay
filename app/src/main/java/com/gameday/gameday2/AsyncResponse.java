/**
 * Written by: Pratyush Singh
 * Date: 4/26/18
 * This is the interface used to implement the observer pattern that will notify the Main UI Thread
 * once an activity is complete
 */
package com.gameday.gameday2;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<?> response);
}

