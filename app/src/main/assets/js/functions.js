/*
 * Function to show/hide page header
*/

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
}

