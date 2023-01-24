var bVersion = '0.8 2023-01-24 18:29:29';

function showBookmarks(onlyTag = '', search = '') {
  // sort bookmarks by date (newest upper)
  bookmarks = bookmarks.sort((a, b) => { if (a.date > b.date) { return -1; } });
  // tag display priority on search
  if (onlyTag !== '' || search === '') {
    search = '';
    var searchF = document.querySelector('#searchInput');
    searchF.value = '';
  }
  var counter = 0;
  var allTags = [];
  var ul = document.createElement('ul');
  ul.style.height = '100%;';
  for (const bm of bookmarks) {
    if (bm.url !== '') {
      if (!display(bm, onlyTag, search)) continue;
      counter++;
      var li = document.createElement('li');
      var a = document.createElement('a');
      a.setAttribute('href',bm.url);
      // Set title or url as title if empty
      if (bm.title && bm.title !== '')
        a.innerHTML = bm.title;
      else
        a.innerHTML = bm.url;
      li.appendChild(a);
      var span = document.createElement('span');
      span.innerHTML = '<br/>' + bm.url;
      li.appendChild(span);
      if (bm.date && bm.date !== '') {
        li.appendChild(document.createElement('br'));
        var spanDate = document.createElement('span')
        txt = document.createTextNode('');
        txt.textContent =  bm.date;
        spanDate.appendChild(txt);
        spanDate.className = 'spanDate';
        li.appendChild(spanDate);
      }
      // find tags if exists   
      var ulin = document.createElement('ul');
      var liint = document.createElement('li');
      var divt = document.createElement('div');
      txt = document.createTextNode('');
      var btnt = document.createElement('button');
      if (bm.tags && bm.tags.length > 0){
        for (_tag of bm.tags) {
          btnt = document.createElement('button');
          btnt.innerText = _tag;
          btnt.className = 'tag';
          btnt.onclick = function(){showBookmarks(this.innerText);};
          divt.appendChild(btnt);
          if (!allTags.includes(_tag)) {
            allTags.push(_tag);
          }
          first = false;
        }
        liint.appendChild(divt);
        ulin.appendChild(liint);
      }
      // find notes if exists
      txt = document.createTextNode("")
      var first = true;
      if (bm.notes && bm.notes.length > 0){
        var liinn = document.createElement('li');
        for (note of bm.notes) {
          if (!first) txt.textContent += ', '  
          txt.textContent += '"'+note+'"';
          first = false;
        }
        liinn.appendChild(txt);
        ulin.appendChild(liinn);
      }
      // add sub-ul with tags and notes if not empty
      if (ulin.firstChild) {
        var txt = document.createTextNode('');
        txt.textContent = 'ⓘ';
        var btn = document.createElement('button');
        btn.onclick=function() {
          if(this.parentNode) {
            var infoBlock = this.parentNode.getElementsByTagName('ul')[0];
            if(infoBlock.style.display == 'none') {
              infoBlock.style.display='block';
            } else {
              infoBlock.style.display='none';
            }
          }
        }
        btn.appendChild(txt);
        btn.className = 'details';
        li.insertBefore(btn, li.firstChild);
        li.appendChild(ulin);
      }
      // add boormark to list
      ul.appendChild(li);
    }
  }
  // display list of bookmarks
  document.querySelector('#bmlist').replaceChildren(ul);
  // collapse all details
  colexpall('collapse');
  // display counter
  var counterSpan = document.querySelector('#counter');
  counterSpan.innerHTML = counter;
  counterSpan.onclick = toggleTagsCloud;
  // clear and display tags cloud
  var tagsDiv = document.querySelector('#tagsCloud');
  tagsDiv.innerHTML = "";
  if (allTags.length > 0) {
    allTags = allTags.sort();
    for (const tagC of allTags) {
      var tagB = document.createElement('button');
      if (tagC == onlyTag)
        tagB.disabled = true;
      tagB.innerHTML = tagC;
      var tagHandler = function(){showBookmarks(tagC);};
      tagB.onclick = tagHandler;
      tagsDiv.appendChild(tagB);
    }
  }
}

function toggleTagsCloud(){
  var tagsCloud = document.querySelector('#tagsCloud');
  if (window.getComputedStyle(tagsCloud).display === 'none')
    tagsCloud.style.display = 'block';
  else
    tagsCloud.style.display = 'none';
}

// Check bookmark need to be displayed or not
function display(bm, tag, search) {
  //serch in notes, url, tags, date and title
  if (search != '') {
    const lsearch = lowCaseAndStripAccents(search.trim());
    var patt = new RegExp(lsearch, 'imu');
    if (bm.url.search(patt) !== -1) {
      return true;
    }
    if (typeof bm.notes !== 'undefined' && bm.notes.length > 0) {
      for (note of bm.notes) {
        if (note != '' && lowCaseAndStripAccents(note).search(patt) !== -1)
          return true;
      }
    }
    if (typeof bm.tags !== 'undefined' && bm.tags.length > 0) {
      for (tag of bm.tags) {
        if (tag != '' && lowCaseAndStripAccents(tag).search(patt) !== -1)
          return true;
      }
    }
    if (typeof bm.title !== 'undefined' && bm.title != '')
      if (lowCaseAndStripAccents(bm.title).search(patt) !== -1)
        return true; 
    if (typeof bm.date !== 'undefined' && bm.date != '')
      if (bm.date.search(patt) !== -1)
        return true; 
    return false;
  }
  // show without tags
  if (tag == -1 && typeof bm.tags !== 'undefined' && bm.tags.length > 0) {
    return false;
  }
  // show only with specified tag
  if (tag != '' && tag != -1) {
    if (typeof bm.tags === 'undefined' 
        || (typeof bm.tags !== 'undefined' && bm.tags.indexOf(tag) < 0)
    )        
    return false;
  }
  return true;
}

function colexpall(state) {
  var allButtons =
      document
      .getElementById('content')
      .getElementsByTagName('ul')[0]
      .getElementsByClassName('details');
  for(var x = 0; x < allButtons.length; x++) {
    // collapse/expand all details
    var sub = allButtons[x].parentNode.getElementsByTagName('ul')[0];
    if(sub && sub != null){
      if(state == 'collapse') {
        sub.style.display = 'none';
      } else {
        sub.style.display = 'block';
      }
    }
  }
}

function createControlHeader() {
/*
<div
  id="searchBox"
  style="width: 95%; padding-top: 0.8em;">
  <input
    id="searchInput"
    type"text">
  </input>
  <button 
    id="searchBtn"
    class="colexp" 
    onClick="
      showBookmarks('', document.querySelector('#searchInput').value);
    ">
Search
  </button>
  <span id="counter" style="float: right;">?</span>
</div>
*/
  // create search box
  var searchD = document.createElement('div');
  searchD.id = 'searchBox';
  var searchI = document.createElement('input');
  searchI.id = 'searchInput';
  searchI.type = 'search';
  searchI.placeholder = 'Enter search pattern…';
  searchD.appendChild(searchI);

  var searchB = document.createElement('button');
  searchB.id = 'searchBtn';
  searchB.className = 'colexp';
  searchB.innerHTML = 'Search';
  searchB.onclick = function(){
    showBookmarks('', document.querySelector('#searchInput').value);
  };
  searchD.appendChild(searchB);
  // Enable 'Enter' on keyboard to perform search
  //document.querySelector('#searchInput')
  searchI.addEventListener('keyup', function(event) {
      event.preventDefault();
      if (event.keyCode === 13) {
        document.querySelector('#searchBtn').click();
      }
  });

  var searchC = document.createElement('span');
  searchC.id = 'counter';
  searchC.style = 'float: right;';
  searchD.appendChild(searchC);

  var contentM = document.querySelector('#content');
  contentM.insertBefore(searchD, contentM.firstChild);

/*
<div
  id="buttons
  style="width: 95%; padding-top: 0.8em;">
<p><button
  class="colexp"
  onClick="colexpall('expand')">
Expand
</button>
<button
  class="colexp"
  onClick="colexpall('collapse')">
Collapse
</button>
<button
  class="colexp"
  onClick="showBookmarks('');colexp('collapse');">
All
</button>
<button
  class="colexp"
  onClick="showBookmarks(-1)">
No tags
</button></p>
</div>
*/
  // create control buttons
  var buttonsD = document.createElement('div');
  buttonsD.id = 'buttons';

  var buttonsBexpand = document.createElement('button');
  buttonsBexpand.className = 'colexp';
  buttonsBexpand.innerHTML = 'Expand';
  buttonsBexpand.onclick = function(){
    colexpall('expand');
  };
  buttonsD.appendChild(buttonsBexpand);

  var buttonsBcollapse = document.createElement('button');
  buttonsBcollapse.className = 'colexp';
  buttonsBcollapse.innerHTML = 'Collapse';
  buttonsBcollapse.onclick = function(){
    colexpall('collapse');
  };
  buttonsD.appendChild(buttonsBcollapse);

  var buttonsBall = document.createElement('button');
  buttonsBall.className = 'colexp';
  buttonsBall.innerHTML = 'All';
  buttonsBall.onclick = function(){
    showBookmarks('');
    colexpall('collapse');
  };
  buttonsD.appendChild(buttonsBall);

  var buttonsBnoTags = document.createElement('button');
  buttonsBnoTags.className = 'colexp';
  buttonsBnoTags.innerHTML = 'No Tags';
  buttonsBnoTags.onclick = function(){
    showBookmarks(-1);
    colexpall('collapse');
  };
  buttonsD.appendChild(buttonsBnoTags);

  searchD.after(buttonsD);
/*
<div
  id="tagsCloud">
</div>
<div
    id="bmlist">
</div>
*/

  // create Tags cloud
  var tagsCloudD = document.createElement('div');
  tagsCloudD.id = 'tagsCloud';
  buttonsD.after(tagsCloudD);

  // create main bookmark list
  var bmlistD = document.createElement('div');
  bmlistD.id = 'bmlist';
  tagsCloudD.after(bmlistD);

/*
<div
  style="font-size:0.3em;"
  id="bVersion">
Version 0.4 (2023-01-16 05:19:40)
</div>
*/
  // Display version at the botom
  var versionS = document.createElement('span');
  versionS.innerHTML = bVersion;
  versionS.id = 'bVersion'
  document.querySelector('#bmlist').after(versionS);
}

function urlParams() {
  createControlHeader();
  // process url parameters
  // one of two parameters 'tag' or 'search' processed
  // The 'tag' has higher priority then 'search'.
  var urlQuery = window.location.search.substring(1);
  if(urlQuery.length > 0){
    const urlParams = new URLSearchParams(urlQuery);
    var searchF = document.querySelector('#searchInput');
    var tagP = urlParams.get('tag', '')
    if (tagP)
      searchF.value = '';
      showBookmarks(tagP);
    var searchP = urlParams.get('search','')
    if (searchP) {;
      searchF.value = searchP;
      showBookmarks('', searchP);
    }
  } else {
    showBookmarks();
    //showBookmarks(-1); // without tags
    //showBookmarks('Tag 2'); // with 'Tag 2'
    //showBookmarks('', 'google.com'); // search 'google.com'
  }
}

// Convert string to lower case and strip accents 
function lowCaseAndStripAccents(str) { 
  var r=str.toLowerCase();
  //r = r.replace(new RegExp("\\s", 'g'),""); 
  r = r.replace(new RegExp("[àáâãäå]", 'g'),"a");
  r = r.replace(new RegExp("æ", 'g'),"ae");
  r = r.replace(new RegExp("ç", 'g'),"c");
  r = r.replace(new RegExp("[èéê]", 'g'),"e");
  r = r.replace(new RegExp("[ё]", 'g'),"е"); // replace Cyrillic ё то Cyrillic е
  r = r.replace(new RegExp("[ìíîï]", 'g'),"i");
  r = r.replace(new RegExp("ñ", 'g'),"n");
  r = r.replace(new RegExp("[òóôõö]", 'g'),"o");
  r = r.replace(new RegExp("œ", 'g'),"oe");
  r = r.replace(new RegExp("[ùúûü]", 'g'),"u");
  r = r.replace(new RegExp("[ýÿ]", 'g'),"y");
  //r = r.replace(new RegExp("\\W", 'g'),"");
  ///alert(r);
  return r;
};

urlParams();
