


<template>

<div>

new Test()

<button @click="xxx()">test...</button>



</div>




</template>

<script>

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



