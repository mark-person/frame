

<template>
<div class="modal-mask">
<div class="modal-wrapper">
	<div class="modal-container" :style="'width:'+modal.width">
		<div class="modal-header"><h3>{{modal.title}}</h3></div>
		<div class="modal-body"><slot name="body"></slot></div>
		<div class="modal-footer">&nbsp;
			<div class="modal-default-button">
				<button class="v-button-primary" v-if="typeof modal.okFun == 'function'" @click="modal.ok(modal)">确定</button>
				<button class="v-button" @click="modal.showModal = false">关闭</button>
			</div>
		</div>
	</div>
</div>
</div>
</template>


<script>
module.exports = {
	props: {modal:Object,p:Object}
}
</script>