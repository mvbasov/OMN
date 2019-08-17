Title: Welcome!
Date: 2018-08-24 10:16:34
Modified: 2019-05-31 22:50:43
Author: Mikhail Basov
Tags: OMN default, Documentation

<style>
p {
text-indent: 1em;
margin: 0px 3px 0px 3px;
}
</style>

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
  * Pages can be exported by E-Mail for example
  * The application can be target for sharing from another applications
  * Optional support [Pelican Content Management System (Pelican CMS)](https://blog.getpelican.com) Markdown extension for page header

<span class="bg-yellow">To see <a href="Help.html#btn-desc">action buttons</a> press on the page title.</span>

- - -

<a id="this-is-easy" />

### Simplest usage case
You can create quick notes.
Press on <button onclick="Android.quicknoteButtonCallback()"> <i class="material-icons">comment</i> </button> buton on any page header and enter text of new note then press "Ok".

Notes organized as stream on one page.
Your new note will be placed at the top of the [QuickNotes page](../QuickNotes.html) with time stamp before.
To quick access to your notes you can create shortcut at the home screen by pressing <button onclick="Android.shortcutButtonCallback(PFN, Title)"> <i class="material-icons">screen_share</i> </button> buton on the QuickNotes page header.

If you are using the application on Android 8.0 (Oreo) you can long press the application icon on the home scree and then choise "Take QuickNotes" in the menu that appears.

- - -

<a id="main-usage" />

### Main usage case
#### More about QuickNotes
Also you can share some text from another application. The quick note dialog will be fired in this case.

If someone share URL in browser to E-Mail programm and send you this message you can share it message with OMNotes and give it as [list item](Markdown.html#help-lists)

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
Don't edit this page! All you changes will be lost at application version update time. At the first, create own start page. To do this visit [this link](../Start.html) and page will be created automatically. Useful links will be placed on the new start page.

Read about how to [Add new pages](Help.html#add-new-page) by action button <button onclick="BLOCK_Android.newPageButtonCallback()"> <i class="material-icons">add_box</i> </button>.

Another way is to put link to new page on current page and then visit it. On this case link to another page is page name followed by `.html`
Pages can be formatted using [Markdown syntax](Markdown.html) and
[Extended Markdown syntax](MarkdownExt.html) specific for this application.

Notes stored in [Markdown](Markdown.html) format and shown as html. New html page creates only if it does not exists or corresponding markdown page has later modification timestamp.

You can organize your pages by putting [tags](Help.html#help-tags) to page [Markdown header](Help.html#page-header)

- - -

<a id="for-true-geeks" />

### Advanced usage case
Read this page and [the help page](Help.html)
Treat the application as web server with static content (without server side logic) and browser.

If you're referring to a local page, you must use relative paths:

    [My start page](../Start.html)

More information about links format can be foud [here](MarkdownExt.html#links)

Place you pages under md/ directory, JavaScripts under js/ and styles under /css or embed all of these in page directly.. 

#### Application files tree

``` text
/
├─ css/
│  ├─ common.css
│  ├─ custom.css (**)
│  └─ highlight.css (**)
├─ fonts/
│  └─ google-material/
│     ├─ material-icons.css
│     └─ MaterialIcons-Regular.woff2
├─ html/ (*)
├─ js/
│  └── functions.js
├─ md/
│  ├─ default/
│  │  ├─ Build.md (*)
│  │  ├─ Changelog.md
│  │  ├─ Help.md
│  │  ├─ Markdown.md
│  │  ├─ MarkdownExt.md
│  │  └─ Welcome.md
│  ├─ incoming/ (*)
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

#### Automaticaly modified pages and files

All pages in the directory ```md/default/*``` can be totaly rewrite by application automaticaly. At version update time, usualy.
The same is true for thr following files:

```
css/common.css
css/highlight.css
fonts/google-material/MaterialIcons-Regular.woff2
fonts/google-material/material-icons.css
js/functions.js
```
All other pages and files are you own files and never been touched by programm without your intention.

If Pelican CMS header meta information ```modified``` exists on page it updated automaticaly after editor call and page modified.

<a id="remote-acces" />

#### Access notes from other device

To see this application notes on another device (desktop or tablet, for example) I would recommend my small lightweight and simple web server for Android:

* [lightweight Web Server (lWS)](http://play.google.com/store/apps/details?id=net.basov.lws.r)

It was specially designed as companion application for these notes. To configure lWS go to its settings and set as document root OMN data directory. Data directory can be found near the bottom of [this page](Build.html).

- - -

### License

The app licensed under MIT License. Copyright (c) 2017-2019, Mikhail Basov

marked.js is released under MIT License. Copyright (c) 2011-2018, Christopher Jeffrey

Highlight.js is released under the BSD License. Copyright (c) 2006, Ivan Sagalaev   

- - -

### Contribution

I develop this application for my own needs but will be glad if it useful for someone.

You can use the application and it's [source code](https://github.com/mvbasov/lWS) according to MIT license.

If you wish to contribute your efforts to the project I am welcome it. Open issues on source page or [E-Mail me](mailto: OMN@basov.net). The first and main thing required to the project is to translate documentation pages from my English to common used English.

<script>
  window.onload=createTOC();
</script>
