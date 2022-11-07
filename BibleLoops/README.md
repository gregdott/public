This readme needs formatting and editing. These are my rough thoughts for now.
============================================================================================================================= 
 This is potentially a work of insanity, but I think it should be interesting. I doubt I'm the first to try
 something like this and others have probably given it a better go than I will be able to. Nevertheless, I find this idea
 quite compelling, so I'm going to see where it leads to. The thing that inspired this idea was Chris Harrison's visualisation
 of a number of cross references in the Bible.
 
 I have 2 files: kjv.txt and cross_references.txt. kjv.txt contains the King James Version Bible, verse by verse.
 cross_references.txt contains 340 000 cross references found in the Biblical Scriptures.
 
 kjv.txt I acquired from https://github.com/pstephens/kingjames.bible/tree/master/kjv-src/www.staggs.pair.com-kjbp
 cross_references.txt I acquired from https://www.openbible.info/labs/cross-references/
 
 I made minor edits to each of these files:
 kjv.txt - removed some weird character from the end of the file (appeared as NUL in notepad++)
 cross_references.txt - removed the headings from the beginning of the file (so that I didn't need to put in a conditional to account for those when processing)
 Removed the last blank line from the file so that the last line contained data. Just some basic sanitising.
 
*******************************************************************************
 The idea is this:
 
 Every Bible verse is a node in a graph.
 Every cross reference is an edge linking 2 nodes.
 
 We can construct a gigantic graph representing many different possible paths through the Bible.
 
 What can we do with this?
 
 I'm not quite sure...
 I have one idea of looking for cycles in the BibleGraph.
 A cycle in a graph is when we start at one node, traverse a number of edges through a bunch of other nodes, and
 eventually end up back where we started. I want to see what kind and size of cycles can be found and if there is
 anything interesting to be found in the interconnections of the verses traversed along the way. I'm sure there 
 would have to be some conditions imposed in order to ensure we can find a reasonable length cycle.
 I don't really know how easy it would be. Lots of trial an error will be needed.
 I have an inkling that one of the ways will be to create weights for each node (verse) that relate to how many other nodes reference it
 and what weights those nodes have. These could act as hubs to ensure that we can easily find cycles. We can create metanodes for groups of nodes that 
 we know are connected. Something like that. There is a lot to think about and do.
 
 There are 31,102 verses in the bible. 31,102 verses with 344,790 cross references in the cross_references.txt file. That's a pretty big graph.

 I imagine there are probably a bunch of other interesting graph theory related things that we can do with this data.
 
 This is an exploration of where we can go with that.
 
*******************************************************************************
 
 Some cross references are kind of weak:
 Joh 4:29 Come, see a man, which told me all things that ever I did: is not this the Christ?
 Re 22:17 And the Spirit and the bride say, Come. And let him that heareth say, Come. And let him that is athirst come. And whosoever will, let him take the water of life freely.
 
 They seem to be linked simply by the word 'come'. So edges are definitely to be weighted. Forunately there is a 'vote' count with 
 each reference. Not sure what that is or what it means, but it's a decent start.
 
 Compared to:
 Ge 1:1 In the beginning God created the heaven and the earth.
 Joh 1:1 In the beginning was the Word, and the Word was with God, and the Word was God.
 
 Much higher vote count and much stronger correlation in terms of content.
 