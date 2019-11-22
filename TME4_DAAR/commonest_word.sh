#!/bin/bash

head -n $1 "$2" | awk -F " " '{print $2}' > $3
IFS=$old
