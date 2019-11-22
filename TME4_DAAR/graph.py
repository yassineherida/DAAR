import networkx as nx
import matplotlib.pyplot as plt

f=open("graph.txt", "r")

G=nx.Graph()
for line in f:
    s=line.split(" ")
    G.add_edge(int(s[0]), int(s[1]))
nx.draw(G)
plt.savefig("simple_path.png") # save as png
plt.show() # display