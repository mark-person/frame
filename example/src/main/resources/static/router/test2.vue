


<template>

<div>

new Test()

<button @click="xxx()">test...222222</button>




<div style="height:800px">
qqq
</div>

</div>




</template>

<script>

window.onscroll = function(e) {}

module.exports =  {
    name: "app",
    data:function(){
        return {
            name:'tom'
        }
    },
    methods:{
        xxx:function(){
            var testModal = base.modal("testModal");
			testModal.show('');
        }
    }
}

//var testModal = base.modal("testModal");
//testModal.show('');


// alert(modal);


// base.showLoading();
// alert(1);

</script>



