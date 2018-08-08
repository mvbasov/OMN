/*
 * Function to show/hide page header (version for legacy build)
*/

function toggleHdrBtnVis() {
	hheader=document.getElementById(\'hidable_header\');
    btn=document.getElementById(\'title_arrow\');
    if (hheader.style.display !== \'none\') {
        hheader.style.display = \'none\';
        btn.innerHTML = \'+\';
    } else {
        hheader.style.display = \'block\';
        btn.innerHTML = \'-\';
    }
}

