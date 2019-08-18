/*
  Minimal wrapper for marked.js
  For set marked.js options
*/

function run_marked(body, eHlJs){

  marked.setOptions({
    gfm: true,
    xhtml: true
  });
  
  if ('true' == eHlJs) {
    marked.setOptions({
      highlight: function (code) {
        return hljs.highlightAuto(code).value;
      }
    });
  }

  Android.saveHTML(marked(body), PFN, Title);
}
