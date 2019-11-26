#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Nov 26 19:17:06 2019

@author: 3535008
"""

import math

f = open("sample.txt")


all_data = []
for line in f:
    all_data.append(float(line))
    
filtre_par_dec = {}

all_data.sort()

dec = len(all_data) / 10.0

res = {}
for i in range(10):
    moy = 0.0
    for j in range(math.floor(dec)):
        moy += all_data[int(i*dec)+j]
    moy = moy / math.floor(dec)
    res[i] = moy
    
print(res)