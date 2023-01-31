Title: Help
Date: 2017-10-22 19:31:15
Modified: 2022-12-22 01:19:15
Category: Index
Tags: OMN documentation
Lang: en
Slug: omn-help
Author: Mikhail Basov
Summary: Open Markdown Note Help

<a id="syntax" />

### Syntax

Read page about common [Markdown syntax](Markdown.html) and then about
[Extended Markdown syntax](MarkdownExt.html) specific for this application.

- - -

<a id="btn-desc" />

### Action buttons description

Actions buttons can be found at any page top header and allow you to control the program.
Also you can place any button on any place of your pages by copy/paste button code. _At default pages buttons able to modify pages not function but you able to do it from page header if you wish._

#### <button onclick="BLOCK_Android.editButtonCallback(PFN)"> <i class="material-icons">edit</i> </button> "Edit"

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

<a name="add-new-page" />

#### <button onclick="BLOCK_Android.newPageButtonCallback()"> <i class="material-icons">add_box</i> </button> "Add new page"

Create new page and add link to it on top of current page. Input new page (file) name and title of new page. New page file name relative to current page name directory. As title automatically substitute page name which can be edited. Current page modification time set automatically.

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

You can send current page by E-Mail. If you press this button on ```/default/Build``` page my special address to collect platform statistic will be substituted automatically.

Button code:

    <button
        onclick="Android.emailButtonCallback(PFN)">
        <i class="material-icons">email</i>
    </button>

#### <button onclick="Android.sendButtonCallback(PFN, Title)"> <i class="material-icons">send</i> </button> "Send page ..."

You can send current page as file. Page file will be wraped by some meta information whivh required to correct import. Tested with Telegram and K-9 Mail

Button code:

    <button
        onclick="Android.sendButtonCallback(PFN)">
        
    </button>

#### <button onclick="Android.folderButtonCallback(PFN)"> <i class="material-icons">folder_open</i> </button> "Open data folder in file manager"

This application has no permission to write to storage.
It store files in dedicated application private area.
Press this button to show this area in file browser.
*If you uninstall this application all data (pages) will be deleted automatically*

Button code:

    <button
        onclick="Android.folderButtonCallback(PFN)">
        <i class="material-icons">folder_open</i>
    </button>
    
#### <button onclick="Android.refreshHtmlButtonCallback()"> <i class="material-icons">cached</i> </button> "Refresh HTML page"

Force create HTML page from Markdown source. Disabled by default.

Button code:

    <button
        onclick="Android.refreshHtmlButtonCallback()">
        <i class="material-icons">cached</i>
    </button>

#### <button onclick="Android.shortcutButtonCallback(PFN, Title)"> <i class="material-icons">screen_share</i> </button> "Create shortcut to page in home screen"

Button code:

    <button
        onclick="Android.shortcutButtonCallback(PFN, Title)">
        <i class="material-icons">screen_share</i>
    </button>

#### <button onclick="Android.quicknoteButtonCallback()"> <i class="material-icons">comment</i> </button> "Quick note"
If you need to quickly write something press on this button. Write text dialog form and press Ok. This text will be inserted at top of QuickNote page. Before text will be added horizontal divider and current time. 

Button code:

    <button
        onclick="Android.quicknoteButtonCallback()">
        <i class="material-icons">comment</i>
    </button>

#### <button onclick="Android.prefButtonCallback()"> <i class="material-icons">settings</i> </button> "Edit application settings"

Button code:

    <button
        onclick="Android.prefButtonCallback()">
        <i class="material-icons">settings</i>
    </button>

- - -

<a id="app-settings" />

### Application settings

#### "Notes author"

Default notes author name. Effective only at page creation time.

#### "Use VIEW_DIRECTORY"

Different File Managers <button onclick="Android.folderButtonCallback(PFN)"> <i class="material-icons">folder_open</i> </button> require different arguments to call.
 
The example. For X-plore or ES file managers this options need to be set to enable.
For OI File Manager to disable.

#### "Enable code highlight"

Enable/disable source code highlighting at HTML creation time.
Disable this option speed up HTML page creation process.

#### "Enable Pelican CMS meta"

Enable/disable creation of Pelican CMS page metadata header.
Effective only at page creation time. If disabled at first line of page title placed as 4-th level header.

#### "Actions buttons show/hide"

Any of action buttons exclude <button onclick="Android.prefButtonCallback()"> <i class="material-icons">settings</i> </button> and <button onclick="BLOCK_Android.editButtonCallback(PFN)"> <i class="material-icons">edit</i> </button> can be disabled or enabled.
This option effective only at HTML page creation time. If you want to add remove buttons on already created page you need to set option an regenerate (delete and the visit) HTML page.

<a id="enable-jsdebug" />

#### "Enable JS debug"

After enable this options JavasScripts's console log and errors messages processed by the application. Last string of output placed on [special debug page](/default/JSDebug.html) which can be quickly accessed from page header by button <button onclick="Android.debugButtonCallback()"> <i class="material-icons">adb</i> </button>
which appeared in page header automatically after enabling this option.

#### "Enable JS localStorage"

Enable using `localStorage` in Java Scripts on pages  
<span class="fg-red">WARNING! EXPERIMENTAL! </span>  
_The application has no mechanism to clean or manage another way databases created by this feature_  
**Restart the application to make this setting effective**

#### "Enable JS openDatabase"

Enable using `openDatabase` in Java Scripts on pages  
<span class="fg-red">WARNING! EXPERIMENTAL! </span>  
_The application has no mechanism to clean or manage another way databases created by this feature_  
**Restart the application to make this setting effective**

#### "Enable Intent URI"

Enable "intent:" URI scheme.  
<span class="fg-red">WARNING! EXPERIMENTAL! Dangerous but powerful. You device can be controlled by simple link click.</span>
[Example of usage](/default/MarkdownExt.html#uri-intent)

#### "Enable Termux RUN_COMMAND Intent"

<span class="fg-red">WARNING! EXPERIMENTAL! Dangerous but powerful.</span>
Enable [Termux](https://f-droid.org/en/packages/com.termux/) integration. [Example of usage](/default/MarkdownExt.html#termux-intent)
**Require "Enable Intent URI" enabled.**

- - -

<a id="page-header" />

### Markdown page header

This application use [Pelican Content Management System (Pelican CMS)](https://blog.getpelican.com) Markdown extension for page header.

#### Header processing rules

From the begin of file any string contains valid metadata name and value (empty value is valid) treat as page header string.
Page header processed till first empty line or not valid meta name.
No one meta strings is mandatory for this application but header processing rules applied to any page.
Recommended minimum meta header is `Title: Page title` or "#### Page title", if meta not used, followed by one empty line.
4-th level header at 1-st line of file always treat as page title.

If "Enable Pelican CMS meta" option is enabled new pages automatically created with Date: and Modified: meta set to current time also Author: meta set if defined in application settings.

If "Enable Pelican CMS meta" option is disabled new page automatically created with 4-th level header and the following empty line.

Title meta set to Page(file) name relative to application data storage directory if title is empty.

#### Valid metadata names

##### Title:

This meta data display as page name in application and set as `<title>` tag value in HTML page.

<a id="help-tags" />

##### Tags:

Tags help you to organize pages. Page tags displayed at page header.
This metadata will write to HTML page `<meta>` tag. Tags from modified pages automatically collected to special [Tags page](/Tags.html) and placed to page header. If you press tag on page header you will be redirected to tag page section according to pressed tag and tag section header will be highlighted. To navigate to top of tag page where some "tag cloud" displayed press on any tag section header.

##### Date:

This metadata will write to HTML page `<meta>` tag  
Automatically set to current time when page create.

##### Modified:

This metadata will write to HTML page `<meta>` tag
Automatically set to current time when page create.
Automatically changed (if present on page) after external editor called and file time stamp changed.

##### Category:

This metadata will write to HTML page `<meta>` tag

##### Authors:

This metadata will write to HTML page `<meta>` tag

##### Author:

Same as previous but can't contain ','
This metadata will write to HTML page `<meta>` tag

##### Summary:

This metadata will write to HTML page `<meta>` tag

##### Keywords:

This metadata will write to HTML page `<meta>` tag

##### Lang:

This metadata affect Pelican CMS page processing logic. Does not write to HTML page.

##### Status:

This metadata affect Pelican CMS page processing logic. Does not write to HTML page

##### Slug:

This metadata affect Pelican CMS page processing logic. Does not write to HTML page

- - -

<a id="images" />

### Images

Technically this program support image display. It can be done by the following code:

``` markdown
![Image alternative name](../img/Image.jpg)
```
In this case you need to put image file to [[data directory]]()`/img/Image.jpg` but I don't know convenient way to do this and don't treat this feature as supported for this program. Images need too be not upper then /storage/emulated/0/Android/data/net.basov.omn.b/files/ directory because this application has only permission to read files only from this directory and below.

- - -

<a id="issues" />

### Known issues

* Can't return to original link when use in-page reference. Back key return to previous page. 
* Pages file name case sensitive but due to Android and VFat file system limitations 'FileName' and 'filename' may be the same file but may not to be ^)
* Page with zero file size is not normal for this application. 
<a name="header-issue" />
* First click on the page header shift top of page content little bit lower then expected (approximetly to the size of expnded header). The following click place page content top to apropriate place. This issue reproduced only on some Android 8/9. As work around at the end of every html page Goggle material design icon font symbol <i class="material-icons">error</i> placed. Symbol is isnvisible (white on white). It solve the issue.

<script>
  window.onload=createTOC();
</script>

<link rel="stylesheet" type="text/css" href="../../css/PSearch.css" />
<script type="text/javascript" src="../../js/PSearch.js"></script>

