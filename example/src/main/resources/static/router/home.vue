
<template>

<div class="commodity-div">
	<div class="commodity-item-div" @click="view()" v-for="item in list" v-cloak>
		<div class="commodity-img-div">
			<img style="border-radius:0.4rem" src="" class="commodity-img">
		</div>
		<div class="commodity-title">{{item.testName}}</div>
	</div>
</div>

<div id="pageMsg" v-cloak>
	<div v-if="noData">0 row</div>

	<div v-if="pageMore"><p>-- 上拉加载更多 --</p></div>
	<div v-if="pageLoading"><p>-- 加载中... --</p></div>
	<div v-if="pageEnd"><p>-- 暂时就这么多了 --</p></div>
</div>

</template>

<script>

var pageHasMore = true;
var currentPageNumber = 1;
window.onscroll = function(e) {
	var e = e || window.event;
	var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
	
	if (scrolltop + document.body.clientHeight >= window.innerHeight - 50) {
		if (!pageHasMore) return;
		
		pageHasMore = false;
		currentPageNumber++;
		
		thisSelf.pageMore = false;
		thisSelf.pageEnd = false;
		thisSelf.pageLoading = true;
		
		axios.post("base/router/test", {pageNumber:currentPageNumber}).then(function(res) {
			thisSelf.pageLoading = false;
			
			for (var i = 0; i < res.list.length; i++) {
				thisSelf.list.push(res.list[i]);
			}
			
			if (res.page.pageSize * res.page.pageNumber < res.page.totalRows) {
				pageHasMore = true;
				thisSelf.pageMore = true;
			}
			else {
				thisSelf.pageEnd = true;
			}
	    })
	}
}

module.exports =  {
 	name: "app",
	data:function() {
        return {list:[], pageMore:false, pageLoading:false, pageEnd:false, noData:false}
    },
    methods:{
        xxx:function() {
        }
    },
    created:function() {
    	thisSelf = this;
    	axios.post("base/router/test", {pageNumber:1}).then(function(res) {
			thisSelf.list = res.list;
			thisSelf.noData = (res.list.length == 0);
		});
    }
}

</script>



