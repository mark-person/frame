


<template>


<div id="pageDiv">
	<div class="commodity-div" style="border:1px solid #ddd;margin:-2px;">
		<div class="commodity-item-div" @click="view()" v-for="item in list" v-cloak>
			<div class="commodity-img-div">
				<img style="border-radius:0.4rem" src="" class="commodity-img">
			</div>
			<div class="commodity-title">{{item.testName}}</div>
		</div>
	</div>
	<div id="pageMsg" v-cloak>
		<div id="pageMore" v-if="pageMore"><p>-- 上拉加载更多 --</p></div>
		<div id="pageLoading" v-if="pageLoading"><p>-- 加载中... --</p></div>
		<div id="pageEnd" v-if="pageEnd"><p>-- 暂时就这么多了 --</p></div>
	</div>
</div>





</template>

<script>

var pageHasMore = true;
var currentPageNumber = 1;
window.onscroll = function(e) {
	var pageDiv = thisSelf;
	
	var e = e || window.event;
	var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
	
	if (scrolltop + document.body.clientHeight >= window.innerHeight - 50) {
		if (pageHasMore) {
			pageHasMore = false;
			currentPageNumber++;
			
			pageDiv.pageMore = false;
			pageDiv.pageEnd = false;
			pageDiv.pageLoading = true;
			
			axios.post("auto/router/test", {pageNumber:currentPageNumber}).then(function(res) {
				
				if (currentPageNumber == 1) {
					pageDiv.list = res.list;
				}
				else {
					for (var i = 0; i < res.list.length; i++) {
						pageDiv.list.push(res.list[i]);
					}
				}
				
				pageDiv.pageMore = false;
				pageDiv.pageEnd = false;
				pageDiv.pageLoading = false;
				
				if (res.page.pageSize * res.page.pageNumber < res.page.totalRows) {
					pageHasMore = true;
					pageDiv.pageMore = true;
				}
				else {
					pageDiv.pageEnd = true;
				}
				
		    });
		}
	}
}


module.exports =  {
 	name: "app",
	data:function() {
        return {
            list:[], pageMore:false, pageLoading:false, pageEnd:false
        }
    },
    methods:{
        xxx:function() {
        }
    },
    created:function() {
    	thisSelf = this;
    	axios.post("auto/router/test", {pageNumber:1}).then(function(res) {
			thisSelf.list = res.list;
		});
    }
}


</script>



