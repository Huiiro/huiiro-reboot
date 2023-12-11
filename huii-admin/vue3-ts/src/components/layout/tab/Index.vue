<template>
  <el-tabs v-model="tabValue"
           class="tabs"
           type="card"
           closable
           @tab-remove="removeTab"
           @tab-click="clickTab"
  >
    <el-tab-pane
        v-for="(item) in tabList"
        :key="item.name"
        :label="item.name"
        :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import {computed} from 'vue';
import router from "@/router";
import {useUserStore} from "@/store/modules/user.ts";


const userStore = useUserStore();

let tabValue = computed({
  get() {
    return userStore.tabValue;
  },
  set(value) {
    userStore.tabValue = value;
  }
});

let tabList = computed({
  get() {
    return userStore.tabList;
  },
  set(list) {
    userStore.tabList = list;
  }
});

const removeTab = (tab: any) => {
  let tabs = tabList.value;
  let activeTab = tabValue.value;
  if (tabs.length === 1) {
    return;
  }
  if (activeTab === tab) {
    tabs.forEach((item: any, index: number) => {
      if (item.name === tab) {
        let nextTab = tabs[index + 1] || tabs[index - 1];
        if (nextTab) {
          activeTab = nextTab.name;
        }
      }
    })
  }
  tabValue.value = activeTab;
  tabList.value = tabs.filter(t => t.name !== tab);
  router.push({name: activeTab});
};

const clickTab = (tab: any) => {
  const name = tab.props.name;
  tabValue.value = name;
  const find = tabList.value.find(i => i.name == name);
  if (router.currentRoute.name !== name) {
    router.push({name: name, params: find.params});
  }
};

</script>

<style scoped lang="scss">
.tabs {
  user-select: none;
}

.el-tabs {
  --el-tabs-header-height: $c-tab-height;
  border: none;
}

.el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
  border-bottom: none;
}

:deep(.el-tabs__item) {
  padding: 0 24px;
  height: calc($c-tab-height - 2px);
  line-height: calc($c-tab-height - 2px);
  font-size: 14px;
}

:deep(.el-tabs__item.is-active) {
  color: $global-import-color;
}

:deep(.el-tabs__item:hover) {
  color: $global-import-color;
}
</style>