<template>
  <div id="ssss"> </div>
  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleQuery()">
                  查詢
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  編輯
                </a-button>
                <a-popconfirm
                    title="刪除後不可恢復，確認刪除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    刪除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名稱"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="請選擇父文檔"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="順序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 內容預覽
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>

  <!--<a-modal-->
  <!--  title="文檔表單"-->
  <!--  v-model:visible="modalVisible"-->
  <!--  :confirm-loading="modalLoading"-->
  <!--  @ok="handleModalOk"-->
  <!--&gt;-->
  <!--  -->
  <!--</a-modal>-->
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, createVNode } from 'vue';
import axios from 'axios';
import {message, Modal} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from "wangeditor";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);

    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    // 因為樹選擇組件的屬性狀態，會隨當前編輯的節點而變化，所以單獨聲明一個響應式變量
    const treeSelectData = ref();
    treeSelectData.value = [];

    const columns = [
      {
        title: '名稱',
        dataIndex: 'name',
        slots: { customRender: 'name' }
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 一級文檔樹，children屬性就是二級文檔
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一級文檔樹，children屬性就是二級文檔
    level1.value = [];

    /**
     * 數據查詢
     **/
    const handleQuery = () => {
      loading.value = true;
      // 如果不清空現有數據，則編輯保存重新加載數據後，再點編輯，則列表顯示的還是編輯前的數據
      level1.value = [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("原始數組：", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("樹形結構：", level1);

          // 父文檔下拉框初始化，相當於點擊新增
          treeSelectData.value = Tool.copy(level1.value) || [];
          // 為選擇樹添加一個"無"
          treeSelectData.value.unshift({id: 0, name: '無'});
        } else {
          message.error(data.message);
        }
      });
    };

    // -------- 表單 ---------
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const editor = new E('#content');

    editor.config.zIndex = 0;
    // 顯示上傳圖片按鈕，轉成Base64存儲，同時也支持拖拽圖片
    // 上傳圖片文檔：https://doc.wangeditor.com/pages/07-%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87/01-%E9%85%8D%E7%BD%AE%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%8E%A5%E5%8F%A3.html
    // 上傳視頻文檔：https://doc.wangeditor.com/pages/07-%E4%B8%8A%E4%BC%A0%E8%A7%86%E9%A2%91/01-%E9%85%8D%E7%BD%AE%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%8E%A5%E5%8F%A3.html
    editor.config.uploadImgShowBase64 = true;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          // modalVisible.value = false;
          message.success("保存成功！");

          // 重新加載列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 將某節點及其子孫節點全部置為disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍歷數組，即遍歷某一層節點
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果當前節點就是目標節點
          console.log("disabled", node);
          // 將目標節點設置為disabled
          node.disabled = true;

          // 遍歷所有子節點，將所有子節點全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果當前節點不是目標節點，則到其子節點再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];
    /**
     * 查找整根樹枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍歷數組，即遍歷某一層節點
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果當前節點就是目標節點
          console.log("delete", node);
          // 將目標ID放入結果集ids
          // node.disabled = true;
          deleteIds.push(id);
          deleteNames.push(node.name);

          // 遍歷所有子節點
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果當前節點不是目標節點，則到其子節點再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 內容查詢
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content)
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 編輯
     */
    const edit = (record: any) => {
      // 清空富文本框
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleQueryContent();

      // 不能選擇當前節點及其所有子孫節點，作為父節點，會使樹斷開
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 為選擇樹添加一個"無"
      treeSelectData.value.unshift({id: 0, name: '無'});
    };

    /**
     * 新增
     */
    const add = () => {
      // 清空富文本框
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value) || [];

      // 為選擇樹添加一個"無"
      treeSelectData.value.unshift({id: 0, name: '無'});
    };

    const handleDelete = (id: number) => {
      // console.log(level1, level1.value, id)
      // 清空數組，否則多次刪除時，數組會一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '將刪除：【' + deleteNames.join("，") + "】刪除後不可恢復，確認刪除？",
        onOk() {
          // console.log(ids)
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加載列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        },
      });
    };

    // ----------------富文本預覽--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    onMounted(() => {
      handleQuery();
      console.log("test");
      console.log(document.getElementById("ssss"));
      editor.create();
    });

    return {
      param,
      // docs,
      level1,
      columns,
      loading,
      handleQuery,

      edit,
      add,

      doc,
      modalVisible,
      modalLoading,
      handleSave,

      handleDelete,

      treeSelectData,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
