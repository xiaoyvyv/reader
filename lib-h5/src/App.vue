<script setup lang="ts">
import {onMounted, onUnmounted, ref} from "vue";

const html = ref("");


onMounted(() => {
  window.reader = {
    render: (data: string) => {
      html.value = data;
    }
  }
});

onUnmounted(() => {
  window.reader = null;
});
</script>

<template>
  <div v-html="html">

  </div>
</template>

<style scoped>

</style>
