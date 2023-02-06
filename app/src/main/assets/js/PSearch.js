var sVersion = '0.2 2023-01-28 12:32:18';
var injected = false;
function injectSearch() {
  var form = document.createElement('div');
  var inputSearch = document.createElement('input');
  var btnPrev = document.createElement('button');
  var btnNext = document.createElement('button');
  var btnTop = document.createElement('button');
  var btnHide = document.createElement('button');
  var btnButtom = document.createElement('button');
  //var nbsp = document.createTextNode('\u00a0');
  //var nbsp2 = document.createTextNode(String.fromCharCode(160));
  form.id = 'search_form';
  form.style.display = 'none';
  inputSearch.id = 'search_input';
  inputSearch.type = 'search';
  inputSearch.placeholder = 'Enter text to search here...';
  // Enable 'Enter' on keyboard to perform search
  inputSearch.addEventListener('keyup', function(event) {
      event.preventDefault();
      if (event.keyCode === 13) {
        document.querySelector('#btnNext').click();
      }
  });
  btnPrev.innerText = '\u25C0';
  btnPrev.type = 'button';
  btnPrev.onclick = function() {
    if (lastpos>0) lastpos--;
    var searchFor = document.getElementById('search_input').value
    doSearch(searchFor, lastpos);  
  }
  btnPrev.className = 'b05';
  btnNext.id = 'btnNext';
  btnNext.innerText = '\u25B6';
  btnNext.type = 'button';
  btnNext.onclick = function() {
    lastpos++;
    var searchFor = document.getElementById('search_input').value;
    doSearch(searchFor, lastpos);
    //alert('Fire!' + lastpos);
  }
  btnNext.className = 'b05';
  btnTop.innerText = '\u25B2';
  btnTop.type = 'button';
  btnTop.onclick = function() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
  btnTop.className = 'b03';
  btnHide.innerText = '\u2716';
  btnHide.type = 'button';
  btnHide.onclick = function() {
          document.querySelector('#search_form').style = 'display: none;';
  }
  btnHide.className = 'b03';
  btnButtom.innerText = '\u25BC';
  btnButtom.type='button';
  btnButtom.onclick = function() {
      var cnt = document.querySelector('#content');
      window.scroll({ top: cnt.scrollHeight, behavior: 'smooth' });
  }
  btnButtom.className = 'b03';
  // make search form from elements
  form.appendChild(btnTop);
  form.appendChild(btnHide);
  form.appendChild(btnButtom);
  form.appendChild(inputSearch);
  form.appendChild(btnPrev);
  form.appendChild(btnNext);
  // inject search form to the top of content
  var content = document.querySelector('#content');
  content.insertBefore(form, content.firstChild);
  // create find button and injct it to page header
  var btnFind = document.createElement('button');
  btnFind.id = 'btnFind';
  btnFind.innerText = '\uD83D\uDD0E';
  //btnFind.style = 'float: right; font-size: 1.2em';
  btnFind.onclick = function() {
    var formState = document.querySelector('#search_form');
      if (formState.style.display === 'none')
        formState.style.display = 'block';
      else
        formState.style.display = 'none';
  }
  document.querySelector('#pageTitle').style.display = 'inline-block';
  document.querySelector('#pageTitle').style.width = '85%';
  document.querySelector('#ptitle').appendChild(btnFind);
}

// https://stackoverflow.com/questions/5886858/full-text-search-in-html-ignoring-tags
var lastpos = -1; // 1 because 0 in input area
function doSearch(text, pos) { 
  if (!window.find || !window.getSelection) return;
  document.designMode = "on"; 
  var sel = window.getSelection();
  sel.collapse(document.body, 0);
  while (window.find(text) && pos > 0 ) {
    //document.execCommand("HiliteColor", false, "yellow");
    sel.collapseToEnd();
    pos--;
    if (pos==0){
      document.execCommand("HiliteColor", false, "#00ff00");
    }
  }
  if (!window.find(text)) lastpos--;
  document.designMode = "off"; 
}

function urlParams() {
  injectSearch();
  // process url parameters
  var urlQuery = window.location.search.substring(1);
  if(urlQuery.length <= 0) return;
  const urlParams = new URLSearchParams(urlQuery);
  var searchP = urlParams.get('search','');
  if (searchP) {
    document.querySelector('#search_input').value = searchP;
    document.querySelector('#btnNext').click();
  }
}

urlParams();


