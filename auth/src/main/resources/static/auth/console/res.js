

util.getResIcon = function(resType, open) {
	if (resType == 0 && open) return "fa fa-folder-open";
	if (resType == 0) return "fa fa-folder";
	if (resType == 1) return "fa fa-list-ul";
	if (resType == 2) return "fa fa-cogs";
	return "fa fa-home";
}
util.getResTypeName = function(resType) {
	if (resType == 0) return "目录";
	if (resType == 1) return "菜单";
	if (resType == 2) return "操作";
	// -1
	return "资源"; 
}

function itemOver(obj) {
	obj.style.backgroundColor = '#E5F3FF';
	var action = obj.querySelector(".tree-action");
	if (action) {
		action.style.display = 'block';
	}
}

function itemOut(myThis, obj) {
	obj.style.backgroundColor = 'white';
	var action = obj.querySelector(".tree-action");
	if (action) { 
		action.style.display = 'none';
	}
}

function updateResPrio(id) {
	loading.show();
	axios.post("auth/res/updateResPrio", {id:id}).then(function(res) {
		tree.treeData = res.tree;
		loading.hide();
	})
}

util.topSort = function(resId, sortId) {
	var r = [];
	r.push(sortId);
	for (var i = 0; i < resId.length; i++) {
		if (sortId != resId[i]) {
			r.push(resId[i]);
		}
	}
	return r;
}

util.upSort = function(resId, sortId) {
	var r = [];
	for (var i = 0; i < resId.length; i++) {
		if (i + 1 != resId.length && sortId == resId[i + 1]) {
			r.push(resId[i + 1]);
		}
		else if (i != 0 && sortId == resId[i]) {
			r.push(resId[i - 1]);
		}
		else {
			r.push(resId[i]);
		}
	}
	return r;
}

util.downSort = function(resId, sortId) {
	var r = [];
	for (var i = 0; i < resId.length; i++) {
		if (i + 1 != resId.length && sortId == resId[i]) {
			r.push(resId[i + 1]);
		}
		else if (i != 0 && sortId == resId[i - 1]) {
			r.push(resId[i - 1]);
		}
		else {
			r.push(resId[i]);
		}
	}
	return r;
}