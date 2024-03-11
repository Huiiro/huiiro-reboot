<template>
  <div class="msg-container">
    <div class="msg-main">
      <el-empty description="没有更多消息啦！" v-if="!hasData"/>
      <template v-if="hasData">
        <ul v-infinite-scroll="getMyMessage"
            :infinite-scroll-disabled="disabled"
            class="infinite-list" style="overflow: auto">
          <li v-for="i in tableData" :key="i" class="infinite-list-item">
            <div>
              <div class="user-info">
                <el-avatar :src="i.sendUserAvatar"></el-avatar>
                {{ i.sendUserName }}
              </div>
              <div class="message">{{ i.message }}</div>
              <div class="btn-container">
                {{ i.createTime }}
                <el-icon @click="handleClickDelete(i.messageId)">
                  <Delete/>
                </el-icon>
              </div>
            </div>
          </li>
        </ul>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import {deleteMessage, getMessageListMy} from "@/api/system/message/index.js";
import {computed, onMounted, ref} from "vue";
import {paramBuilder} from "@/utils/common.ts";
import {Delete} from "@element-plus/icons-vue";
import {ElMessageBox} from "element-plus";

onMounted(() => {
  getMyMessage();
});

const loading = ref(false);
const tableData = ref([]);
const noMore = computed(() => count.value >= page.value.total)
const disabled = computed(() => loading.value || noMore.value)
const page = ref({
  current: 1,
  size: 10,
  total: 0
});
const count = ref(0);
const hasData = ref(false);
const getMyMessage = async () => {
  try {
    loading.value = true;
    const response = await getMessageListMy(paramBuilder({}, page.value, null, null));
    if (response.data.total > 0) {
      hasData.value = true;
    }
    tableData.value = [...tableData.value, ...response.data.data];
    count.value = page.value.current * page.value.size;
    page.value.total = response.data.total;
    page.value.current++;
  } catch (error) {
  } finally {
    loading.value = false;
  }
}

const handleClickDelete = (id: number) => {
  ElMessageBox.confirm(
      '删除后无法恢复，是否确认？',
      '提示', {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
  ).then(() => {
    let ids: any = [];
    ids.push(id);
    deleteMessage(ids).then(() => {
      ids.forEach(i => {
        const index = tableData.value.findIndex(item => item.messageId === i);
        if (index !== -1) {
          tableData.value.splice(index, 1);
        }
      });
    });
  }).catch();
}
</script>

<style lang="scss">
.msg-container {
  display: flex;
  justify-content: center;
  padding: 0;
}

.msg-main {
  width: 70%;
}

.infinite-list {
  background-color: #ffffff;
  height: calc(100vh - $header-height);
  padding: 0;
  margin: 0 0 10px;
  list-style: none;
}

.infinite-list .infinite-list-item {
  display: flex;
  min-height: 120px;
  background: #ffffff;
  margin: 20px;
  border-bottom: 1px solid #d0d0d0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
  padding-bottom: 20px
}

.btn-container {
  margin-top: 20px;
  margin-bottom: 10px;
  gap: 20px;
  display: flex;
  align-items: center;

}
</style>