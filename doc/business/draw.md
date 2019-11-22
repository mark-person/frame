

https://gojs.net/latest/download.html

https://github.com/NorthwoodsSoftware/GoJS/tree/master/samples


gojs 生成Img 并下载
// 画布生成Img 的其他属性定义详情查看gojs api,
myDiagram.makeImage();

// 通过gojs API获取画布的img对象
img = myDiagram.makeImage({scale: 1,});
// 将图片的src属性作为URL地址
var url = img.src;
var a = document.createElement('a');
var event = new MouseEvent('click');
a.download = '下载图片名称';
a.href = url;



