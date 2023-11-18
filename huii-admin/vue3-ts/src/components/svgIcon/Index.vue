<template>
  <svg :class="svgClass" :style="{ color, width, height }">
    <use :xlink:href='iconName'/>
  </svg>
</template>

<script setup lang="ts">
import {computed} from 'vue'

const props = defineProps({
  prefix: {
    type: String,
    default: '#icon-'
  },
  name: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: ''
  },
  width: {
    type: String,
    default: ''
  },
  height: {
    type: String,
    default: ''
  }
})
const iconName = computed(() => {
  return `${props.prefix + props.name}`
})
const svgClass = computed(() => {
  if (props.name) return `svg-icon icon-${props.name}`
  return 'svgIcon-icon'
})
</script>


<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: middle;
}
</style>