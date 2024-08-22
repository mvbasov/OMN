Title: Welcome! (legacy mode)
Date: 2018-08-24 10:16:34
Modified: 2019-05-31 22:50:43
Author: Mikhail Basov
Tags: OMN documentation

[I don't want to read the documentation! Create my own page now!](../Start.html)

<div id="TOC"></div>

### General information.

This is application to create and organize everyday notes.

It can be useful to simple web development on Android mobile devices.

#### Key features
* **Simple.** See [simplest usage case](#this-is-easy)
* **Lightweight.** Package size less then 500Kb
* **[Open source.](https://github.com/mvbasov/OMN)**
* **Features rich.** 
  * Markdown, HTML, CSS and JavaScript can be used.
  * Pages can be organized by tags and folders
  * Pages can be [exported by E-Mail or Telegram](/default/Help.html#send-page-by-e-mail) for example
  * The application can be [target for sharing](#OMN-as-share-target) from another applications
  * URL bookmarks can be organized on [Incoming bookmarks](/bookmarks/Incoming.html)
  * [Search](/default/MarkdownExt.html#add-search-to-page) available on the page
  * Optional support [Pelican Content Management System (Pelican CMS)](https://blog.getpelican.com) Markdown extension for page header

To see <a href="Help.html#btn-desc">action buttons</a> press on the page title.

The program store it's files in application private directory. The directory path can be found near the bottom of [this page](Build.html). It allow to operate without storage access permission. But be <span class="fg-red">carefully!</span> If you use Android 5.0 or above and deinstall the application this directory and it's content will be removed.

- - -

<a id="this-is-easy" />

### Simplest usage case
You can create quick notes.
Press on <button onclick="Android.quicknoteButtonCallback()"> &#x1f3c3; </button> button on any page header and enter text of new note then press "Ok".

Notes organized as stream on one page.
Your new note will be placed at the top of the [QuickNotes page](../QuickNotes.html) with time stamp before.
To quick access to your notes you can create shortcut at the home screen by pressing <button onclick="Android.shortcutButtonCallback(PFN, Title)"> &#x2197; </button> button on the QuickNotes page header.

If you are using the application on Android 8.0 (Oreo) you can long press the application icon on the home scree and then choice "Take QuickNotes" in the menu that appears.

- - -

<a id="main-usage" />

### Main usage case
#### More about QuickNotes
Also you can share some text from another application. The quick note dialog will be fired in this case.

<a id="OMN-as-share-target"/>

#### OMN as target for `Share...` from other applications
If you use the OMN as target for `Share` from another application OMN will receive this information.
* From K9-Mail: if someone send you a page from OMN by E-Mail (K9-Mail tested) you OMN will receive this page and import if to [Incoming pages](/incoming/Incoming.html)
* The same, using Telegram.
* If you share some currently opened page from Web Browser (Google Chrome tested) OMN will receive the page information and save it to special [Incoming bookmarks](/bookmarks/Incoming.html) page. More information about this page [here](/default/BookmarkerHelp.html)

#### External editor
This program use external editor. If you try to edit page and get message about no editor found install one, please.
On Android older then 8.0 (Oreo) suitable almost any editor. 

I would recommend install the following editor and file manager to use with this application:
* [Simple generic text editor (F-Droid)](https://f-droid.org/app/org.billthefarmer.editor)
* [920 Text Editor (Google Play Market)](https://play.google.com/store/apps/details?id=com.jecelyin.editor.v2)
* [OI Notepad (Google Play Market)](market://details?id=org.openintents.notepad)
* [X-plore File Manager (Google Play Market)](market://details?id=com.lonelycatgames.Xplore)
* [QuickEdit Text Editor (Google Play Market)](market://details?id=com.rhmsoft.edit)

On Android 8.0 and newer the following editors tested:
* [Simple generic text editor (F-Droid)](https://f-droid.org/app/org.billthefarmer.editor)
* [QuickEdit Text Editor (Google Play Market)](market://details?id=com.rhmsoft.edit)
* [Total Commander](market://details?id=com.ghisler.android.TotalCommander)

#### Create new page
**Don't edit this and other default pages! All you changes will be lost at application version update time.**
Before making your own pages set "Author name" in [application settings](/default/Help.html#app-settings).
At the first, create own start page. To do this visit [this link](../Start.html) and page will be created automatically. Useful links will be placed on the new start page.

Read about how to [Add new pages](Help.html#add-new-page) by action button <button onclick="BLOCK_Android.newPageButtonCallback()"> &#x2795; </button>.

Another way is to put link to new page on current page and then visit it. On this case link to another page is page name followed by `.html`
Pages can be formatted using [Markdown syntax](Markdown.html) and
[Extended Markdown syntax](MarkdownExt.html) specific for this application.

Notes stored in [Markdown](Markdown.html) format and shown as HTML. New HTML page creates only if it does not exists or corresponding markdown page has later modification timestamp.

You can organize your pages by putting [tags](Help.html#help-tags) to page [Markdown header](Help.html#page-header)

- - -

<a id="for-true-geeks" />

### Advanced usage case
Read this page and [the help page](Help.html)
Treat the application as web server with static content (without server side logic) and browser.

If you're referring to a local page, you must use relative paths:

    [My start page](../Start.html)

More information about links format can be found [here](MarkdownExt.html#links)

Place you pages under md/ directory, JavaScripts under js/ and styles under css/ or embed all of these in page directly. 

The Telegram channel with news about OMN and examples of pages [created](https://t.me/OMNNewsAndTips)

#### Application files tree

``` text
/
├─ css/
│  ├─ Bookmarker.css
│  ├─ PSearch.css
│  ├─ common.css
│  ├─ custom.css (**)
│  └─ highlight.css (**)
├─ fonts/
│  └─ google-material/
│     ├─ material-icons.css
│     └─ MaterialIcons-Regular.woff2
├─ html/ (*)
├─ js/
│  ├─ Bookmarker.js
│  ├─ PSearch.js
│  └─ functions.js
├─ md/
│  ├─ default/
│  │  ├─ BookmarkerHelp.md
│  │  ├─ Build.md (*)
│  │  ├─ Changelog.md
│  │  ├─ Help.md
│  │  ├─ Markdown.md
│  │  ├─ MarkdownExt.md
│  │  └─ Welcome.md
│  ├─ incoming/ (*)
│  │  └─ Incoming.md (**)
│  ├─ bookmarks/ (*)
│  │  ├─ BookmarkTags.md (**)
│  │  └─ Incoming.md (**)
│  ├─ QuickNotes.md (*)
│  ├─ Start.md (**)
│  └─ Tags.md (*)
└─ .ts (*)

(*) modified at operation time
(**) customizable
```
The / is the data directory which can be found near the bottom of [this page](Build.html).


<a id="auto-pages" />

#### Automatically modified pages and files

All pages in the directory ```md/default/*``` can be totally rewrite by application automatically. At version update time, usually.
The same is true for the following files:

```
css/Bookmarker.css
css/PSearch.css
css/common.css
css/highlight.css
fonts/google-material/MaterialIcons-Regular.woff2
fonts/google-material/material-icons.css
js/Bookmarker.js
js/PSearch.js
js/functions.js
```
All other pages and files are you own files and never been touched by program without your intention.

If Pelican CMS header meta information ```modified``` exists on page it updated automatically after editor call and page modified.

<a id="remote-acces" />

#### Access notes from other device

To see this application notes on another device (desktop or tablet, for example) I would recommend my small lightweight and simple web server for Android:

* [lightweight Web Server (lWS)](http://play.google.com/store/apps/details?id=net.basov.lws.r)

It was specially designed as companion application for these notes. To configure lWS go to its settings and set as document root OMN data directory. Data directory can be found near the bottom of [this page](Build.html).

- - -

### License

The application licensed under MIT License. Copyright (c) 2017-2019, Mikhail Basov

marked.js is released under MIT License. Copyright (c) 2011-2018, Christopher Jeffrey

Highlight.js is released under the BSD License. Copyright (c) 2006, Ivan Sagalaev   

- - -

### Contribution

I develop this application for my own needs but will be glad if it useful for someone.

You can use the application and it's [source code](https://github.com/mvbasov/lWS) according to MIT license.

If you wish to contribute your efforts to the project I am welcome it. Open issues on source page or [E-Mail me](mailto:OMN@basov.net). The first and main thing required to the project is to translate documentation pages from my English to common used English.

<script>
  window.onload=createTOC();
</script>

<link rel="stylesheet" type="text/css" href="../../css/PSearch.css" />
<script type="text/javascript" src="../../js/PSearch.js"></script>

