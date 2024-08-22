Title: Bookmarker
Date: 2023-01-21 16:31:36
Modified: 2023-01-26 21:06:06
Author: Mikhail Basov
Tags: OMN documentation

## General

You can organize url bookmarks using the bookmark page. Share opened page in browser (Google Chrome tested) and select OMN as target for sharing. The OMN will show you dialog with 'Title', 'URL', 'Tags' and 'Notes' which can be changed. Multiple tags and notes need to be separated by `;` After you press 'OK' button the bookmark will be created on the [Incoming bookmarks](/bookmarks/Incoming.html) page

### Search

Search lookup in title, url, tags and notes parts of bookmarks and display bookmarks, where search string found.
Search uses JavaScript RegExp.

[How to Use Regular Expressions in JavaScript – Tutorial for Beginners](https://www.freecodecamp.org/news/regular-expressions-for-beginners/)

#### Useful search patterns examples
| Pattern | search for |
|---------|------------|
|`li(-)?ion`| `liion` or `li-ion`|
|`(?=.*omn)(?=.*mqtt)`| `omn` and `mqtt`|
|`OMN\|mqtt`|`omn` or `mqtt`|
|`\bOpen\b`|`Open Markdown` but not `OpenFile`|
|`Open\w`|`OpenFile` but not `Open Markdown`|

### Displayed bookmarks counter

At the top right of page the displayed bookmarks counter shown. This counter is clickable and show/hide "tags cloud".

### Control buttons

<button class="colexp">Expand</button>

Expand details of all displayed bookmarks

<button class="colexp">Collapse</button>

Collapse details of all displayed bookmarks

<button class="colexp">All</button>

Show all bookmarks

<button class="colexp">No tags</button>

Show only the bookmarks without tags

### Bookmark element

- - -

<ul>
  <li>
    <button 
      class="details"
      onClick="
        if(this.parentNode) {
          var infoBlock = this.parentNode.getElementsByTagName('ul')[0];
          if(infoBlock.style.display == 'none') {
            infoBlock.style.display='block';
          } else {
             infoBlock.style.display='none';
          }
        }">
ⓘ
    </button>
    <a href="https://github.com/mvbasov/OMN">GitHub - mvbasov/OMN</a>
    <span><br>https://github.com/mvbasov/OMN</span>
    <br>
    <span class="spanDate">2022-12-25 16:07:28</span>
    <ul style="display: none;">
      <li><div><button class="tag">Tag 1</button><button class="tag">Tag2</button></div></li>
      <li><div><span class="note">"Note 1"</span>,<span class="note">"Note 2"</span></div></li>
    </ul>
  </li>
</ul>

- - -

* The ⓘ is button is used to show/hide details:
  * Tags
  * Notes
* The title: `GitHub - mvbasov/OMN`
  * Press and hold the title (mouse over) to show target url: `https://github.com/mvbasov/OMN`. Press on any other place hide it.
* The bookmark creation date is displayed above the title: `2022-12-25 16:07:28`.
* You can click the tag button to show bookmarks with this tag.

## Tags
### Tags cloud

Tags cloud shows all tags of displayed bookmarks. All tags, except current, are clickable. Click on tag show bookmarks with that tag only.

### Predefined tags

You can define tags to show up in tag auto completion when creating a bookmark.
These tags are defined on [Bookmark Tags](../bookmarks/BookmarkTags.html), one tag per line.

## Advanced 
### Bookmark page URI parameters
#### Special tag/search
It is possible to create links to the bookmark page with special search string or tag to display. The URL parameters need to be encoded as in browser url. Examples:

```
* [Bookmarks search \"OMN&mqtt\"](/bookmarks/Incoming.html?search=%28%3F%3D.*omn%29%28%3F%3D.*mqtt%29)
```
* [Bookmarks search \"OMN&mqtt\"](/bookmarks/Incoming.html?search=%28%3F%3D.*omn%29%28%3F%3D.*mqtt%29)

```
* [Bookmarks search \"OMN|mqtt\"](/bookmarks/Incoming.html?search=OMN%7Cmqtt)
```
* [Bookmarks search \"OMN|mqtt\"](/bookmarks/Incoming.html?search=OMN%7Cmqtt)

```
* [Bookmarks show without tag](/bookmarks/Incoming.html?tag=-1)
```
* [Bookmarks show without tag](/bookmarks/Incoming.html?tag=-1)

```
* [Bookmarks show with \"Tag 2\"](/bookmarks/Incoming.html?tag=Tag%202)
```
* [Bookmarks show with \"Tag 2\"](/bookmarks/Incoming.html?tag=Tag%202)

Tag parameter has priority on search. If multiple specified only tag used.

#### Configuration
The bookmark page has options listed below:

##### `ignoreCase`
Default value is `true`.  
If this option set (true) the strings `tiMer` and `timer` treat the same for search. If the option unset (false) the strings is differ.

##### `stripAccents`
Default value is `true`.  
If this option set (true) the strings `ёжик` and `ежик` treat the same for search. If the option unset (false) the strings is differ.

Try runing bookmark page with some options set/unset, click the links below:
* [ignoreCase unset](/bookmarks/Incoming.html?config=%7B%22ignoreCase%22%3Afalse%7D)
* [ignoreCase set](/bookmarks/Incoming.html?config=%7B%22ignoreCase%22%3Atrue%7D)
* [stripAccents unset](/bookmarks/Incoming.html?config=%7B%22stripAccents%22%3Afalse%7D)
* [stripAccents set](/bookmarks/Incoming.html?config=%7B%22stripAccents%22%3Atrue%7D)

To show current effective configuration visit the following link:
* [show config](/bookmarks/Incoming.html?config=show)

The configuration stored permanently if [JS localStorage](/default/Help.html#enable-jsdebug) is enabled.

Bookmark page configuration is not added to application settings to allow page to operate even if it is exported as .html page and placed to desktop computer, for example.

`config` and `url`/`search` uri parametes can be combined.

### Data store format
OMN will add incoming bookmarks to [Incoming bookmark](/bookmarks/Incoming.html).  
You can create you own bookmark page with format described below.

#### Mandatory header of file

The following lines must to be on the top of the bookmark file (below metadata if present).

- - -

```
<script>bookmarks = [
<!-- Don't edit body below this line -->
```

- - -

#### Bookmark format

- - -


```
  {
    "date": "2023-01-11 08:52:49",
    "url": "https://github.com/mvbasov/OMN",
    "title": "GitHub - \"mvbasov/OMN\"",
    "tags": [
      "Tag 1",
      "Tag 2"
    ],
    "notes": [
      "Note 1",
      "Note 2"
    ]
  },
```

- - -

#### Mandatory footer of file

The following lines must to be at the bottom of the bookmark file. The path to .css and .js files needs to correspond to the real files location.

- - -

```
  } <--- from the last bookmark.
];
</script>

<!-- end of bookmarks definition -->

<link rel="stylesheet" type="text/css" href="../../css/Bookmarker.css" />
<script type="text/javascript" src="../../js/Bookmarker.js"></script>
```

- - -
<script>
  window.onload=createTOC(document.getElementById('content'));
</script>

<link rel="stylesheet" type="text/css" href="../../css/Bookmarker.css" />

<link rel="stylesheet" type="text/css" href="../../css/PSearch.css" />
<script type="text/javascript" src="../../js/PSearch.js"></script>

