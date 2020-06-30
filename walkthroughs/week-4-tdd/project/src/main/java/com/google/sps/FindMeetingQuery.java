// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
  private static final Collection<Event> NO_EVENTS = Collections.emptySet();
  private static final Collection<String> NO_ATTENDEES = Collections.emptySet();

  // Some people that we can use in our tests.
  private static final String PERSON_A = "Person A";
  private static final String PERSON_B = "Person B";

  // All dates are the first day of the year 2020.
  public static final int START_OF_DAY = TimeRange.getTimeInMinutes(0, 0);
  public static final int END_OF_DAY = TimeRange.getTimeInMinutes(23, 59);
  private static final int TIME_0800AM = TimeRange.getTimeInMinutes(8, 0);
  private static final int TIME_0830AM = TimeRange.getTimeInMinutes(8, 30);
  private static final int TIME_0900AM = TimeRange.getTimeInMinutes(9, 0);
  private static final int TIME_0930AM = TimeRange.getTimeInMinutes(9, 30);
  private static final int TIME_1000AM = TimeRange.getTimeInMinutes(10, 0);
  private static final int TIME_1100AM = TimeRange.getTimeInMinutes(11, 00);

  private static final int DURATION_30_MINUTES = 30;
  private static final int DURATION_60_MINUTES = 60;
  private static final int DURATION_90_MINUTES = 90;
  private static final int DURATION_1_HOUR = 60;
  private static final int DURATION_2_HOUR = 120;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //throw new UnsupportedOperationException("TODO: Implement this method.");
    int maxHours = TimeRange.WHOLE_DAY.duration() ;

    if(request.getAttendees().isEmpty()){           //if there are no attendees in the request
        return Arrays.asList(TimeRange.WHOLE_DAY);  //the event can be scheduled at any time of the day
    }
    else if(request.getDuration() > maxHours){           //if the duration of the meeting exceeds the amount of time in a day
        return Arrays.asList();                     //there are no possible meeting times
    }
    else if(!events.isEmpty()){
        //System.out.println(TIME_0830AM);
        for (Event event: events){
            TimeRange time = event.getWhen();
            int startTime = time.start();
            int endTime = time.end();
            //System.out.println(startTime);
            //System.out.println(endTime);
            if((startTime - START_OF_DAY)>= 30){
                if((END_OF_DAY - endTime)>=30){
                    return Arrays.asList(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, startTime, false), TimeRange.fromStartEnd(endTime, TimeRange.END_OF_DAY, true));
                }
            }
        }
    }
    return null;
  }
}
