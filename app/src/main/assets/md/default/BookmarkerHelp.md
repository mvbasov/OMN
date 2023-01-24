Title: Bookmarker
Date: 2023-01-21 16:31:36
Modified: 2023-01-23 17:07:08
Author: Mikhail Basov
Tags: OMN documentation

## General

You can organize url bookmarks using the bookmark page. Share opened page in browser (Google Chrome tested) and point OMN as target for this sharing. The OMN will show you dialog with 'Title', 'URL', 'Tags' and 'Notes' which can be changed. Multiple tags and notes need to be separated by `;` After you will press 'OK' button the bookmark will be created on the [Incoming bookmarks](/bookmarks/Incoming.html) page

## Search

Search lookup in title, url, tags and notes parts of bookmarks and display bookmarks where search string found.
Search uses JavaScript RegExp.

[How to Use Regular Expressions in JavaScript – Tutorial for Beginners](https://www.freecodecamp.org/news/4-reasons-your-z-index-isnt-working-and-how-to-fix-it-coder-coder-6bc05f103e6c/)

#### Useful search patterns examples
| Pattern | search for |
|---------|------------|
|`li(-)?ion`| liion or li-ion|
|`(?=.*omn)(?=.*mqtt)`| omn and mqtt|
|`OMN\|mqtt`|omn or mqtt|

## Displayed bookmarks counter

At the top right of page the displayed bookmarks counter shown. This counter is clickable and show/hide "tags cloud".

## Control buttons

<button class="colexp">Expand</button>

Expand all displayed bookmarks details

<button class="colexp">Collapse</button>

Collapse all displayed bookmarks details

<button class="colexp">All</button>

Show all bookmarks

<button class="colexp">No tags</button>

Show only bookmarks without tags

## Tags cloud

Tags cloud show all tags of displayed bookmarks. All tags, except current, are clickable. Click on tag show bookmarks with this tag only.

## Bookmark element

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

* The ⓘ is button to show/hide details:
  * Tags
  * Notes
* The title: `GitHub - mvbasov/OMN`
  * Press and swap out over title (mouse over) on title show target url: `https://github.com/mvbasov/OMN`. Press on any other place hide it.
* The bookmark creation date displayed above title: `2022-12-25 16:07:28`.
* The Tag is clickable and show bookmarks with this tag only.

## Bookmark page URI parameters

It is possible to create links to the bookmark page with predefined search string or tag to display. The URL parameters need to be encoded as in browser url. Examples are below:

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

Tag parameter has priority on search. If both specified only tag used.

## Data store format
OMN will add incoming bookmarks automatically to [Incoming bookmark](/bookmarks/Incoming.html).
You can create you own bookmark page according to file format.
Format of the bookmark page described below.

#### Mandatory header of file

The following lines must to be on the top of the bookmark file.

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

The following lines must to be at the bottom of the bookmark file. The path to .css and .js files needs to correspond real files location.

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

<link rel="stylesheet" type="text/css" href="../../css/Bookmarker.css" />

