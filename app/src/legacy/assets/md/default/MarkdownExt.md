Title: Extended Markdown syntax
Date: 2017-10-11 01:40:42
Modified: 2017-11-01 22:08:15
Category: Markdown
Category: Documentation
Tags: Markdown,
Lang: en
Authors: Mikhail Basov
Summary: Extended Markdown syntax of Open Markdown Note

* [Links](#links)
  * [Inline links](#links-inline)
  * [Reference-style links](#links-reference)
* [Supported URI schemas](#uri)
  * [geo:](#uri-geo)
  * [tel:](#uri-tel)
  * [mailto:](#uri-mailto)
  * [sms:](#uri-sms)
  * [market:](#uri-market)
* [Highlight text](#highlight-text)

- - -

<a name="links" />

#### Links

Markdown supports two style of links: *inline* and *reference*.

<a name="links-inline" />

##### Inline links

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

##### Reference-style links

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

    [Some place I don't know where it is](geo:55.55,11.11)

Renders to:
[Some place I don't know where it is](geo:55.55,11.11)

<a name="uri-tel" />

#### "tel:"

Open telephony application an place provided number

    [call phone +87654](tel:+8765)

Renders to: [call phone +87654](tel:+8765)

If you wish to place USSD code as number use aditional rules:

* Escape \* by \\ in link name
* Replace \# with %23

    [Balance \*100#](tel:*100 %23)

Renders to: [Balance \*100#](tel:*100 %23)

<a name="uri-mailto" />

#### "mailto:"

Call E-Mail application.

    [E-Mail predefined letter](mailto:someone@example.com?subject=Subject text&body=E-Mail body)

Renders to: [E-Mail predefined letter](mailto:someone@example.com?subject=Subject text&body=E-Mail body)

Subject an body parts optional

<a name="uri-sms" />

#### "sms:"

Call SMS application.

    [Predefined SMS](sms:+8765?body=SMS text)

Renders to: [Predefined SMS](sms:+8765?body=SMS text)

<a name="uri-market" />

#### "market:"

Call Google Play Market application and open some application page.

    [Google Maps](market://details?id=com.google.android.apps.maps)

Renders to: [Google Maps](market://details?id=com.google.android.apps.maps)

This type of link work only inside Android. Equivalent links operates on pages exported html pages are

    [Google Maps](http://play.google.com/store/apps/details?id=com.google.android.apps.maps)

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

