<h1 align="center">Test app for it-company</h1>

<p align="left">
  <a href="https://kotlinlang.org"><img alt="Kotlin Version" src="https://img.shields.io/badge/kotlin-1.7.0-blue.svg"/></a>
  <a href="https://developer.android.com/studio/releases/platforms#8.0"><img alt="API 26" src="https://img.shields.io/badge/API-26%2B-brightgreen"/></a>
  <a href="https://developer.android.com/studio/releases/platforms#13"><img alt="API 33" src="https://img.shields.io/badge/API-33-brightgreen"/></a>
</p>


<p align="left">  
This test application was developed for it-company to apply for a vacancy. It was created from figma view thats why where aren't any notifications, animations and so on. Looks well only in Light Mode.
To start this app quckly, I provide some test data, you can upload "test_data.json" into your FireBase Realtime database. 
To download or upload files, you have to create FireBase Storage with special hierarchy. For uploading files create: "/users/companies/[company owner from task]/documents/[task id]". 
Path paraments you can get from test_data.json file. For donwloading files hierarchy looks quite the same:\n
1. for licence "/users/companies/[company owner from task]/licences/[task id]" and don't forget to put here a file with name "test.pdf".
2. for user logo create "/users/employees/user/photo" and don't forget to put here a file with name "test.jpg".
</p>
</br>

<p align="center">
<img src="readme_assets/Screenshot_1.png" width="200">
<img src="readme_assets/Screenshot_2.png" width="200">
<img src="readme_assets/Screenshot_3.png" width="200">
<img src="readme_assets/Screenshot_4.png" width="200">
<img src="readme_assets/Screenshot_5.png" width="200">
<img src="readme_assets/Screenshot_6.png" width="200">
<img src="readme_assets/Screenshot_7.png" width="200">
<img src="readme_assets/Screenshot_8.png" width="200">
<img src="readme_assets/Screenshot_9.png" width="200">
<img src="readme_assets/Screenshot_10.png" width="200">
<img src="readme_assets/Screenshot_11.png" width="200">
<img src="readme_assets/Screenshot_12.png" width="200">
<img src="readme_assets/Screenshot_13.png" width="200">
</p>
  
### Architecture
  * Clean Architecture
  * MVVM (Model - View - ViewModel)

### Technology Stack
  * [Kotlin](https://kotlinlang.org/)
  * [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
  * [FireBase](https://firebase.google.com/)
  * [Android Jetpack](https://developer.android.com/jetpack)
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
    * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
	* [ConstraintLayout](https://developer.android.com/training/constraint-layout)
	* [Architecture Components](https://developer.android.com/topic/libraries/architecture)
    * [Navigation](https://developer.android.com/guide/navigation)
    * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
  * [Retrofit 2](https://square.github.io/retrofit/)
  * [GSON](https://github.com/google/gson)
  * [Material-Components](https://github.com/material-components/material-components-android)
  * [Glide](https://bumptech.github.io/glide/)