Title: Welcome!
Date: 2017-10-11 01:38:26
Modified: 2017-10-16 14:17:19
Category: Index
Tags: Markdown,
Lang: en
Authors: Mikhail Basov
Summary: Open Markdown Note start page

### General information.

This is simple, lightweight and open source application to create and organize everyday notes.
Notes stored in [Markdown](/default/Markdown.html) format and shown as html. 

To see action buttons press on the page title.

This program use external editor. If you try to edit page and get message about no editor found install one, please.

Suitable almost any editor. 

I would recommend install the following editor and file manager to use with this application:

#### Recommended applications from Google Play Market:

* [OI Notepad](market://details?id=org.openintents.notepad)
* [X-plore File Manager](market://details?id=com.lonelycatgames.Xplore)

#### Recommended applications from F-Droid:

* [OI Notepad](https://f-droid.org/app/org.openintents.notepad)
* [OI File Manager](https://f-droid.org/app/org.openintents.filemanager)

**Hint**: Before creating own start page press on <button onclick="Android.linkButtonCallback(PFN, Title)"><i class="material-icons">insert_link</i></button> button to save link to this page in clipboard and then paste it on your new page.

Read page about common [Markdown syntax](/default/Markdown.html) and then about
[Extended Markdown syntax](/default/MarkdownExt.html) specific for this application.

Any new pages can be created by editing current page and placing link to new page.

Now, time to create [Own start page](/Start.html)

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

#### <button onclick="Android.prefButonCallback()"> <i class="material-icons">settings</i> </button> "Edit appication settings"

Button code:

    <button id="btnSettings"
        onclick="Android.prefButonCallback()">
        <i class="material-icons">settings</i>
    </button>

