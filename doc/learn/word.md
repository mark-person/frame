
* location是从locate(确定某某的位置)变来的,强调的是地点、位置。
* position : 多指物体相对于其他物体所处的位置或状态。 Where would be the best position for the lights?
* spot : 指相对较小的特定地点或事物所在地。He showed me the exact spot where he had asked her to marry him.


* Palette : 调色版


* accordion 手风琴 jQuery Accordion插件用于创建折叠菜单

* Panel 面板组件

*
GraphObject
Panel
Part
Node
Group
* A Node is a Part that may connect to other nodes with Links, or that may be a member of a Group.
* Many of the simplest nodes just consist of a Panel of type Panel.Auto with a Shape surrounding a TextBlock.

```javascript
  diagram.nodeTemplate =
    $(go.Node, "Auto",
      $(go.Shape, "Rectangle",
        new go.Binding("fill", "color")),
      $(go.TextBlock,
        { margin: 5 },
        new go.Binding("text", "key"))
    );

  diagram.model.nodeDataArray = [
    { key: "Alpha", color: "lightblue" }
  ];
 ```
 
 
 Fixed-size nodes
```
 $(go.Node, "Auto",
      { desiredSize: new go.Size(100, 50) },  // on Panel
      $(go.Shape,...
      
$(go.Node, "Auto",
      $(go.Shape,
        { desiredSize: new go.Size(100, 50) },  // on main element, not on Panel
```

* stacked content 层积
$(go.Node, "Vertical", ...
$(go.Node, "Horizontal", ...

*
$(go.Shape,  // the ribbon itself
 {geometryString: "F1 M0 0 L30 0 70 40 70 70z",
  fill: "red", stroke: null, strokeWidth: 0 }),


* >>>>>>>>>>>>>> Grid Patterns
Grids are implemented using a type of Panel, Panel.Grid. Grid Panels, like most other types of Panels, can be used within Nodes or any other kind of Part. However when they are used as the Diagram.grid, they are effectively infinite in extent.

Unlike in other kinds of Panels, Grid Panel elements must be Shapes that are only used to control how the grid lines or grid bars are drawn.

>>> Default Grid  
To display a grid pattern in the background of the diagram, you can just make the Diagram.grid visible:

```
diagram.grid.visible = true;

diagram.nodeTemplate =
    $(go.Node, "Auto",
      $(go.Shape, "Rectangle", { fill: "lightgray" }),
      $(go.TextBlock, { margin: 5},
        new go.Binding("text", "key"))
    );
var nodeDataArray = [
   { key: "Alpha" }, { key: "Beta" }, { key: "Gamma" }
];
diagram.model = new go.GraphLinksModel(nodeDataArray);

```
>>> Grid Snapping 
snapping:移到某位置;
The DraggingTool and ResizingTool can change their behavior based on the background grid pattern, if you set the DraggingTool.isGridSnapEnabled and/or ResizingTool.isGridSnapEnabled properties to true.

Setting DraggingTool.isGridSnapEnabled to true will not affect disconnected Links, but these can snap if you define a custom Part.dragComputation to do so on the Link template.

```
  diagram.grid.visible = true;
  diagram.toolManager.draggingTool.isGridSnapEnabled = true;
  diagram.toolManager.resizingTool.isGridSnapEnabled = true;
```
>>> Simple Grid Customization
You can change the size of the grid cell by setting Panel.gridCellSize:

``` 
  diagram.grid.visible = true;
  diagram.grid.gridCellSize = new go.Size(30, 20);
  diagram.toolManager.draggingTool.isGridSnapEnabled = true;
  diagram.toolManager.resizingTool.isGridSnapEnabled = true;
```































