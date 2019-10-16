


<template>

<div>

<a @click="back()" href="javascript:this">back</a>

view001

</div>




</template>

<script>

window.onscroll = function() {}

module.exports =  {
    name: "app",
    data:function(){
        return {
            name:'tom'
        }
    },
    methods:{
        back:function() {
        	goBack();
        }
    }
}

</script>



