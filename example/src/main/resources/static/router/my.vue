


<template>

<div>

my my my

<button @click="xxx()">test...1</button>




<div style="height:800px">
qqq
</div>

</div>




</template>

<script>

window.onscroll = function(e) {
	alert(1);
}

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



