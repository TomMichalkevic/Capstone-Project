# See Vilnius

This application was created as part of a Nanodegree at Udacity. The app displays museums that are present at maximum 5km(changeable from settings) from your current position. When the list is populated with POIs, you can click on a specific location and see its details such as opening times and reviews. From the main menu you can also see bus stops in the distance of 1km based on Trafi data. Please do note that the application is made with Vilnius/Lithuania in mind so it will work best there. However, no functionality was restricted to Vilnius so potentially wherever you have museums in your area and if Trafi supports that area - you can use the app.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the app

```
1. Install Android Studio on your machine. Versions for Linux, Windows and macOS are available from here https://developer.android.com/studio/
2. Create a new emulator for a Google Pixel 2 with SDK version 27 or have a physical device on hand
3. If you are using a physical device, enable ADB debugging on your device. Refer to official documentation on how to install do it
```

### Installing

A step by step series of examples that tell you how to get a development env running

Cloning the project

```
1. Navigate to a location on your machine's file system, where you want to clone the project using a terminal or command line
2. Run this command to clone the master branch https://github.com/TomMichalkevic/Capstone-Project.git
3. Open your Android Studio and open the project from the project root you have just created by cloning
```

Setting up the project

```
1. In the root of the project create a file called secrets.properties and create the contents as decribed in the section below
2. Run gradle sync on the build.gradle in the app folder
3. When either an emulator or a physical device is connected, run installRelease task
4. Now you have a working instance the app
```

### Creating the secrets.properties
The file has to be created in the root of the projec. Its contents need to be as follows:
```
GOOGLE_API_KEY="INSERT_YOUR_GOOGLE_API_KEY"
TRAFI_API_KEY="INSERT_YOUR_TRAFI_API_KEY"
```

The keys can be requested at:
```
https://cloud.google.com/
https://developer.trafi.com/
```

## Running the tests

No tests are defined at this point

## Built With

* [Gradle](https://gradle.org) - Dependency management and building

## Contributing

Please read [CONTRIBUTING.md](https://github.com/TomMichalkevic/Capstone-Project/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/TomMichalkevic/Capstone-Project/tags). 

## Authors

* **Tomas Michalkevic** - *Initial work* - [See Vilnius](https://github.com/TomMichalkevic/Capstone-Project)

See also the list of [contributors](https://github.com/TomMichalkevic/Capstone-Project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [official resource](https://opensource.org/licenses/MIT) for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
