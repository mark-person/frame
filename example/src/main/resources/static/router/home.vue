
<template>

<div class="commodity-div">
	<div class="commodity-item-div" @click="view()" v-for="item in list" v-cloak>
		<div class="commodity-img-div">
			<img style="border-radius:0.4rem" src="" class="commodity-img">
		</div>
		<div class="commodity-title">{{item.testName}}</div>
	</div>
</div>

<div v-cloak>
	<div v-if="page.noData">0 row</div>

	<div v-if="page.more"><p>-- 上拉加载更多 --</p></div>
	<div v-if="page.loading"><p>-- 加载中... --</p></div>
	<div v-if="page.end"><p>-- 暂时就这么多了 --</p></div>
</div>

</template>

<script>


alert(1);
window.onscroll = function(e) {
	base.pageScroll(e, "base/router/test");
}

module.exports =  {
 	name: "app",
	data:function() {
        return {list:[], page:{more:false, loading:false, end:false, noData:false, hasMore:true, currentNumber:1}}
    },
    methods:{
        xxx:function() {
        }
    },
    created:function() {
    	thisSelf = this;
    	axios.post("base/router/test", {pageNumber:1}).then(function(res) {
			thisSelf.list = res.list;
			thisSelf.page.noData = (res.list.length == 0);
		});
    }
}

</script>



