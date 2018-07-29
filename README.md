# OvertimeTracker 

![Build Status](https://travis-ci.org/vmpay/OvertimeTracker.svg?branch=master)

Android app for counting single calendar events duration

If you are recording your working hours in your mobile calendar, this application is for you!
It allows to sum all single events duration with the similar name. Open the application and 
choose the proper calendar. You can also specify date range to sum up duration. For example,
if you are recording overtime hours like "overtime" the app shows the total hours of overtime during specified period of time.

| calendar view  | overtime view |
| ------------- | ------------- |
| ![calendar view](https://github.com/vmpay/OvertimeTracker/blob/master/screenshots/calendar.png)  | ![overtime view](https://github.com/vmpay/OvertimeTracker/blob/master/screenshots/overtime.png) |

This android application requires Calendar permission to access your calendar.
The MVVM architecture app uses rxjava 2 and Android Architecture Components.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install:

* Android development IDE such as [Android Studio](https://developer.android.com/studio/index.html)
* Android device or emulator with [Android 	JELLY_BEAN_MR1](https://developer.android.com/reference/android/os/Build.VERSION_CODES#JELLY_BEAN_MR1). You can see  how to create an emulator in Android Studio on the [official site](https://developer.android.com/studio/run/managing-avds.html)

### Installing

Clone the OvertimeTracker repo from github from the console

```
git clone https://github.com/vmpay/OvertimeTracker.git
```

or download it manually.

After that open this repo from your IDE.

OR

Download from [release section](https://github.com/vmpay/OvertimeTracker/releases) and install on your android device.

## Built With

* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/) - A collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence
* [RxJava](https://github.com/ReactiveX/RxJava) - a library for composing asynchronous and event-based programs by using observable sequences
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) - This module adds the minimum classes to RxJava that make writing reactive components in Android applications easy and hassle-free. More specifically, it provides a Scheduler that schedules on the main thread or any given Looper
* [RxPermissions](https://github.com/tbruyelle/RxPermissions) - This library allows the usage of RxJava with the new Android M permission model
* [MaterialDateRangePicker](https://github.com/borax12/MaterialDateRangePicker) - A material Date Range Picker based on [wdullaers MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker)

## Authors

* **Andrew** - *Initial work* - [vmpay](https://github.com/vmpay)

See also the list of [contributors](https://github.com/vmpay/OvertimeTracker/graphs/contributors) who participated in this project.

## License

This project is licensed under the Apache License 2.0 License - see the [LICENSE](LICENSE) file for details

