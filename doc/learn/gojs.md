
# Tutorials
TextBlocks, Shapes, and Pictures are the primitive building blocks of GoJS.

$(go.Node, "Vertical", {locationSpot: go.Spot.Center},  new go.Binding("location", "loc"),
    $(go.Shape, ...), $(go.TextBlock, ...
    


# Kinds of Models
In order to get links into our diagram, the basic Model is not going to cut it. 
We are going to have to pick one of the other two models in GoJS, both of which support Links. These are GraphLinksModel and TreeModel. 


# Link Templates
We will construct a new Link template that will better suit our wide, boxy nodes. A Link is a different kind of Part, not like a Node. 
The main element of a Link is the Link's shape, and must be a Shape that will have its geometry computed dynamically by GoJS. 



