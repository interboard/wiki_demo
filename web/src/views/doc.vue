<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">對不起，找不到相關文件！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{doc.name}}</h2>
            <div>
              <span>閱讀數：{{doc.viewCount}}</span> &nbsp; &nbsp;
              <span>點贊數：{{doc.voteCount}}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <div class="wangeditor" :innerHTML="html"></div>
          <div class="vote-div">
            <a-button type="primary" shape="round" :size="'large'" @click="vote">
              <template #icon><LikeOutlined /> &nbsp;點贊：{{doc.voteCount}} </template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, createVNode } from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    const route = useRoute();
    const docs = ref();
    const html = ref();
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];
    // 當前選中的文檔
    const doc = ref();
    doc.value = {};

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
     * 內容查詢
     **/
    const handleQueryContent = (id: number) => {
      axios.get("/doc/find-content/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          html.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 數據查詢
     **/
    const handleQuery = () => {
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        const data = response.data;
        if (data.success) {
          docs.value = data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);

          if (Tool.isNotEmpty(level1)) {
            defaultSelectedKeys.value = [level1.value[0].id];
            handleQueryContent(level1.value[0].id);
            // 初始顯示文檔信息
            doc.value = level1.value[0];
          }
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)) {
        // 選中某一節點時，加載該節點的文檔信息
        doc.value = info.selectedNodes[0].props;
        // 加載內容
        handleQueryContent(selectedKeys[0]);
      }
    };

    // 點贊
    const vote = () => {
      axios.get('/doc/vote/' + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          doc.value.voteCount++;
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      level1,
      html,
      onSelect,
      defaultSelectedKeys,
      doc,
      vote
    }
  }
});
</script>

<style>
/* wangeditor默認樣式, 參照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 樣式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 樣式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 樣式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 樣式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p沖突，覆蓋掉 */
.wangeditor blockquote p {
  font-family:"YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight:600;
}

/* 點贊 */
.vote-div {
  padding: 15px;
  text-align: center;
}


.wangeditor img {
  max-width: 100%;
  height: auto;
}


.wangeditor iframe {
  width: 100%;
  height: 400px;
}
</style>
