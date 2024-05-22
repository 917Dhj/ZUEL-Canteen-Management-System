<template>
	<el-tabs v-model="message" type="card">
		<el-tab-pane :label="`消息列表(${state.unread.length})`" name="first">
			<el-table :data="state.unread" :show-header="false" style="width: 100%">
				<el-table-column>
					<template #default="scope">
						<span class="message-title">{{ scope.row.title }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="date" width="180"></el-table-column>
			</el-table>
		</el-tab-pane>
	</el-tabs>
</template>

<script setup lang="ts" name="tabs">
import { ref, reactive } from 'vue';
import {getMessageToRestaurant} from "@/api";

const message = ref('first');
const state = reactive({
	unread: [

	],
});

interface userInfo {
  restaurantId: string;
}
const restaurantId = localStorage.getItem('vuems_name');

const getData = async () => {
  const param: userInfo = {
    restaurantId: restaurantId
  };
  const res = await getMessageToRestaurant(param);
  console.log(res);
  state.unread = res.data;
}
getData();
</script>

<style>
.message-title {
	cursor: pointer;
	color: var(--el-color-primary);
}
</style>
