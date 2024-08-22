const bVersion = '0.16 2024-01-10T22:17:42';
const duplicateTag = '!Duplicates';
const noTagTag = '!NoTag';
const config = {};
const configKey = 'OMNBookmarkerConfig';
if (typeof packgeName !== 'undefined' && packgeName)
  configKey = packageName.replace('\.','-') + '_' + configKey;
let index = {};
let durls = [];
function analyzeDuplicates(bm){
  if(bm.url in index) {
    index[bm.url].push(bm);
  } else {
    index[bm.url] = [bm];
  }
}
function compactDuplicates(){
  for(const url in index){
    if(index[url].length < 2)
      delete index[url];
  }
  durls = Object.keys(index);
}
function displayDuplicates(){
  compactDuplicates();
  let dupCounter = 0;
  for(const url in index){
    dupCounter++;
  }
  alert("Number of duplicate records sets: " + dupCounter);
  showBookmarks(duplicateTag, '', duplicates = true);
}
function showBookmarks(onlyTag = '', search = '', duplicates = false) {
  // sort bookmarks by date (newest upper)
  bookmarks.sort((a, b) => { if (a.date > b.date) { return -1; } });
  // tag display priority on search
  if (onlyTag !== '' || search === '') {
    search = '';
    let searchF = document.querySelector('#searchInput');
    searchF.value = '';
  }
  let counter = 0;
  let allTags = [];
  let ul = document.createElement('ul');
  ul.style.height = '100%;';
  index = {};
  for (const bm of bookmarks) {
    if (bm.url !== '') {
      if (!display(bm, onlyTag, search, duplicates)) continue;
      counter++;
      analyzeDuplicates(bm);
      let li = document.createElement('li');
      let a = document.createElement('a');
      a.setAttribute('href',bm.url);
      // Set title or url as title if empty
      if (bm.title && bm.title !== '')
        a.innerHTML = bm.title;
      else
        a.innerHTML = bm.url;
      li.appendChild(a);
      let span = document.createElement('span');
      span.innerHTML = '<br/>' + bm.url;
      li.appendChild(span);
      if (bm.date && bm.date !== '') {
        li.appendChild(document.createElement('br'));
        let spanDate = document.createElement('span')
        txt = document.createTextNode('');
        txt.textContent =  bm.date;
        spanDate.appendChild(txt);
        spanDate.className = 'spanDate';
        li.appendChild(spanDate);
      }
      // find tags if exists   
      let ulin = document.createElement('ul');
      let liint = document.createElement('li');
      let divt = document.createElement('div');
      txt = document.createTextNode('');
      let btnt = document.createElement('button');
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
      txt = document.createTextNode('')
      var first = true;
      if (bm.notes && bm.notes.length > 0){
        const liinn = document.createElement('li');
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
  compactDuplicates();
  //displayDuplicates();
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
    allTags.unshift(duplicateTag);
    allTags.unshift(noTagTag);
    for (const tagC of allTags) {
      var tagB = document.createElement('button');
      if (tagC == onlyTag)
        tagB.disabled = true;
      if (tagC.startsWith('!')) {
        tagB.innerHTML = tagC.substring(1);
        tagB.classList.add('tagSpecial');
      } else {
        tagB.innerHTML = tagC;
      }
      tagB.classList.add('tag');
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
function display(bm, tag, search, duplicates) {
  //serch in notes, url, tags, date and title
  if (search != '') {
    const lsearch = stripAccents(search.trim());
    var roptions = 'mu';
    if (config['ignoreCase']) roptions += 'i';
    var patt = new RegExp(lsearch, roptions);
    if (bm.url.search(patt) !== -1) {
      return true;
    }
    if (typeof bm.notes !== 'undefined' && bm.notes.length > 0) {
      for (note of bm.notes) {
        if (note != '' && stripAccents(note).search(patt) !== -1)
          return true;
      }
    }
    if (typeof bm.tags !== 'undefined' && bm.tags.length > 0) {
      for (tag of bm.tags) {
        if (tag != '' && stripAccents(tag).search(patt) !== -1)
          return true;
      }
    }
    if (typeof bm.title !== 'undefined' && bm.title != '')
      if (stripAccents(bm.title).search(patt) !== -1)
        return true; 
    if (typeof bm.date !== 'undefined' && bm.date != '')
      if (bm.date.search(patt) !== -1)
        return true; 
    return false;
  }
  // show without tags
  if ((tag == -1 || tag == noTagTag) && typeof bm.tags !== 'undefined' && bm.tags.length > 0) {
    return false;
  }
 /* Show only duplicates */
  if (tag == duplicateTag && durls.includes(bm.url)){
    //alert('dt');
    return true;
  }
  // show only with specified tag
  if (tag != '' && tag != -1 && tag != noTagTag) {
    if (typeof bm.tags === 'undefined' 
        || (typeof bm.tags !== 'undefined' && bm.tags.indexOf(tag) < 0)
    )        
    return false;
  }
  if (duplicates && durls.includes(bm.url)){
    return true;
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

//   var buttonsBnoTags = document.createElement('button');
//   buttonsBnoTags.className = 'colexp';
//   buttonsBnoTags.innerHTML = 'No Tags';
//   buttonsBnoTags.onclick = function(){
//     showBookmarks(-1);
//     colexpall('collapse');
//   };
//   buttonsD.appendChild(buttonsBnoTags);
// 
//   var buttonsBduplicates = document.createElement('button');
//   buttonsBduplicates.className = 'colexp';
//   buttonsBduplicates.innerHTML = 'DD';
//   buttonsBduplicates.onclick = function(){
//     compactDuplicates();
//     //prompt('Duplicates:',Object.keys(index).join('\n\r'));
//     displayDuplicates();
//   };
//   buttonsD.appendChild(buttonsBduplicates);

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
  var searchF = document.querySelector('#searchInput');
  if(urlQuery.length > 0){
    const urlParams = new URLSearchParams(urlQuery);
    var configP = urlParams.get('config', '');
    if (configP) {
      configuration(configP);
    } else {
      configuration();
    }
    var tagP = urlParams.get('tag', '');
    if (tagP) {
      searchF.value = '';
      showBookmarks(tagP);
      return;
    }
    var searchP = urlParams.get('search','');
    if (searchP) {
      searchF.value = searchP;
      showBookmarks('', searchP);
      return;
    }
  }
  configuration();
  showBookmarks();
  //showBookmarks(-1); // without tags
  //showBookmarks('Tag 2'); // with 'Tag 2'
  //showBookmarks('', 'google.com'); // search 'google.com'
}

// Strip accents 
function stripAccents(str) {
  var r = str;
  if (!config['stripAccents']) return r; 
  r = r.replace(new RegExp('[àáâãäå]', 'g'),'a');
  r = r.replace(new RegExp('[ÀÁÂÃÄÅ]', 'g'),'A');
  r = r.replace(new RegExp('æ', 'g'),'ae');
  r = r.replace(new RegExp('Æ', 'g'),'Ae');
  r = r.replace(new RegExp('ç', 'g'),'c');
  r = r.replace(new RegExp('Ç', 'g'),'C');
  r = r.replace(new RegExp('[èéê]', 'g'),'e');
  r = r.replace(new RegExp('[ÈÉÊ]', 'g'),'E');
  r = r.replace(new RegExp('[ё]', 'g'),'е'); // replace Cyrillic ё то Cyrillic е
  r = r.replace(new RegExp('[Ё]', 'g'),'Е'); // replace Cyrillic Ё то Cyrillic Е
  r = r.replace(new RegExp('[ìíîï]', 'g'),'i');
  r = r.replace(new RegExp('[ÌÍÎÏ]', 'g'),'I');
  r = r.replace(new RegExp('ñ', 'g'),'n');
  r = r.replace(new RegExp('Ñ', 'g'),'N');
  r = r.replace(new RegExp('[òóôõö]', 'g'),'o');
  r = r.replace(new RegExp('[ÒÓÔÕÖ]', 'g'),'O');
  r = r.replace(new RegExp('œ', 'g'),'oe');
  r = r.replace(new RegExp('Œ', 'g'),'Oe');
  r = r.replace(new RegExp('[ùúûü]', 'g'),'u');
  r = r.replace(new RegExp('[ÙÚÛÜ]', 'g'),'U');
  r = r.replace(new RegExp('[ýÿ]', 'g'),'y');
  r = r.replace(new RegExp('[ÝŸ]', 'g'),'Y');
  return r;
};

function configuration(params = '') {
  var configDefault = {
    "stripAccents": true,
    "ignoreCase": true,
    "storage": false
  };
  configDefault['configVersion'] = bVersion;
  var configURI = {};
  if (params !== '') {
    alert('URI params: ' + params);
    try {
      if (params != 'show') 
        configURI = JSON.parse(params);
    } catch (e) {
      alert('URI Config parsing error: ' + e);
    }
  }
  var configStorage = {};
  if (localStorage != null) {
    if (localStorage[configKey]) {
      try {
        configStorage = JSON.parse(localStorage[configKey]);
        configStorage['storage'] = true;
      } catch (e) {
        alert('Storage Config parsing error: ' + e);
        configStorage['storage'] = false;
      }
    } else {
       localStorage[configKey] = JSON.stringify(configDefault);
    } 
  }
  for (const p in configDefault) {
    if (configURI.hasOwnProperty(p))
      config[p] = configURI[p];
    else if (configStorage.hasOwnProperty(p))
      config[p] = configStorage[p];
    else 
      config[p] = configDefault[p];
  }
  config['configVersion'] = bVersion;
  if (localStorage != null)
    localStorage[configKey]= JSON.stringify(config);
  if (params !== '') {
    alert(
      'Configuration\n'
    + '—'.repeat(16) +'\n'
    + 'configKey: ' + configKey + '\n'
    + 'Default: ' + JSON.stringify(configDefault, null, 2) + '\n'
    + 'Storage: '  + JSON.stringify(configStorage, null, 2) + '\n'
    + 'URI: ' + JSON.stringify(configURI, null, 2) + '\n'
    + 'Effective: '  + JSON.stringify(config, null, 2)
    );
  }
}

urlParams();
