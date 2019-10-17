//全局的 axios 默认值
axios.defaults.baseURL = baseURL;

axios.interceptors.request.use(function (config) {
	
	base.showLoading();
	 
	if (config.headers["Content-Type"] && config.headers["Content-Type"].indexOf("multipart") >= 0) {
		return config;
	}
	config.transformRequest=[function (data) {
		if (typeof data == "object")  return Qs.stringify(data, {arrayFormat:'repeat'});
         return data;
	}]
	return config;
}, function (error) {
	return Promise.reject(error);
})

axios.interceptors.response.use(function(res) {
	
	base.hideLoading();
	
	if (res.data.code >= 30000) {
		alert(res.data.msg);
		return new Promise(() => {});
	}
	return res.data;
}, function(error) {
	base.hideLoading();
	alert(JSON.stringify(error.response.data));
	return Promise.reject(error);
})

var base = {};
base.showLoading = function() {base.initLoading();_loading.show();}
base.hideLoading = function() {_loading.hide();}

base.initLoading = function() {
	if (document.getElementById("_loading")) return;
	
	document.body.insertAdjacentHTML("beforeend",'<div id="_loading"><loading v-if="showLoading"></loading></div>')
	_loading = new Vue({
		el: '#_loading',
		components: {'loading': httpVueLoader(baseURL + 'static/common/template/loading.vue?v=2')},
		data: {showLoading: false, delayLoading: true},
		methods: {
	      	show: function() {
	      		this.delayLoading = true;
				setTimeout(function() {_loading.showLoading = (_loading.delayLoading ? true : false)}, 400);
	      	},
	      	hide: function() {
	      		this.delayLoading = false;
	      		setTimeout(function() {_loading.showLoading = (_loading.delayLoading ? true : false)}, 30);
	      	}
		}
	})
}


base.page = {more:false, loading:false, end:false, noData:false, hasMore:true, currentNumber:1};

base.pageScroll = function(e, uri) {
	if (_getScrollTop() + _getWindowHeight() > _getScrollHeight() - 50) {
		if (!thisSelf.page.hasMore) return;
		
		thisSelf.page.hasMore = false;
		thisSelf.page.currentNumber++;
		
		thisSelf.page.more = false;
		thisSelf.page.end = false;
		thisSelf.page.loading = true;
		
		axios.post(uri, {pageNumber:thisSelf.page.currentNumber}).then(function(res) {
			thisSelf.page.loading = false;
			
			for (var i = 0; i < res.list.length; i++) {
				thisSelf.list.push(res.list[i]);
			}
			
			if (res.page.pageSize * res.page.pageNumber < res.page.totalRows) {
				thisSelf.page.hasMore = true;
				thisSelf.page.more = true;
			}
			else {
				thisSelf.page.end = true;
			}
	    })
	}
}






// 滚动条在Y轴上的滚动距离
function _getScrollTop(){
　　var scrollTop = 0, bodyScrollTop = 0, documentScrollTop = 0;
　　if (document.body) {
　　　　bodyScrollTop = document.body.scrollTop;
　　}
　　if (document.documentElement) {
　　　　documentScrollTop = document.documentElement.scrollTop;
　　}
　　scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
　　return scrollTop;
}
 
// 文档的总高度
function _getScrollHeight(){
　　var scrollHeight = 0, bodyScrollHeight = 0, documentScrollHeight = 0;
　　if (document.body) {
　　　　bodyScrollHeight = document.body.scrollHeight;
　　}
　　if (document.documentElement) {
　　　　documentScrollHeight = document.documentElement.scrollHeight;
　　}
　　scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
　　return scrollHeight;
}
 
// 浏览器视口的高度
function _getWindowHeight() {
　　var windowHeight = 0;
　　if (document.compatMode == "CSS1Compat") {
　　　　windowHeight = document.documentElement.clientHeight;
　　} else {
　　　　windowHeight = document.body.clientHeight;
　　}
　　return windowHeight;
}


