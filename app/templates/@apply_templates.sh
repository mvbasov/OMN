#!/bin/bash
mustache data/strings.json Help.md.mustache ../src/main/assets/md/default/Help.md
mustache data/strings_legacy.json Help.md.mustache ../src/main/assets/md/default/Help_legacy.md
mustache data/strings.json strings-v21.xml.mustache ../src/main/res/values-v21/strings.xml
sed -i 's/"material-icons"/\\"material-icons\\"/' ../src/main/res/values-v21/strings.xml
mustache data/strings_legacy.json strings.xml.mustache ../src/main/res/values/strings.xml
