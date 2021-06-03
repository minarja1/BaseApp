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

#### 1.1. [Room](https://developer.android.com/training/data-storage/room)

##### 1.1.1 DAO
Refer to class [BaseDao.kt](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/data/db/dao/BaseDao.kt)
Example usage: 
```kotlin
@Dao
interface DummyDao : BaseDao<DummyEntity> {

    @Query("Select * From DummyEntity")
    suspend fun getAll(): List<DummyEntity>

    @Query("Select * From DummyEntity Where note Like :note")
    suspend fun getAllByNote(note: String): List<DummyEntity>

    @Query("Select * From DummyEntity Where id = :id")
    suspend fun getById(id: Int): DummyEntity?

    @Query("Select note From DummyEntity Where id = :id")
    suspend fun getNoteById(id: Int): String?

}
```
##### 1.1.2 Repository
Refer to class [BaseRepository.kt](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/di/base/BaseRepository.kt)
Example usage: 
```kotlin
class DummyRepository : BaseRepository() {

    private val dummyDao by inject<DummyDao>()

    /**
     * @sample get for REST calls
     */
    suspend fun getDummy() = dummyWebService.getDummyResponseList()

    /**
     * @sample load for DB calls
     */
    suspend fun loadDummy() = dummyDao.getAll()
}
```

##### 1.1.3 Migrations
Example usage: 
```kotlin
val Migration_x_to_y = object : Migration(11, 12) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("Alter Table CategoryEntity Add Column note Text Not Null Default ''")
        database.execSQL("Alter Table CategoryEntity Add Column categoryNumber Text Not Null Default '0'")
    }
}
```

#### 1.2. [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
Refer to file [DataStoreExtensions.kt](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/common/extensions/DataStoreExtensions.kt)

Example usage: 
```kotlin
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("DataStoreName")
    
    fun getShouldShowLongPressHint(): Flow<Boolean> {
        return context.dataStore.getBooleanData(SHOULD_SHOW_LP_HINT, true)
    }
```

#### 1.3. [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)
Refer to file [Preferences+Ext.kt](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/common/extensions/Preferences%2BExt.kt)

Example usage: 
```kotlin
   class PrefManagerImp(context: Context) : PrefManager(context) {

    companion object {
        const val shouldShowLongPressHint = "shouldShowLongPressHint"
    }
    
    var shouldShowLongPressHint by booleanPreference(shouldShowLongPressHint)
}
```


### 2. Network communicaiton
Refer to file [Modules.kt](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/di/Modules.kt)
Here, Retrofit and OkHttp are set up and ApiService classes initialized with Retrofit instances and base URLs.
Example:
```kotlin
DummyApiService(
            createRetrofit(
                createOkHttpClient(),
                "https://jsonplaceholder.typicode.com/"
            )
        )
```
The Service interface itself is very simple:
```kotlin
interface DummyApiService {

    companion object {
        operator fun invoke(retrofit: Retrofit): DummyApiService {
            return retrofit
                .create(DummyApiService::class.java)
        }
    }

    @GET("posts")
    suspend fun getDummyResponseList(): List<DummyResponse>

}
```

### 3. UI
#### 3.1. [Base Fragment](https://github.com/minarja1/Base/tree/master/app/src/main/java/cz/minarik/base/ui/base)
Simply pass the fragment's layout ID in the constructor:
```kotlin
class DummyFragment : BaseFragment(R.layout.fragment_dummy) {...}
```

And then call ``` protected fun <T> LiveData<T>.observe(function: (value: T) -> Unit)``` to observe LiveData
or   ```  protected fun <T> Flow<T>.collectWhenStarted(function: (value: T) -> Unit) ```  to collect Flows according to [this approac](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)

#### 3.2. [Base ViewModel](https://github.com/minarja1/Base/blob/master/app/src/main/java/cz/minarik/base/di/base/BaseViewModel.kt)
Extend the BaseViewModel class, i. e. like this:
```kotlin
class DummyViewModel(private val dummyRepo: DummyRepository) : BaseViewModel() {...}
```

