#!/bin/bash
# Pack assets for mobile development

GCOMMIT=`git rev-parse --short HEAD`
GLTAG=`git  describe --tags --abbrev=0`
GCOUNT=`git rev-list --count ${GLTAG}..HEAD`

cd app/src/main/
tar -zcvf ../../../OMN_assets.${GLTAG}-${GCOUNT}-${GCOMMIT}.tgz assets/js/highlight.pack.js assets/css/highlight.css assets/js/marked.min.js assets/fonts/google-material/MaterialIcons-Regular.woff2

