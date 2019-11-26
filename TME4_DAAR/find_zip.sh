#!/bin/bash

find . -name '*.zip' -exec mv {} ~/DAAR/TME4_DAAR/bibli \;
cd ~/DAAR/TME4_DAAR/bibli && unzip "*.zip"
cd ~/DAAR/TME4_DAAR/bibli && rm *.zip

