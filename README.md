
### Build instructions

I use several code components from another open source projects. You nee dto obtain their before building.

#### Markdown to html javascript (app/src/main/assets/js/marked.min.js)

##### download source:

`git clone https://github.com/chjj/marked`

File (marked.min.js) in project root directory.

#### Source code highlighter javascript (app/src/main/assets/js/highlight.pack.js)

##### download source:

`git clone https://github.com/isagalaev/highlight.js`

##### [build instructions](http://highlightjs.readthedocs.io/en/latest/building-testing.html#building)

##### my build command:

`nodejs tools/build.js browser :common`

The result (highlight.pack.js) in the build/ subdirectory.

#### Material Design icons (app/src/main/assets/fonts/google-material/MaterialIcons-Regular.woff2)

##### download source

`git clone http://github.com/google/material-design-icons/`

Font file (MaterialIcons-Regular.woff2) in the iconfont/ subdirectory.

