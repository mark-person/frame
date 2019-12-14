
# async await 和promise的使用




### Puppeteer安装
npm install -g cnpm --registry=https://registry.npm.taobao.org
cnpm i puppeteer

```javascript
// baidu.js
const puppeteer = require('puppeteer');
(async () => {
    const browser = await puppeteer.launch({
        headless: true
    })
    const page = await browser.newPage()
    await page.goto('http://www.baidu.com')
    await page.screenshot({
        path: 'E:/Program Files/nodejs/baidu.png'
    })
})()
```
node baidu.js


### Puppeteer 安装
安装 Puppeteer 并不难，只需要保证你的环境上安装了 Node.js 以及能够运行 NPM。

由于官方的安装教程没有考虑到已经安装了 Chromium 的情况，我们这里使用一个第三方库 ，它能够自定义化 Puppeteer 以及管理 Chromium 的下载情况。

运行以下命令安装 Puppeteer：

的详细用法请参照官网：https://www.npmjs.com/package/puppeteer-chromium-resolver。











