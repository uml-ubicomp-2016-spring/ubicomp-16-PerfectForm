# ubicomp-16-PerfectForm
Perfect Form Ubicomp Project

Update #0 This app will overall help the runner from preparing for your run until the run is over. The app will help the runner figure out what to wear based on the weather amongst other tasks. Ideas so far

    Light sensor will warn user too late to run or to wear refelective material
    Step sensor will count steps per minute (180 steps/min is the recommended amount for a jog pace)
    Weather/Location services to help tell runner decide what to wear
    Gyroscope to detect if arms are parrallel to body
    Accelerometer readings will be used to detect if the user is exerting too much energy with needless arm motion

Pre-Update #1 Ronald Manganaro: working on light sensor Paul: Working on weather service to download JSON data Curtis: pending Bobby: light sensor.

Update #1 4/4: Initial support added for weather and light sensor. We expected to get more done but it took a lot of time for members to become more comfortable with the android environment. Following updates will see more content.

Update #2 Working further on weather/location services. Connecting the sensor classes into prototype app. Collecting test data to and analyzing to find patterns of good/bad behavior. 

Update #3 As of now the we have created a prototype of the app consisting of almost all of the activities that will be used in the app. Still working on accelerometer and gyroscope reading analysis. Weather services portion are almost finished so that the app will be able to make suggestions on when the person should run and what the person should wear for that day. This will be based off of the temperature, wind force, weather conditions, and what time the sun will set/rise. Next update will use steps/minute to find out if the user is running at a good pace (the average should be 180 steps/minute to avoid injury and to ensure better endurance). We have changed the main idea of out app from being all about running with better form to add other features that would help encourage a user to run. The audience is not for expert runners but for people who want to get into the habit of running and need a little push here and there as well as some advice.

Update #4 Should be able to fetch weather information and use it in the app. Will also have added changes to the sampling rate for the accelerometer as well as a timer so that can decide how long to record data. Will have a metric for deciding whether or not someone is running with good form. Will be using the stepcounter provided by android api to see if the runners pace is too slow or too fast as well.

update #5 Weather now works when correct zip code is entered. The weater screen will show an icon depeneding on the weather conditions in the area. Working on fixing the sunrise and sundown time. Worked out metrics for whether the runner is in good form or not. We came up with some bounds the users accelerometer readings should follow based on test data taken from people who have been running for years versus people who are new to running. Will formalize these metrics before the projects end. Added a points system so the user can get an idea of how well they ran. Will add more precise feedback in the followuing week.
