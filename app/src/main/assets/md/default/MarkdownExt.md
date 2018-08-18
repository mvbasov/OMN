Title: Extended Markdown syntax
Date: 2017-10-11 01:40:42
Modified: 2018-01-19 03:36:21
Category: Documentation
Tags: Markdown, Documentation, OMN default 
Lang: en
Author: Mikhail Basov
Summary: Extended Markdown syntax of Open Markdown Note

<a name="links" />
### Links

Markdown supports two style of links: *inline* and *reference*.

<a name="links-inline" />
#### Inline links

In both styles, the link text is delimited by [square brackets].

To create an inline link, use a set of regular parentheses immediately
after the link text's closing square bracket. Inside the parentheses,
put the URL where you want the link to point, along with an *optional*
title for the link, surrounded in quotes. For example:

    This is [an example](http://example.com/ "Title") inline link.

    [This link](http://example.net/) has no title attribute.

Will produce:

    <p>This is <a href="http://example.com/" title="Title">
    an example</a> inline link.</p>

    <p><a href="http://example.net/">This link</a> has no
    title attribute.</p>

If you're referring to a local page, you must use relative paths:

    [My start page](../Start.html)

<a name="links-reference" />
#### Reference-style links

Reference-style links use a second set of square brackets, inside
which you place a label of your choosing to identify the link:

    This is [an example][id] reference-style link.

You can optionally use a space to separate the sets of brackets:

    This is [an example] [id] reference-style link.

Then, anywhere in the document, you define your link label like this,
on a line by itself:

    [id]: http://example.com/  "Optional Title Here"

That is:

*   Square brackets containing the link identifier (optionally
    indented from the left margin using up to three spaces);
*   followed by a colon;
*   followed by one or more spaces (or tabs);
*   followed by the URL for the link;
*   optionally followed by a title attribute for the link, enclosed
    in double or single quotes, or enclosed in parentheses.

- - -

<a name="uri" />
### Supported URI schemas

URI schema is protocol which will be used to follow link.
http:// or https:// uri schemas open web pages, usually.

This program also support the following uri:

<a name="uri-geo" />
#### "geo:"

Open map or navigation application and point it to geographical coordinates
``` markdown
[Some place I don't know where it is](geo:55.55,11.11)
```
Renders to:
[Some place I don't know where it is](geo:55.55,11.11)

<a name="uri-tel" />
#### "tel:"

Open telephony application an place provided number
``` markdown
[call phone +87654](tel:+87654)
```
Renders to:
[call phone +87654](tel:+87654)

If you wish to place USSD code as number use aditional rules:

* Escape \* by \\ in link name
* Replace \# with %23 in phone number

``` markdown
[Balance \*100#](tel:*100 %23)
```
Renders to:
[Balance \*100#](tel:*100 %23)

<a name="uri-mailto" />
#### "mailto:"

Call E-Mail application.
``` markdown
[E-Mail predefined letter](mailto:someone@example.com?subject=Subject text&body=E-Mail body)
```
Renders to:
[E-Mail predefined letter](mailto:someone@example.com?subject=Subject text&body=E-Mail body)

Subject an body parts optional

<a name="uri-sms" />
#### "sms:"

Call SMS application.
``` markdown
[Predefined SMS](sms:+8765?body=SMS text)
```
Renders to:
[Predefined SMS](sms:+8765?body=SMS text)

<a name="uri-market" />
#### "market:"

Call Google Play Market application and open some application page.
``` markdown
[Google Maps](market://details?id=com.google.android.apps.maps)
```
Renders to:
[Google Maps](market://details?id=com.google.android.apps.maps)

This type of link work only inside Android. Equivalent links operates on pages exported html pages are
``` markdown
[Google Maps](http://play.google.com/store/apps/details?id=com.google.android.apps.maps)
```
- - -

<a name="highlight-text" />
### Highlight text 

You can change text background (like maker on paper text). See examples:

Some <span class="bg-yellow">text</span> and another <span class="bg-aqua">text</span>

Code:

``` html
Some <span class="bg-yellow">text</span> and another <span class="bg-aqua">text</span>
```

Some <span class="fg-red">text with another color</span>

Code:

``` html
Some <span class="fg-red">text with another color</span>
```

Some <span class="fg-green">text with another color</span>

Code:

``` html
Some <span class="fg-green">text with another color</span>
```

Some text <span class="bg-yellow fg-red">text with both attributes changed</span>

Code:

``` html
Some text <span class="bg-yellow fg-red">text with both attributes changed</span>
```

- - -

<a name="custom-css" />
### Custom CSS

You can customize global CSS used on all pages by creating and editing ```css/custom.css``` file.
This CSS referenced at the end of header on any generated HTML page. You can extend [text highlight](#highlight-text) combinations, for example

- - -

<a name="create-toc" />
### Create TOC

If you wish create Table Of Contents (TOC) place at the end of you page the following code:

    <script>
     window.onload=createTOC(document.getElementById('content'));
    </script>

TOC will be created at the top of you page. To position TOC to another place put at that place the following code:

    <div id="TOC"></div>

- - -

<a name="google-icons" />
### Google Material Design icons

*Doesn't available in legacy version*
You can use [Material design icons](https://material.io/icons/) anywhere as text.  
(if name contain spece replace it to _)

    <i class="material-icons">check_circle</i>
    
Renders to: <i class="material-icons">check_circle</i>

- - -

<a name="unicode-symbols" />
### Unicode symbols collection

Any pages can contain Unicode symbols. In the legacy mode such symbols used instead of Google Material Design icons.
There is collection of funny or usefull Unicode symbols.

|          |           |          |           |
|:--------:|:---------:|:--------:|:---------:|
| &#x270e; | &#x1f3e0; | &#x2795; | &#x1f517;
|`&#x270e;`|`&#x1f3e0;`|`&#x2795;`|`&#x1f517;`
| &#x2709; | &#x1f4c2; | &#x2197; | &#x1f3c3;
|`&#x2709;`|`&#x1f4c2;`|`&#x2197;`|`&#x1f3c3;`
| &#x1f527;| &#x2103;  | &#x1f514; |&#x1f515;
|`&#x1f527;`|`&#x2103;`|`&#x1f514;`|`&#x1f514;`
|  &#x3b1; |  &#x3b2;  |  &#x3a9; |  &#x3bc;
|`&#x3b1;` | `&#x3b2;` | `&#x3a9;`|`&#x3bc;`
| &#x2713; |  &#xb0;   |&#x1f529; | &#x1f427;
|`&#x2713;`| `&#xb0;`  |`&#x1f529;`|`&#x1f427;`
|  &#xb2;  |  &#xb5;   | &#x23f3; |&#x23f0;
| `&#xb2;` | `&#xb5;`  |`&#x23f3;`|`&#x23f0;`
| &#x2300; | &#x2328;  | &#x231b;  | &#x231a;
|`&#x2300;`|`&#x2328;` |`&#x231b;` |`&#x231a;`
| &#x238c; | &#x2393;  | &#x23f1; | &#x23f2;
|`&#x238c;`|`&#x2393;` |`&#x23f1;`|`&#x23f2;`
|  &#xae;  |  &#xa9;   | &#x2122; | &#x23f2;
| `&#xae;` | `&#xa9;`  |`&#x2122;`|`&#x23f2;`

<script>
  window.onload=createTOC(document.getElementById('content'));
</script>
