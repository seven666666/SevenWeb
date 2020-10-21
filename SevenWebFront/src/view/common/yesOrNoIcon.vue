<template>
  <i :class="judge == true? yes:no"></i>
</template>

<script>
export default {
  data() {
    return {
      yes: ['el-icon-check', 'lightGreen'],
      no: ['el-icon-close', 'red'],
    }
  },
  props: {
    judge: Boolean,
  },
}
</script>

<style>
.lightGreen {
  color: lightgreen;
  filter: drop-shadow(0 0 2px lightgreen);
}

.lightGreen:hover .red:hover {
  filter: drop-shadow(0 0 2px rgb(0, 0, 0)) drop-shadow(0 0 2px #4b4a4a)
    drop-shadow(0 0 2px #4b4a4a);
  -webkit-filter: drop-shadow(0 0 2px rgb(0, 0, 0)) drop-shadow(0 0 2px #4b4a4a)
    drop-shadow(0 0 2px #4b4a4a);
}

.red {
  color: red;
  filter: drop-shadow(0 0 2px red);
}
</style>
