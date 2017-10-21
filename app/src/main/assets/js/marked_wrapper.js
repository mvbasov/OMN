/*
  Minimal wrapper for marked.js
  inspired by Strapdown.js
*/

function run_marked(body, eHlJs){

  var markdownEl = document.getElementsByTagName('xmp')[0];

  var markdown = "";
  if (body.length == 0)
    markdown = markdownEl.textContent;
  else
    markdown = body;


  var newNode = document.createElement('div');
  document.body.replaceChild(newNode, markdownEl);

  marked.setOptions({
    gfm: true,
  });
  
  if ('true' == eHlJs) {
    marked.setOptions({
      highlight: function (code) {
        return hljs.highlightAuto(code).value;
      }
    });
  }

  content = marked(markdown);

  newNode.innerHTML = content;
  if (body.length != 0) Android.saveHTML(content, PFN, Title);
}
