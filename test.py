from ghost import Ghost
from ghost.ghost import Session
import os
from __builtin__ import len

def readAddresses(fileLocation="list.txt"):
    f = open(fileLocation,'r')
    addresses = []
    while (True):
        a = f.readline()
        if (a == ""):
            break
        if (a.endswith('\n')):
            a = a[0:len(a)-1]
        if (a[0:11]!="http://www."):
            a = "http://www."+a
        addresses.append(a)
    return addresses

def prune(tree):
    if (tree==[]):
        return []
    if (tree[0]==0 or tree[1]==0):
        return []
    ptree = []
    for x in range(0,6):
        ptree.append(tree[x])
    for y in range(6,6+int(tree[5])):
        var0 = prune(tree[y])
        if (var0!=[]):
            ptree.append(var0)
        continue
    ptree[5] = len(ptree)-6
    return ptree


def parseSites(addressList, saveIntermediately=True):
    print str(len(addressList)) +" addresses will be searched"
    # The ghost window uses a browser similar to Chrome/15.0.874.121
    trees = []
    nrOfErrors = 0
    ghost = Ghost()
    with ghost.start() as Session:
        print "Creating Virtual Window"
        Session.set_viewport_size(1920, 1080)
        for address in addressList:
            print "currently working on: "+address
			#Firefox: 	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0
			#IExplore: 	Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; rv:11.0) like Gecko
            Session.open(address,headers = {'Access-Control-Allow-Origin': '*'},user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
            Session.print_to_pdf(path="Page.pdf")
            print "Virtual Window opened"
            print "Preparing website to be parsed"
            #print str(Session.evaluate("var xhr = new XMLHttpRequest(); xhr.open('GET', document.styleSheets[0].href);xhr.send; xhr.readyState")[0])
            Session.evaluate_js_file("pre.js","UTF-8")
            print "Parsing site into tree"
            tree = Session.evaluate_js_file("test.js", "UTF-8")[0]
            Session.print_to_pdf("file.pdf")
            if (tree == None):
                print "Tree could not be created."
                nrOfErrors+=1
                continue
            print "Tree created"
            tuple = (prune(tree),address)
            print "Tree pruned"
            trees.append(tuple)
            print "Tree appended"
            if (saveIntermediately):
                print "Saving trees to database"
                saveToFile(trees)
                print "Trees saved to database"
        print "All sites have been parsed"
    
    if (not saveIntermediately):
        print "Saving trees to database"
        saveToFile(trees)
        print "Trees saved to database"
    print str(nrOfErrors) + " errors have been encountered"
    print "All sites have been succesfully parsed and saved to the database."
    
def toString(tree, pre):
    out = str(tree[0])+","+str(tree[1])+","+str(tree[2])+","+str(tree[3])+","+tree[4]+","+str(tree[5])+":"
    
    if (tree[5]==0):
        return out;
    for x in range(6,6+int(tree[5])):
        out = out+"\n"+pre+toString(tree[x],"| "+pre)   
    return out;

def saveToFile(trees):
    os.remove("DB.txt")
    f = open("DB.txt",'w')
    for (tree,name) in trees:
        f.write("$<"+name+">\n")
        f.write(toString(tree, "|-"))
        f.write("\n")
    f.close()

addresses = readAddresses("list - Singular.txt")
#addresses = readAddresses()
#["http://www.html5rocks.com/en/tutorials/developertools/part1/","http://sites.cs.queensu.ca/tr/"]
parseSites(addresses)
 