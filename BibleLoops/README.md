# Bible Loops

Sample graph created with Bible Loops:

<img src="https://github.com/gregdott/public/blob/main/BibleLoops/binary%20sample%202.png?raw=true" width="800">


❗NOTE❗
This is very much in progress at the moment. Anyone happening upon this may find it in a partially working state.

The Bible has 31 102 verses in it. There is a file on openbible.info that contains 344 790 cross references. This is a gigantic graph. It's hard to say where this project is going, and it might have little point to it, but it will be interesting for me nonetheless. At the very least, it will provide an interesting way to explore the Bible.

The thing that inspired this idea was the visual that Chris Harrison created using 63 779 cross references in the Bible. You can find out more about this on his website: https://www.chrisharrison.net/index.php/Visualizations/BibleViz. The image is given below:

<img src="https://chrisharrison.net/projects/bibleviz/BibleVizArc7WiderOTNTsmall.png" width="800">

When I saw that I immediately thought of graphs (this type: https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)). I wanted to see if there was anything interesting to be found by modelling this data as a graph and then exploring it in different ways. I have a number of half-formed ideas that I will be experimenting with as I go along with this project. Given the limits of computing power, constructing the entire graph would be unnecessarily time consuming. Instead, my investigations will focus on creating and exploring sub-graphs of this data set.

There are also a number of different ways that this data can be modelled as a graph. The first, most obvious one is that each verse is a node/vertex and each cross reference is an edge. To have more meaningful results, I might end up modelling collections of verses as vertices. 

I'm interested in applying certain ideas from graph theory to this data set and seeing if anything interesting arises. 

For example, the notion of a cycle: "In graph theory, a cycle in a graph is a non-empty trail in which only the first and last vertices are equal." It will certainly be possible to find cycles in the data set. Will they contain anything useful regarding their content and its relations?

Also, is it possible to find isomorphic subgraphs, and is there some shared idea given their shared structure?

etc...

To be completely honest, this may be a futile task, bordering on the limits of sanity. However, if nothing meaningful is achieved through this, I will have at least improved my programming skills along with my knowledge of the Biblical stories and graph theory.

The data used in this project is from the following two files:

kjv.txt: acquired from https://github.com/pstephens/kingjames.bible/tree/master/kjv-src/www.staggs.pair.com-kjbp

cross_references.txt: acquired from https://www.openbible.info/labs/cross-references/

## The Path Forward

So far, the following has been implemented:
- Data import to mongodb (this is to speed up graph creation so that we aren't reading through very long files every time we want to map a connection)
- Constructing basic graphs from the db data

Busy with:
- Different ways of constructing graphs (depth-first, breadth-first, magnetism and whatever else I can think of)
- Visual interface for exploring graphs (will be a separate custom built lib for graph visualisation - I have some other uses for this in mind, that's why it will be a separate lib - eventually 😂)

Next to implement:
- Different ways of exploring graphs
- Calculating Magnetism. This will be the node weight. Magnetism will be derived from the degree of a node and the degrees of the nodes connected to it. Need to flesh this out properly.



