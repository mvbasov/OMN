Title: Help
Date: 2017-10-22 19:31:15
Modified: 2017-10-22 19:31:15
Category: Index
Tags: Markdown,
Lang: en
Author: Mikhail Basov
Summary: Open Markdown Note Help

#### Recommended applications from F-Droid:

* [Simple generic text editor](https://f-droid.org/app/org.billthefarmer.editor)
* [OI File Manager](https://f-droid.org/app/org.openintents.filemanager)

#### Recommended applications from Google Play Market:

* [OI Notepad](market://details?id=org.openintents.notepad)
* [X-plore File Manager](market://details?id=com.lonelycatgames.Xplore)


Read page about common [Markdown syntax](Markdown.html) and then about
[Extended Markdown syntax](MarkdownExt.html) specific for this application.

Any new pages can be created by editing current page and placing link to new page.


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

#### <button onclick="Android.linkButtonCallback(PFN, Title)"> <i class="material-icons">insert_link</i> </button> "Copy link to page to clipboard"

Button code:

    <button
        onclick="Android.linkButtonCallback(PFN, Title)">
        <i class="material-icons">insert_link</i>
    </button>

#### <button onclick="Android.emailButtonCallback(PFN, Title)"> <i class="material-icons">email</i> </button> "Send page by E-Mail"

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


