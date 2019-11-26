
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


# 修改值


diagram.nodeTemplate =
  $(go.Node, "Auto",
    $(go.Shape, "RoundedRectangle",
      { fill: "white" },  // the default value if there is no modelData.color property
      new go.Binding("fill", "color").ofModel()),  // meaning a property of Model.modelData
    $(go.TextBlock,
      { margin: 5 },
      new go.Binding("text"))
  );
  
// start all nodes yellow
diagram.model.modelData.color = "yellow";

diagram.model.nodeDataArray = [
  { text: "Alpha" },
  { text: "Beta" }
];

diagram.undoManager.isEnabled = true;

changeColor = function(text) {  // define a function named "changeColor" callable by button.onclick
  diagram.model.commit(function(m) {
    // alternate between lightblue and lightgreen colors
    var oldcolor = m.modelData.color;
    var newcolor = (oldcolor === "lightblue" ? "lightgreen" : "lightblue");
    m.set(m.modelData, "color", newcolor);
	
	if (text) {
		m.set(diagram.model.nodeDataArray[0], "text", text);
	}
	
  }, "changed shared color");
}


# 放大缩小图片
diagram.nodeTemplate =
  $(go.Node, "Auto", {resizable: true}, 
    $(go.Shape, "RoundedRectangle", 
      { fill: "white"},  // the default value if there is no modelData.color property
      new go.Binding("fill", "color").ofModel()),  // meaning a property of Model.modelData
    $(go.TextBlock,
      { margin: 5 },
      new go.Binding("text")),
     $(go.Picture, 
       { column: 0, margin: 2, 
         imageStretch: go.GraphObject.Fill }, new go.Binding("source"), new go.Binding("desiredSize",
         "desiredSize", function() {console.log(1); return diagram.findNodeForKey("aaa").desiredSize}).ofObject())
  );

