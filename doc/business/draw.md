
去水印解决办法：

1.在文件中搜索7eba17a4ca3b1a8346，找到类似a.Jv=d[w.Jg("7eba17a4ca3b1a8346")][w.Jg("78a118b7")](d,w.um,4,4);这样结构的代码
2.将其注释，替换成a.Jv=function(){return true;};
///////////// 是a.kr

https://gojs.net/latest/download.html


https://github.com/NorthwoodsSoftware/GoJS/tree/master/samples


https://gojs.net/latest/samples/planogram.html


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



