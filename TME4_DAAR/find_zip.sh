#!/bin/bash

#wget -w 2 -m -H "http://www.gutenberg.org/robot/harvest?filetypes[]=txt&langs[]=en"
find . -name '*.zip' -exec mv {} $1 \;
cd $1 && unzip "*.zip"
cd $1 && rm *.zip

