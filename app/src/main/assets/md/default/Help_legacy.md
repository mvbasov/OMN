Title: Help (legacy version)
Date: 2017-10-22 19:31:15
Modified: 2018-01-19 17:37:56
Category: Index
Tags: Documentation,  OMN default,
Lang: en
Slug: omn-help-legacy
Author: Mikhail Basov
Summary: Open Markdown Note Help

* [Syntax](#syntax)
* [Recommended aditional software](#rec-software)
* [Access notes from other device](#remote-acces)
* [Automaticaly modified pages](#auto-pages)
* [Action buttons description](#btn-desc)
* [Application settings](#app-settings)
* [Markdown page header](#page-header)
* [Images](#images)
* [Known issues](#issues)

- - -

<a id="syntax" />
### Syntax

Read page about common [Markdown syntax](Markdown.html) and then about
[Extended Markdown syntax](MarkdownExt.html) specific for this application.

- - -

<a id="rec-software" />
### Recommended aditional software

#### Recommended applications from F-Droid <span class="fg-green">(Recommended)</span>:

* [Simple generic text editor](https://f-droid.org/app/org.billthefarmer.editor)
* [OI File Manager](https://f-droid.org/app/org.openintents.filemanager)

#### Recommended applications from Google Play Market:

* [920 Text Editor](https://play.google.com/store/apps/details?id=com.jecelyin.editor.v2)
* [OI Notepad](market://details?id=org.openintents.notepad)
* [X-plore File Manager](market://details?id=com.lonelycatgames.Xplore)

- - -

<a id="remote-acces" />
### Access notes from other device

To see this application notes on another device (desktop or tablet, for example) I would recommend my small lightweight and simple web server for Android:

* [lightweight Web Server (lWS)](http://play.google.com/store/apps/details?id=net.basov.lws.r)

It was specially designed as companion application for these notes. To configure lWS go to its settings and set as document root OMN data directory. Data directory can be found at the bottom of [this page](Build.html).

- - -

<a id="auto-pages" />

### Automaticaly modified pages and files

All pages in the directory md/default/i\* can be totaly rewrite by application automaticaly. At version update time, usualy.
The same is true for thr following files:

```
css/common.css
css/highlight.css
fonts/google-material/MaterialIcons-Regular.woff2
fonts/google-material/material-icons.css
```
All other pages and files are you own files and never been touched by programm without your intention.

If Pelican CMS header meta information 'modified' exists on page it updated automaticaly after editor call and page modified.

- - -

<a id="btn-desc" />

### Action buttons description

You can place any button buttons on any place of your pages by copy/paste button code.

#### <button onclick="BLOCK_Android.editButtonCallback(PFN)"> &#x270e; </button> "Edit"

Button code:

    <button
        onclick="Android.editButtonCallback(PFN)">
        &#x270e;
    </button>

#### <button onclick="Android.homeButtonCallback()"> &#x1f3e0; </button> "Home"

Button code:

    <button
        onclick="Android.homeButtonCallback()">
        &#x1f3e0;
    </button>

#### <button onclick="BLOCK_Android.newPageButtonCallback()"> &#x2795; </button> "Add new page"

Create new page and add link to it on top of current page. Input new page (file) name and title of new page. New page file name relative to current page name directory. As title automaticaly substitute page name which can be edited. Curent page modification time set automaticaly.

Button code:

    <button
        onclick="Android.newPageButtonCallback()">
        &#x2795;
    </button>

#### <button onclick="Android.linkButtonCallback(PFN, Title)"> &#x1f517; </button> "Copy link to page to clipboard"

Button code:

    <button
        onclick="Android.linkButtonCallback(PFN, Title)">
        &#x1f517;
    </button>

#### <button onclick="Android.emailButtonCallback(PFN, Title)"> &#x2709; </button> "Send page by E-Mail"

You can send current page by E-Mail. If you press this button on /default/Build page my special address to collect platform statistic will be substituted automaticaly.

Button code:

    <button
        onclick="Android.emailButtonCallback(PFN)">
        &#x2709;
    </button>

#### <button onclick="Android.folderButtonCallback(PFN)"> &#x1f4c2; </button> "Open data folder in file manager"

This application has no permission to write to storage.
It store files in dedicated application private area.
Press this button to show this area in file browser.
*If you deinstall this application all data (pages) will be deleted automaticaly*

Button code:

    <button
        onclick="Android.folderButtonCallback(PFN)">
        &#x1f4c2;
    </button>

#### <button onclick="Android.refreshHtmlButtonCallback()"> &#8634; </button> "Refresh HTML page"

Force create HTML page from Markdown source. Disabled by defsult.

Button code:

    <button
        onclick="Android.refreshHtmlButtonCallback()">
        &#8634;
    </button>

#### <button onclick="Android.shortcutButtonCallback(PFN, Title)"> &#x2197; </button> "Create shortcut to page in home screen"

Button code:

    <button
        onclick="Android.shortcutButtonCallback(PFN, Title)">
        &#x2197;
    </button>

#### <button onclick="Android.quicknoteButtonCallback()"> &#x1f3c3; </button> "Quick note"
If you need to quickly write somethig press on this button. Write text dialogue form and press Ok. This text will be inserted at top of QuickNote page. Before text will be added horizontal devider and current time. 

Button code:

    <button id="btnSettings"
        onclick="Android.quicknoteButtonCallback()">
        &#x1f3c3;
    </button>

#### <button onclick="Android.prefButtonCallback()"> &#x1f527; </button> "Edit appication settings"

Button code:

    <button id="btnSettings"
        onclick="Android.prefButtonCallback()">
        &#x1f527;
    </button>

- - -

<a id="app-settings" />

### Application settings

#### "Notes author"
Default notes author name. Effective only at page creation time.

#### "Use VIEW_DIRECTORY"
Differnt File Managers <button onclick="Android.folderButtonCallback(PFN)"> <i class="material-icons">folder_open</i> </button> require different arguments to call.
 
For X-plore or ES file managers this options need to be set to enable.
For OI File Manager to disable.

#### "Enable code highlight"
Enable/disable source code highlighting at html creation time.
Disable this option speed up html page creation process.

#### "Enable Pelican CMS meta"
Enable/disable creation of Pelican CMS page metadata header.
Effective only at page creation time. If disabled at first line of page title placed as 4-th level header.

#### "Actions buttons show/hide"
Any of action buttons exclude <button id="btnSettings" onclick="Android.prefButtonCallback()"> &#x1f527; </button> and <button onclick="BLOCK_Android.editButtonCallback(PFN)"> &#x270e; </button> can be disabled or enabled.
This option effective only at html page creation time. If you want to add remove buttons on already created page you need to set option an regenerate (delete and the visit) html page.

- - -

<a id="page-header" />

### Markdown page header
This application use [Pelican Content Management System (Pelican CMS)]() Markdown extension for page header.

#### Header processing rules
From the begin of file any string contains valid metadata name and value (empty valye is valid) treat as page header string.
Page header processed till first empty line or not valid meta name.
No one meta strings is mandatory for this application but header processing rules applied to any page.
Recommended minimum meta header is `Title: Page title` or "#### Page title", if meta not used, followed by one empty line.
4-th level header at 1-st line af file always treat as page title.

If "Enable Pelican CMS meta" option is enabled new pages automatically created with Date: and Modified: meta set to current time also Author: meta set if defined in application settings.

If "Enable Pelican CMS meta" option is disabled new page automaticaly created with 4-th level header and the following empty line.

Title meta set to Page(file) name relative to application data storage directory if title is empty.

#### Valid metadata names
##### Title:
This meta data display as page name in application and set as `<title>` tag value in html page.

##### Tags:
This metadata will write to html page `<meta>` tag

##### Date:
This metadata will write to html page `<meta>` tag  
Automatically set to current time when page crete.

##### Modified:
This metadata will write to html page `<meta>` tag
Automatically set to current time when page crete.
Automaticaly changed (if present on page) after external editor called and file timestamp changed.

##### Category:
This metadata will write to html page `<meta>` tag

##### Authors:
This metadata will write to html page `<meta>` tag

##### Author:
Same as previout but can't contain ','
This metadata will write to html page `<meta>` tag

##### Summary:
This metadata will write to html page `<meta>` tag

##### Keywords:
This metadata will write to html page `<meta>` tag

##### Lang:
This metadata affect Pelican CMS page processing logic. Does not write to html page.

##### Status:
This metadata affect Pelican CMS page processing logic. Does not write to html page

##### Slug:
This metadata affect Pelican CMS page processing logic. Does not write to html page

- - -

<a id="images" />

### Images
Technicaly this programm suport image display. It can be done by the following code:

``` markdown
![Image alternateve name](../img/Image.jpg)
```
In this case you need to put image file to `/storage/emulated/0/Android/data/net.basov.omn.b/files/img/Image.jpg` but I don't know convenient way to do this and don't treat this feature as supported for this programm. Images need too be not upper then /storage/emulated/0/Android/data/net.basov.omn.b/files/ directory because this application has only permission to read files only from thos directory and below.

- - -

<a id="issues" />

### Known issues
* Can't return to original link when use in-page reference. Back key return to previous page. 
* Pages file name case sensitive but due to Android and VFat file system limitations 'FileName' and 'filename' may be the same file but may not to be ^)
* Page with zerro file size is not normal for this application. 