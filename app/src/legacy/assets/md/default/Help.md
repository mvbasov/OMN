Title: Help
Date: 2017-10-22 19:31:15
Modified: 2017-11-01 22:06:43
Category: Index
Tags: Markdown,
Lang: en
Slug: omn-help
Author: Mikhail Basov
Summary: Open Markdown Note Help

* [Recommended aditional software](#rec-software)
* [Action buttons description](#btn-desc)
* [Application settings](#app-settings)
* [Markdown page header](#page-header)
* [Known issues](#issues)

- - -

<a id="rec-software" />

### Recommended aditional software

#### Recommended applications from F-Droid <span class="fg-green">(Recommended)</span>:

* [Simple generic text editor](https://f-droid.org/app/org.billthefarmer.editor)
* [OI File Manager](https://f-droid.org/app/org.openintents.filemanager)

#### Recommended applications from Google Play Market:

* [OI Notepad](market://details?id=org.openintents.notepad)
* [X-plore File Manager](market://details?id=com.lonelycatgames.Xplore)


Read page about common [Markdown syntax](Markdown.html) and then about
[Extended Markdown syntax](MarkdownExt.html) specific for this application.

Any new pages can be created by editing current page and placing link to new page.
 
- - -

<a id="btn-desc" />

### Action buttons description

You can place any button buttons on any place of your pages by copy/paste button code.

#### <button onclick="Android.editButtonCallback(PFN)"> <i class="material-icons">edit</i> </button> "Edit"

Button code:

    <button
        onclick="Android.editButtonCallback(PFN)">
        <i class="material-icons">edit</i>
    </button>

#### <button onclick="Android.homeButtonCallback()"> <i class="material-icons">home</i> </button> "Home"

Button code:

    <button
        onclick="Android.homeButtonCallback()">
        <i class="material-icons">home</i>
    </button>

#### <button onclick="Android.newPageButtonCallback()"> <i class="material-icons">add_box</i> </button> "Add new page"

Create new page and add link to it on top of current page. Input new page (file) name and title of new page. New page file name relative to current page name directory. As title automaticaly substitute page name which can be edited. Curent page modification time set automaticaly.

Button code:

    <button
        onclick="Android.newPageButtonCallback()">
        <i class="material-icons">add_box</i>
    </button>

#### <button onclick="Android.linkButtonCallback(PFN, Title)"> <i class="material-icons">insert_link</i> </button> "Copy link to page to clipboard"

Button code:

    <button
        onclick="Android.linkButtonCallback(PFN, Title)">
        <i class="material-icons">insert_link</i>
    </button>

#### <button onclick="Android.emailButtonCallback(PFN, Title)"> <i class="material-icons">email</i> </button> "Send page by E-Mail"

You can send current page by E-Mail. If you press this button on /default/Build page my special address to collect platform statistic will be substituted automaticaly.

Button code:

    <button
        onclick="Android.emailButtonCallback(PFN)">
        <i class="material-icons">email</i>
    </button>

#### <button onclick="Android.folderButtonCallback(PFN)"> <i class="material-icons">folder_open</i> </button> "Open data folder in file manager"

This application has no permission to write to storage.
It store files in dedicated application private area.
Press this button to show this area in file browser.
**If you deinstall this application all data (pages) will be deleted automaticaly**

Button code:

    <button
        onclick="Android.folderButtonCallback(PFN)">
        <i class="material-icons">folder_open</i>
    </button>

#### <button onclick="Android.shortcutButtonCallback(PFN, Title)"> <i class="material-icons">screen_share</i> </button> "Create shortcut to page in home screen"

Button code:

    <button
        onclick="Android.shortcutButtonCallback(PFN, Title)">
        <i class="material-icons">screen_share</i>
    </button>

#### <button onclick="Android.quicknoteButtonCallback()"> <i class="material-icons">receipt</i> </button> "Quick note"
If you need to quickly write somethig press on this button. Write text dialogue form and press Ok. This text will be inserted at top of QuickNote page. Before text will be added horizontal devider and current time. 

Button code:

    <button id="btnSettings"
        onclick="Android.quicknoteButtonCallback()">
        <i class="material-icons">receipt</i>
    </button>

#### <button onclick="Android.prefButtonCallback()"> <i class="material-icons">settings</i> </button> "Edit appication settings"

Button code:

    <button id="btnSettings"
        onclick="Android.prefButtonCallback()">
        <i class="material-icons">settings</i>
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

#### "Enable code highlight
Enable/disable source code highlighting at html creation time.
Disable this option speed up html page creation process.

#### "Actions buttons show/hide"
Any of action buttons exclude <button id="btnSettings" onclick="Android.prefButtonCallback()"> <i class="material-icons">settings</i> </button> and <button onclick="Android.editButtonCallback(PFN)"> <i class="material-icons">edit</i> </button> can be disabled or enabled.
This option effective only at html page creation time. If you want to add remove buttons on already created page you need to set option an regenerate (delete and the visit) html page.

- - -

<a id="page-header" />

### Markdown page header
This application use [Pelican Content Management System (Pelican CMS)]() Markdown extension for page header.

#### Header processing rules
From the begin of file any string contains valid metadata name and value (empty valye is valid) treat as page header string.
Page header processed till first empty line or not valid meta name.
No one meta strings is mandatory for this application but header processing rules applied to any page.
Recommended minimum header is `Title: Page title` followed by one empty line.

New pages automatically create with Date: and Modified: meta set to current time also Author: meta set if defined in application settings.
Title meta set to Page(file) name relative to application data storage directory. 

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

##### Category:
This metadata will write to html page `<meta>` tag

##### Authors:
This metadata will write to html page `<meta>` tag

##### Author:
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

<a id="issues" />

### Known issues
* Can't return to original link when use in-page reference. Back key return to previous page. 
