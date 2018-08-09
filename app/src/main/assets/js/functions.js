/*
 * Function to show/hide page header
*/

function toggleHdrBtnVis() {
	hheader=document.getElementById('hidable_header');
    btn=document.getElementById('title_arrow');
    if (hheader.style.display !== 'none') {
        hheader.style.display = 'none';
        btn.innerHTML = '<i class=\"material-icons\">arrow_drop_down</i>';
    } else {
        hheader.style.display = 'block';
        btn.innerHTML = '<i class=\"material-icons\">arrow_drop_up</i>';
    }
}

