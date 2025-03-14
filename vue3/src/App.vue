<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 반응형 변수로 response 선언
const response = ref('');

// 컴포넌트가 마운트될 때 서버 요청을 보내는 로직
onMounted(() => {
    axios.get('http://localhost:8080/api/greeting')  // 실제 서버 주소로 변경 필요
        .then(res => {
            response.value = res.data; // 응답 데이터를 화면에 표시
        })
        .catch(err => {
            console.error('서버 요청 실패:', err);
            response.value = '서버 요청 실패';
        });
});
</script>

<template>
    <div>
        <h1>{{ response }}</h1>  <!-- 서버 응답 표시 -->
        <v-app>
            <v-btn color="primary">Click</v-btn>
        </v-app>
    </div>
</template>

<style scoped>
/* 스타일은 필요에 따라 추가하세요 */
</style>