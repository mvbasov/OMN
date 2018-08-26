
### General information.
This is simple, lightweight and open source application to create and organize everyday notes. Notes stored in Markdown format and shown as html. New html page create if it does not exists or corresponding markdown page has later modification time stamp.

**This program use external editor.** ~~Suitable almost any editor.~~
At this time only editors with FileProvider support suitable, for example: [QuickEdit Text Editor - Writer, Code Editor](https://play.google.com/store/apps/details?id=com.rhmsoft.edit). (Because Google reqired target API level minimum 26. It require using FileProvider witth content:// data scheme.)

Pelican CMS meta information support can be enabled in preferences screen.

I design this application to my needs and use it everyday. I will be glad if this application meets anybody needs also.

### Version scheme

v**RR**.**BB**.**FF**[r|b|a]

* **RR** - minor version
* **BB** - beta version (00 for release)
* **FF** - fix for minor release

r - release
b - beta
a or empty - alpha

#### For versions before v00.21.00b

if application had legacy version:

**FF**=01 mark release legacy release build

**FF**=02,04... mark fix for normal version
**FF**=03,05... mark fix for legacy version

### Note about source under android/support directory

I dont want to add whole Android support library to my project because it will increase application size to more then 600 kB. For application with original size about 200 kB it is wasting of resource. To solve this problem required fragments of source from Android support library included to my source directly.

Support library sources obtained there:

```
git clone https://android.googlesource.com/platform/frameworks/support/
from commit 927a6a5cd2e68973cbca9a676a974aedb259f759

annotations/src/main/java/android/support/annotationGuardedBy.java
annotations/src/main/java/android/support/annotationlist
annotations/src/main/java/android/support/annotationNonNull.java
annotations/src/main/java/android/support/annotationNullable.java
core-utils/src/main/java/android/support/v4/content/FileProvider.java
```

Last file little bit patched:
```
--- FileProvider.java	2018-07-14 14:44:48.000000000 +0300
+++ FileProvider.java_my	2018-07-15 00:37:47.509604911 +0300
@@ -29,6 +29,7 @@
 import android.database.Cursor;
 import android.database.MatrixCursor;
 import android.net.Uri;
+import android.os.Build;
 import android.os.Environment;
 import android.os.ParcelFileDescriptor;
 import android.provider.OpenableColumns;
@@ -613,14 +614,22 @@
                 } else if (TAG_EXTERNAL.equals(tag)) {
                     target = Environment.getExternalStorageDirectory();
                 } else if (TAG_EXTERNAL_FILES.equals(tag)) {
-                    File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
-                    if (externalFilesDirs.length > 0) {
-                        target = externalFilesDirs[0];
+                    if (Build.VERSION.SDK_INT <= 18) {
+                        target = Environment.getExternalStorageDirectory();
+                    } else {
+                        File[] externalFilesDirs = context.getExternalFilesDirs(null);
+                        if (externalFilesDirs.length > 0) {
+                            target = externalFilesDirs[0];
+                        }
                     }
                 } else if (TAG_EXTERNAL_CACHE.equals(tag)) {
-                    File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
-                    if (externalCacheDirs.length > 0) {
-                        target = externalCacheDirs[0];
+                    if (Build.VERSION.SDK_INT <= 18) {
+                        target = Environment.getExternalStorageDirectory();
+                    } else {
+                        File[] externalCacheDirs = context.getExternalCacheDirs();
+                        if (externalCacheDirs.length > 0) {
+                            target = externalCacheDirs[0];
+                        }
                     }
                 }
 
```

Mentioned source code license and coperight: 
 * Copyright (C) 2013 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0

### Build instructions

I use several code components from another open source projects. You nee to obtain their before building.

#### Markdown to html javascript (app/src/main/assets/js/marked.min.js)

##### download source:

`git clone https://github.com/chjj/marked`

File (marked.min.js) in project root directory.

#### Source code highlighter javascript (app/src/main/assets/js/highlight.pack.js and app/src/main/assets/css/highlight.css)

##### download source:

`git clone https://github.com/isagalaev/highlight.js`

##### [build instructions](http://highlightjs.readthedocs.io/en/latest/building-testing.html#building)

##### my build commands:

`npm install`
`nodejs tools/build.js browser :common`

The result (highlight.pack.js) in the build/ subdirectory.
Copy build/highlight.pack.js as app/src/main/assets/js/highlight.pack.js
Copy build/demo/styles/github.css as app/src/main/assets/css/highlight.css

#### Material Design icons (app/src/main/assets/fonts/google-material/MaterialIcons-Regular.woff2)

##### download source

`git clone http://github.com/google/material-design-icons/`

Font file (MaterialIcons-Regular.woff2) in the iconfont/ subdirectory.
Dynamic chortcut (drawable/ic_baseline_comment_24px.xml)for QuickNote based on communication/svg/production/ic_comment_24px.svg

#### Solutions from StackOverflow used in code
* [Escape JavaScript function parameters](https://stackoverflow.com/a/23224442)
* [Intent extra. Debug code](https://stackoverflow.com/a/15074150)
* [Install desktop shortcut](https://stackoverflow.com/a/16873257)
* [Generating TOC](https://stackoverflow.com/q/28792557)
 
