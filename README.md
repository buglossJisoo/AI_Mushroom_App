# AI_Mushroom_App

## AI Mushroom App final code(JAVA)

### 0. SplashActivity

<img src = "https://user-images.githubusercontent.com/70942492/92484959-4e531180-f225-11ea-8aa8-88ae3cf8282d.png" width="20%" height="20%">



### 1. MainActivity (+ SubActivity)
#### MainActivity is comprised of five buttons.

* Button 1. Take the photo of mushroom
* Button 2. Five Mushrooms Quizzes
* Button 3. Korea Mushroom Map
* Button 4. User Guide
* Button 5. Settings

* SubActivity is the notice about "AI Mushroom App".

<img src = "https://user-images.githubusercontent.com/70942492/92482213-53fb2800-f222-11ea-8af9-e4c211962b23.png" width="20%" height="20%">



### 2. CameraMushActivity (+ ProductDBHelper + classifier.imageClassifier)

<img src = "https://user-images.githubusercontent.com/70942492/92488748-eb17ae00-f229-11ea-945d-ffbdcded7b14.PNG" width="20%" height="20%">

If you click this button, CameraMushActivity can be shown below.

<img src = "https://user-images.githubusercontent.com/70942492/92488713-dfc48280-f229-11ea-88f4-7d5f83525fc2.PNG" width="20%" height="20%">

If you click "사진 업로드(Upload the photo)", there's two options you can choose.

First, "사진 촬영(Take the photo)" button is literally to take the mushroom photo.

Second, "앨범 선택(Select album)" button is to select photo from your albums.
 
App will work and be shown mushroom's information below the button after you select or take the photo(mushroom).

* We are using the movilenetV2 model for tensorflow-imageClassification with Colab using Google Gpu.
* ProductDBHelper is about DB for SQLite.(Mushroom DB has name, symptoms, type of mushrooms and its similar mushrooms's name.)
* We convert .h5 to .pb to .tflite and quatize this tflite file to NEW quantizied .tflite.(NEW quantizied tflite is run on Android.)



### 3. QuizMushActivity

<img src = "https://user-images.githubusercontent.com/70942492/92485905-77c06d00-f226-11ea-9b90-152b7ff4cee2.PNG" width="20%" height="20%">

If you click this button, QuizMushActivity can be shown below.

<img src = "https://user-images.githubusercontent.com/70942492/92485925-7db64e00-f226-11ea-9fa7-e1a3c1b26d01.PNG" width="20%" height="20%">

There's five quizes about mushrooms.

You can keep taking mushroom quiz even if your answer is wrong.

* O icon means True, X icon means False.




### 4. MapActivity

<img src = "https://user-images.githubusercontent.com/70942492/92486003-9292e180-f226-11ea-987f-f06de25c00c2.PNG" width="20%" height="20%">

If you click this button, MapActivity can be shown below.

<img src = "https://user-images.githubusercontent.com/70942492/92486020-9888c280-f226-11ea-9ad7-2828e55293ab.png" width="20%" height="20%">

Your location is marked on this map with rec circle icon. 

Loaction of photo taken using this app will be marked with red gps icon.(NOT COMPLETE YET)




### 5. InstructionActivity

<img src = "https://user-images.githubusercontent.com/70942492/92486095-ae968300-f226-11ea-9e0a-54cd47f7cb35.PNG" width="20%" height="20%">

If you click this button, InstructionActivity can be shown below.

<img src = "https://user-images.githubusercontent.com/70942492/92486142-b6eebe00-f226-11ea-8174-38bbdefc7d3e.PNG" width="20%" height="20%">

This activity is instruction of AI Mushroom App for beginners.



### 6. SettingsActivity

<img src = "https://user-images.githubusercontent.com/70942492/92486182-c241e980-f226-11ea-95b4-891bbfd0b907.PNG" width="20%" height="20%">

If you click this button, SettingsActivity can be shown below.

<img src = "https://user-images.githubusercontent.com/70942492/92486211-c968f780-f226-11ea-9f0d-533bd6f96b5a.PNG" width="20%" height="20%">

This activity has authorization for using camera, album, location.

You can control permission of these items and font size on app. 




Copyrightⓒ2020 AI Mushroom team All rights reserved.
