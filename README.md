# Base
A base project to build your app on. Contains common code base you're probably gonna need in every Android app, i. e. BaseFragment, BaseViewModel, common extensions, Dependency Injection with Koin, SharedPrefs/DataStore and more.

Add this to your app's build.gradle and code away!
```
 implementation 'com.github.minarja1:Base:$latest_version'
```
[![](https://jitpack.io/v/minarja1/Base.svg)](https://jitpack.io/#minarja1/Base)


## Third party libraries
### The following libraries and APIs are availale through Base:

[**Jetpack**](https://developer.android.com/jetpack/getting-started)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Room](https://developer.android.com/training/data-storage/room)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

**View**
- [SwipeRefreshLayout](https://developer.android.com/reference/androidx/swiperefreshlayout/widget/SwipeRefreshLayout)
- [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout)
- [Paging](https://developer.android.com/topic/libraries/architecture/paging)

**Network**
- [Retrofit](https://github.com/square/retrofit)
- [OkHttp](https://square.github.io/okhttp/)

**Images**
- [Coil](https://github.com/coil-kt/coil)

**JSON**
- [Moshi](https://github.com/square/moshi)

..and more

## Usage

### 1. Data persistence

#### 1.1. [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
Refer to class [DataStoreExtensions.kt](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/common/extensions/DataStoreExtensions.kt)
Example usage: 
```kotlin
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("DataStoreName")
    
    fun getShouldShowLongPressHint(): Flow<Boolean> {
        return context.dataStore.getBooleanData(SHOULD_SHOW_LP_HINT, true)
    }
```
