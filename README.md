[![](https://jitpack.io/v/ssseasonnn/KeyBoardDetector.svg)](https://jitpack.io/#ssseasonnn/KeyBoardDetector)

# KeyBoardDetector

### How to use

Step 1. Add the JitPack repository to your build file
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```gradle
dependencies {
    // replace xyz to latest version number
    implementation 'com.github.ssseasonnn:KeyBoardDetector:xyz'
}
```

Step 3. Usage


```kotlin
val keyBoardDetector = KeyBoardDetector(this)
keyBoardDetector.keyboardLiveData.observe(this, Observer { height->

    //use the key board height
})
```


### License

> ```
> Copyright 2019 Season.Zlc
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```
