<script setup>
import { ref } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const response = ref(null);
const loading = ref(false);
const errorMessage = ref('');

const searchUser = async () => {
    if (!searchQuery.value) return;

    loading.value = true;
    errorMessage.value = '';
    response.value = null;

    try {
        const res = await axios.get(`http://localhost:8080/api/user/${searchQuery.value}`);
        response.value = res.data;
    } catch (error) {
        console.error('서버 요청 실패:', error);
        errorMessage.value = '검색 중 오류가 발생했습니다. 다시 시도해주세요.';
    } finally {
        loading.value = false;
    }
};
</script>

<template>
    <div class="sc-container">
        <div class="sc-panel">
            <h1 class="sc-title">StarCraft 1 Remastered<br>전적 검색</h1>
            <input
                v-model="searchQuery"
                class="sc-input"
                placeholder="ID 입력 후 엔터"
                @keyup.enter="searchUser"
            />
            <button class="sc-button" @click="searchUser" :disabled="loading">
                {{ loading ? '검색 중...' : '검색' }}
            </button>

            <div v-if="errorMessage" class="sc-error">{{ errorMessage }}</div>

            <div v-if="response" class="sc-result">
                <h2 class="sc-subtitle">Battle Tag: {{ response.battle_tag }}</h2>
                <div v-for="(toon, index) in response.toons" :key="index" class="sc-card">
                    <p><strong>Toon:</strong> {{ toon.toon }}</p>
                    <p><strong>Last Games:</strong> {{ toon.games_last_week }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.sc-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: #050a13;
}

.sc-panel {
    width: 400px;
    padding: 20px;
    background: linear-gradient(145deg, #1b2838, #111);
    border: 3px solid #0074cc;
    border-radius: 8px;
    box-shadow: 0 0 20px rgba(0, 116, 204, 0.5);
    text-align: center;
    color: #00ccff;
}

.sc-title {
    font-size: 22px;
    font-weight: bold;
    text-shadow: 0 0 8px rgba(0, 170, 255, 0.8);
}

.sc-input {
    width: 100%;
    padding: 10px;
    margin: 10px 0;
    background: #0c1624;
    border: 2px solid #0074cc;
    color: #00ccff;
    text-align: center;
    border-radius: 5px;
}

.sc-button {
    width: 100%;
    padding: 10px;
    background: #0044aa;
    border: 2px solid #00ccff;
    color: #fff;
    font-weight: bold;
    text-shadow: 0 0 6px rgba(255, 255, 255, 0.8);
    cursor: pointer;
    transition: 0.3s;
}

.sc-button:hover {
    background: #0074cc;
}

.sc-error {
    color: #ff4b4b;
    margin-top: 10px;
}

.sc-result {
    margin-top: 15px;
}

.sc-subtitle {
    font-size: 18px;
    font-weight: bold;
}

.sc-card {
    background: #0c1624;
    border: 2px solid #0074cc;
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
    text-align: left;
}
</style>
