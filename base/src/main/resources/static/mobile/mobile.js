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