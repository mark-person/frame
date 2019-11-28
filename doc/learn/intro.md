

# Commands 
Commands such as Delete or Paste or Undo are implemented by the CommandHandler class.
Keyboard events, like mouse and touch events, always go to the Diagram.currentTool.


# Groups


treat : v.	以…态度对待; 以…方式对待; 把…看作; 把…视为; 处理; 讨论; 治疗;
Use the Group class to treat a collection of Nodes and Links as if they were a single Node. 
Those nodes and links are members of the group; together they constitute a subgraph.

constitute:  v.	(被认为或看做) 是; 被算作; 组成; 构成; (合法或正式地) 成立，设立;


Simple Groups
In a GraphLinksModel the Model.nodeDataArray holds node data, each of which might be represented by a Group rather than by a regular Node. You can declare that it should be a group by setting the isGroup data property to true. You can declare that a node data be a member of a group by referring to the group's key as the group data property value.


```
  diagram.model.nodeDataArray = [
    { key: "Alpha", isGroup: true },
    { key: "Beta", group: "Alpha" },
    { key: "Gamma", group: "Alpha", isGroup: true },
    { key: "Delta", group: "Gamma" },
    { key: "Epsilon", group: "Gamma" },
    { key: "Zeta", group: "Alpha" },
    { key: "Eta", group: "Alpha", isGroup: true},
    { key: "Theta", group: "Eta" }
  ];

```

# Validation
* Linking Validation
There are a number of GraphObject properties that let you control what links the user may draw or reconnect. These properties apply to each port element and affect the links that may connect with that port.

Linkable properties
The primary properties are GraphObject.fromLinkable and GraphObject.toLinkable. If you do not have a Node containing an element with fromLinkable: true and another node with toLinkable: true, the user will not be able to draw a new link between the nodes.

```
diagram.nodeTemplate =
    $(go.Node, "Auto",
      new go.Binding("location", "loc", go.Point.parse),
      $(go.Shape, "Ellipse",
        { fill: "green", portId: "", cursor: "pointer" },
        new go.Binding("fromLinkable", "from"),
        new go.Binding("toLinkable", "to")),
      $(go.TextBlock,
        { stroke: "white", margin: 3 },
        new go.Binding("text", "key"))
    );

  var nodeDataArray = [
    { key: "From1", loc: "0 0", from: true },
    { key: "From2", loc: "0 100", from: true },
    { key: "To1", loc: "150 0", to: true },
    { key: "To2", loc: "150 100", to: true }
  ];
  var linkDataArray = [
    // initially no links
  ];
  diagram.model = new go.GraphLinksModel(nodeDataArray, linkDataArray);
```




# OneWay and TwoWay Bindings
By default bindings are Binding.OneWay. OneWay bindings are evaluated when the Panel.data property is set or when you call Panel.updateTargetBindings or Model.setDataProperty. OneWay bindings only transfer values from the source to the target.

TwoWay bindings are evaluated in the source-to-target direction just as OneWay bindings are evaluated. However when the GraphObject target property is set, the TwoWay bindings are evaluated in the target-to-source direction. There is no point in having a TwoWay binding on a GraphObject property that cannot be set. For efficiency, avoid TwoWay bindings on GraphObject properties that do not change value in your app.

unintentionally : 无意中
indeterminate : 模糊的; 不确定的; 难以识别的;































