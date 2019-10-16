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




base.pageScroll = function(e, uri) {
	var e = e || window.event;
	var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
	
	if (scrolltop + document.body.clientHeight >= window.innerHeight - 50) {
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

