
// 全局的 axios 默认值
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
 
base.page = function(url, data, param) {
	var page = new Vue({
	    el:'#page',
	    data:{
	        p:param?param:{},list:data.list,sortType:[],page:data.page
	    },
	    created:function () {
	    	this.sortType[data.page.orderName] = data.page.orderType;
	    },
	    methods:{
	    	getSortType:function(orderName) {
	    		var s = this.sortType[orderName] == undefined ? 'sort' : this.sortType[orderName];
	    		return s == 'sort' ? "fa fa-sort" : "fa fa-sort-" + s;
	    	},
	    	sort:function(orderName) {
	    		var orderType = (this.sortType[orderName] == 'asc' ? 'desc' : 'asc');
	    		for (i in this.sortType) {this.sortType[i] = "sort";}
	    		
	    		this.sortType[orderName] = orderType;
	    		this.p.orderName = orderName;
	    		this.p.orderType = orderType;
	    		this.goto();
	    	},
	    	goto:function(n) {
	    		this.p.pageNum = (n ? n : 1);
	    		this.p.pageSize = this.page.pageSize;
	            axios.post(url, this.p).then(function(res) {
	            	page.list = res.list;
	            	page.page = res.page;
	            });
	    	}
	    }
	});
	return page;
}
// okFun 和 width换一下
base.modal = function modal(id, validateFun) {
	var _modal = new Vue({
		el:'#' + id,
		components:{'modal': httpVueLoader(baseURL + 'static/common/template/modal.vue?v=5'), props: {p:Object, modal:Object}},
		data:{
	      	modal:{
	      		showModal:false,
	      		title:'',
	      		width:'38rem',
	      		okFun:function(){},
	      		ok:function(m) {if (_modal.isValid){m.okFun(_modal.p)}}
	      	},
	      	p:{}
		},
		watch: {
			/*
			p: {handler(n, o) {
				alert(JSON.stringify(n))
			},deep:true}*/
		},
		computed: {
			v: function() {
				if (typeof validateFun == "function") return validateFun(this.p);
			},
			isValid:function () {
				for (o in this.v) {
					if (this.v[o] && this.v[o] != "") return false;
				}
				return true;
			}
		},
		methods:{
			show:function(title, p, okFun, callback) {
	      		this.modal.title = title;
	      		
	      		if (p) {
	      			var cloneP = {};
	      			for (i in p) {cloneP[i] = p[i];}
	      			this.p = cloneP;
	      		}
	      		// this.p = p ? p : {};
	      		this.modal.okFun = okFun;
	      		this.modal.showModal = true;
	      		
	      		if (callback) {
	      			this.$nextTick(function() {
		      			 callback.call();
		            })
	      		}
	      	},
	      	hide:function() {
	      		this.modal.showModal = false;
	      	}
		}
	})
	return _modal;
}

base.listDict = function(code) {
	if (!dict[code]) {alert("没有找到数据字典:" + code);return;}

	var disabled = dict[code + "_disabled"];
	if (!disabled) return dict[code];
	
	var newDict = {};
	for (i in dict[code]) {
		if (disabled.split(",").indexOf(i) == -1) {
			newDict[i] = dict[code][i];
		}
	}
	return newDict;
}

base.date = function(callbackObj, name) {
	WdatePicker({onpicked:function(dp){
		callbackObj[name] = dp.cal.getNewDateStr();
	}});
}

base.time = function(callbackObj, name) {
	WdatePicker({dateFmt:'HH:mm:ss', onpicked:function(dp){
		callbackObj[name] = dp.cal.getNewDateStr();
	}});
}



var V = {};
V.notNull = function(v) {
	return v === undefined || v === '' ? '必填' : '';
}
V.isInt = function(v) {
	if (this.notNull(v) != '') return '';
	return /^[0-9]+$/.test(v) ? '' : '必须为整数';	
}
V.isNum = function(v) {
	if (this.notNull(v) != '') return '';
	return isNaN(v) ? '必须为数字' : '';
}


var util = {};



