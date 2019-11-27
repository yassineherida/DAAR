#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Nov 26 19:17:06 2019

@author: 3535008
"""

import math
import networkx as nx
import matplotlib.pyplot as plt

filename = "resBetweeness"
f = open(filename)
fOut = open(filename + ".out", "w")



all_data = []
i=0
for line in f:
    all_data.append(float(line))
    i = i+1
    
filtre_par_dec = {}

all_data.sort()

dec = len(all_data) / 10.0

res = {}
for i in range(10):
    moy = 0.0
    print(math.floor(dec))
    for j in range(int(math.floor(dec))):
        moy += all_data[int(i*dec)+j]
    moy = moy / math.floor(dec)
    res[i] = moy
    fOut.write(str(i) + " " + str(moy)+"\n")

fOut.close()
f.close()
print(res)