# Bible Loops

The KJV Bible has 31 102 verses in it. I found a file on openbible.info that contains 344 790 cross references. What we have is a gigantic graph. I'm not sure where this project is going, and it might have little point to it, but it will be interesting for me nonetheless.

The thing that inspired this idea was the visual that Chris Harrison created using 63 779 cross references in the Bible. You can find out more about this on his website: https://www.chrisharrison.net/index.php/Visualizations/BibleViz. The image is given below:

![graphic representation of Biblical cross references](https://chrisharrison.net/projects/bibleviz/BibleVizArc7WiderOTNTsmall.png)

When I saw that I immediately thought of graphs (this type: https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)). I wanted to see if there was anything interesting to be found by modelling this data as a graph and then exploring it in different ways. I have a number of half-formed ideas that I will be experimenting with as I go along with this project. Given the limits of computing power, constructing the entire graph would be unnecessarily time consuming. Instead, my investigations will focus on creating and exploring sub-graphs of this data set.

There are also a number of different ways that this data can be modelled as a graph. The first, most obvious one is that each verse is a node/vertex and each cross reference is an edge. To have more meaningful results, I might end up modelling collections of verses as vertices. 

I'm interested in applying certain ideas from graph theory to this data set and seeing if anything interesting arises. 

For example, the notion of a cycle: "In graph theory, a cycle in a graph is a non-empty trail in which only the first and last vertices are equal." It will certainly be possible to find cycles in the data set. Will they contain anything useful regarding their content and its relations?

Also, is it possible to find isomorphic subgraphs, and is there some shared idea given their shared structure?

To be completely honest, this may be a futile task, bordering on the limits of sanity. However, if nothing meaningful is achieved through this, I will have at least improved my programming skills along with my knowledge of the Biblical stories and graph theory.

The data used in this project is from the following two files:

kjv.txt: acquired from https://github.com/pstephens/kingjames.bible/tree/master/kjv-src/www.staggs.pair.com-kjbp

cross_references.txt: acquired from https://www.openbible.info/labs/cross-references/

## The Path Forward

So far, the following has been implemented:
- Data import to mongodb (this is to speed up graph creation so that we aren't reading through very long files every time we want to map a connection)

Next to implement:
- Constructing basic graphs from the db data
- Visual interface for exploring graphs (will be a separate custom built lib for graph visualisation - I have some other uses for this in mind, that's why it will be a separate lib)



