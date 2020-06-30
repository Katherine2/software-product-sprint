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
    int counter = 0;
    int maxHours = TimeRange.WHOLE_DAY.duration();
    long requestedDuration = request.getDuration();
    int startTime1 = -1;
    int endTime1 = -1;
    int startTime2 = -1;
    int endTime2 = -1;

    if(request.getAttendees().isEmpty()){           //if there are no attendees in the request
        return Arrays.asList(TimeRange.WHOLE_DAY);  //the event can be scheduled at any time of the day
    }
    else if(request.getDuration() > maxHours){           //if the duration of the meeting exceeds the amount of time in a day
        return Arrays.asList();                     //there are no possible meeting times
    }
    else if(!events.isEmpty()){
        for (Event event: events){
            counter ++;
            if (counter == 1){                      //on the first event
                TimeRange time = event.getWhen();   //get the time interval the attendee has a meeting  
                startTime1 = time.start();          //get the start time of the meeting
                endTime1 = time.end();              //get the end time of the meeting
                
            }
            else if (counter == 2){
                TimeRange time = event.getWhen();   //get the time interval the attendee has a meeting  
                startTime2 = time.start();          //get the start time of the meeting
                endTime2 = time.end();              //get the end time of the meeting
            }
        }
        if (counter == 1){                                          //if there is only one event
            if((startTime1 - START_OF_DAY)>= requestedDuration){    //if there is enough time for the requested meeting to be before the already scheduled meeting
                if((END_OF_DAY - endTime1)>= requestedDuration){    //if there is enough time for the requested meeting to be after the already scheduled meeting
                    //return the times from the start of the day until the start of the scheduled meeting (excluding the actual start time) and the times from the end of the scheduled meeting to the end of the day
                    return Arrays.asList(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, startTime1, false), TimeRange.fromStartEnd(endTime1, TimeRange.END_OF_DAY, true));
                }
            }
        }
        else if (counter == 2){                                                 //if there are 2 events                
            if(startTime1 < startTime2){                                       //if the start time of the first meeting is before the start time of the second meeting
                if((startTime1 - START_OF_DAY) >= requestedDuration){           //if there is enough time for the requested meeting to be before the first already scheduled meeting
                    if(endTime2 > endTime1){                                    //if the end time of the second meeting is after the end time of the first meeting
                        if((END_OF_DAY - endTime2) >= requestedDuration){           //if there is enough time for the requested meeting to be after the second already scheduled meeting
                            if((startTime2 - endTime1)>= requestedDuration){        //if there is enough time between the 2 meetings
                                //return the times from the start of the day until the start of the first scheduled meeting (excluding the actual start time) 
                                //return the times from the end of the first scheduled meeting until the start of the second scheduled meeting (excluding the actual start time) 
                                //and the times from the end of the second scheduled meeting to the end of the day
                                return Arrays.asList(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, startTime1, false), TimeRange.fromStartEnd(endTime1, startTime2, false), TimeRange.fromStartEnd(endTime2, TimeRange.END_OF_DAY, true));
                            }
                            else if((startTime2 - endTime1)<= requestedDuration){   //if there is not enough time between the 2 meetings or if the 2 meetings are overlapping
                                //return the times from the start of the day until the start of the first scheduled meeting (excluding the actual start time) and the times from the end of the second scheduled meeting to the end of the day
                                return Arrays.asList(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, startTime1, false), TimeRange.fromStartEnd(endTime2, TimeRange.END_OF_DAY, true));
                            }
                        }
                    }
                    else{   //if the endTime of the second meeting is before the end time of the first meeting
                        //return from the start of the day to the start of the first meeting and from the end of the frist meeting to the end of the day
                        return Arrays.asList(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, startTime1, false),TimeRange.fromStartEnd(endTime1, TimeRange.END_OF_DAY, true));
                    }
                }
                //if there is not enough room from the start of the day to the start of the first scheduled meeting
                //if there is enough room between the end of the first scheduled meeting and the start of the second scheduled meeting
                else if((startTime2 - endTime1) >= requestedDuration) {
                    if((END_OF_DAY - endTime2) <= requestedDuration){       //if there is not enough room from the end of the second scheduled meeting and the end of the day 
                        return Arrays.asList(TimeRange.fromStartDuration(TIME_0830AM, DURATION_30_MINUTES));
                    }
                }
                else if((END_OF_DAY - endTime2) <= requestedDuration){      //if there is not enough room from the end of the second scheduled meeting and the end of the day 
                        return Arrays.asList();                             //there are no possible meeting times
                } 
            }
        }
    }
    return null;
  }
}
