/*
 * Function to show/hide page header
 */
function setBodyTop(){
  var sticky = document.getElementById('ptitle').offsetHeight;
  document.getElementById('content').style.paddingTop=sticky+'px'; 
}

function toggleHdrBtnVis() {
  hheader=document.getElementById('hidable_header');
  btn=document.getElementById('title_arrow');
  if (hheader.style.display !== 'none') {
    hheader.style.display = 'none';
    btn.innerHTML = '&nbsp;+&nbsp;';
  } else {
    hheader.style.display = 'block';
    btn.innerHTML = '&nbsp;&#x2013;&nbsp;';
  }
  //setBodyTop();
}

/*
 * Functions to create Table Of Content (TOC)
 */
function findHeadings(root, sects){
  for(var c=root.firstChild; c!=null; c=c.nextSibling){
    if (c.nodeType!==1) continue;
    if (c.tagName.length==2 && c.tagName.charAt(0)=="H")
      sects.push(c);
    else
      findHeadings(c, sects);
  }
  return sects;
}

function createTOC(){
  var croot=document.getElementById("content");
  var toc=document.getElementById("TOC");
  if(!toc) {
    toc=document.createElement("div");
    toc.id="TOC";
    croot.insertBefore(toc, croot.firstChild);
  }

  var headings;
  if (croot.querySelectorAll) 
    headings=croot.querySelectorAll("h1, h2, h3, h4, h5, h6");
  else
    headings=findHeadings(croot, []);

  var sectionNumbers=[0,0,0,0,0,0];

  for(var h=0; h<headings.length; h++) {
    var heading=headings[h];
    if(heading.parentNode==toc) continue;

    var level=parseInt(heading.tagName.charAt(1));
    if (isNaN(level)||level<1||level>6) continue;

    sectionNumbers[level-1]++;
    for(var i=level; i<6; i++) sectionNumbers[i]=0;
    
    var sectionNumber=sectionNumbers.slice(0, level).join(".");

    var span=document.createElement("span");
    span.className="TOCSectNum";
    var sectionNumberDisplay = sectionNumber.replace(/0./g, "");
    if(sectionNumberDisplay.length == 1)
      sectionNumberDisplay += ".";
    sectionNumberDisplay += " ";
    span.innerHTML=sectionNumberDisplay;
    heading.insertBefore(span, heading.firstChild);
    heading.id="TOC"+sectionNumber;
    var anchor=document.createElement("a");
    heading.parentNode.insertBefore(anchor, heading);
    anchor.appendChild(heading);

    var link=document.createElement("a");
    link.href="#TOC"+sectionNumber; 
    link.innerHTML=heading.innerHTML;

    var entry=document.createElement("div");
    entry.className="TOCEntry TOCLevel" + level;
    entry.appendChild(link);

    toc.appendChild(entry);
  }
}
