<template>
  <a-layout-footer style="text-align: center">
    文件管理系統<span v-show="user.id">，歡迎：{{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
  import { defineComponent, computed, onMounted } from 'vue';
  import store from "@/store";
  import {Tool} from "@/util/tool";
  import { notification } from 'ant-design-vue';

  export default defineComponent({
    name: 'the-footer',
    setup() {
      const user = computed(() => store.state.user);

      let websocket: any;
      let token: any;
      const onOpen = () => {
        console.log('websocket success code：', websocket.readyState)
      };
      const onMessage = (event: any) => {
        console.log('WebSocket request：', event.data);
        notification['info']({
          message: 'receive message',
          description: event.data,
        });
      };
      const onError = () => {
        console.log('WebSocket error：', websocket.readyState)
      };
      const onClose = () => {
        console.log('WebSocket close：', websocket.readyState)
      };
      const initWebSocket = () => {

        websocket.onopen = onOpen;

        websocket.onmessage = onMessage;

        websocket.onerror = onError;

        websocket.onclose = onClose;
      };

      onMounted(() => {
        // WebSocket
        if ('WebSocket' in window) {
          token = Tool.uuid(10);

          websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
          initWebSocket()

          //
          // websocket.close();
        } else {
          alert('not support')
        }
      });

      return {
        user
      }
    }
  });
</script>
