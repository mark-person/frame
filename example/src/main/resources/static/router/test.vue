

<template>
<div>

new Test()





</div>



<div id="modal" v-cloak>
<modal v-if="modal.showModal" :modal="modal">
test001
</modal>
</div>


</template>



<script>
var modal = base.modal("modal");
// alert(modal);
modal.show('新增',  {});

// base.showLoading();
// alert(1);

</script>



