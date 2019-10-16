
<template>
<div>

<div class="commodity-div">
	<div class="commodity-item-div" @click="view()" v-for="item in list" v-cloak>
		<div class="commodity-img-div">
			<img style="border-radius:0.4rem" src="" class="commodity-img">
		</div>
		<div class="commodity-title">{{item.testName}}</div>
	</div>
</div>

<div class="page-div" v-cloak>
	<div v-if="page.noData">0 row</div>
	<div v-if="page.more"><p>-- 上拉加载更多 --</p></div>
	<div v-if="page.loading"><p>-- 加载中... --</p></div>
	<div v-if="page.end"><p>-- 暂时就这么多了 --</p></div
</div>

<div>
</template>

<script>

var pageUrl = "auto/router/test";

window.onscroll = function(e) {base.pageScroll(e, pageUrl);}

module.exports =  {
 	name: "app",
	data:function() { return {list:[], page:base.page}},
    methods:{
        view:function() {
        	test();
        }
    },
    created:function() {
    	thisSelf = this;
    	axios.post(pageUrl, {pageNumber:1}).then(function(res) {
			thisSelf.list = res.list;
			thisSelf.page.noData = (res.list.length == 0);
		});
    }
}

</script>



