import networkx as nx
import matplotlib.pyplot as plt

f=open("graphNonConnex.txt", "r")

G=nx.Graph()
labels = []
i=0
for line in f:
    s=line.split(" ")
    G.add_edge(int(s[0]), int(s[1]))
    labels.append(str(i))
    i += 1

print(len(G))
pr = nx.pagerank(G,alpha=0.9)

print(pr)
pos = nx.spring_layout(G,k=0.5,iterations=20)

nx.draw(G,pos, with_labels=True)
plt.savefig("simple_path2.png") # save as png
plt.show() # display