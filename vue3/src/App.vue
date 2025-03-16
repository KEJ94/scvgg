<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const response = ref(null);

// 서버에서 받은 결과 필터링
const filteredGameResults = computed(() => {
    if (!response.value) return [];

    return response.value.game_results.map(game => ({
        ...game,
        players: game.players.filter(player => player.result !== 'undecided')
    }));
});

const searchUser = () => {
    if (!searchQuery.value) return; // 빈 값 방지

    axios.get(`http://localhost:8080/api/user/${searchQuery.value}`)
        .then(res => {
            response.value = res.data;
        })
        .catch(err => {
            console.error('서버 요청 실패:', err);
            response.value = '서버 요청 실패';
        });
};
</script>

<template>
    <v-container class="fill-height">
        <v-row justify="center">
            <v-col cols="12" sm="8" md="6">
                <v-card class="pa-6 elevation-4">
                    <v-card-title class="text-center text-h5">SCAN.GG</v-card-title>
                    <v-card-text>
                        <v-text-field
                            v-model="searchQuery"
                            label="검색어 입력"
                            outlined
                            clearable
                            @keyup.enter="searchUser"
                        ></v-text-field>
                        <v-btn
                            block
                            color="primary"
                            @click="searchUser"
                            class="mt-2"
                        >검색</v-btn>
                    </v-card-text>

                    <!-- Response 출력 -->
                    <v-card-actions v-if="response">
                        <v-alert type="info" class="mt-3">
                            <div v-if="response !== '서버 요청 실패'">
                                <v-row>
                                    <v-col cols="12" class="text-center">
                                        <v-subheader>Battle Tag: {{ response.battle_tag }}</v-subheader>
                                    </v-col>

                                    <!-- Toon 목록 출력 -->
                                    <v-col v-for="(toon, index) in response.toons" :key="index" cols="12" sm="6" class="mt-3">
                                        <v-card outlined>
                                            <v-card-title>{{ toon.toon }}</v-card-title>
                                            <v-card-subtitle>Last Games: {{ toon.games_last_week }}</v-card-subtitle>
                                        </v-card>
                                    </v-col>

                                    <!-- 필터링된 게임 결과 출력 -->
                                    <v-col v-for="(game, index) in filteredGameResults" :key="index" cols="12">
                                        <v-card outlined class="mt-3">
                                            <v-card-title>Game {{ index + 1 }} Results</v-card-title>
                                            <v-list>
                                                <v-list-item-group>
                                                    <v-list-item v-for="(player, idx) in game.players" :key="idx">
                                                        <v-list-item-content>
                                                            <v-list-item-title>{{ player.toon }}</v-list-item-title>
                                                            <v-list-item-subtitle>Result: {{ player.result }}</v-list-item-subtitle>
                                                        </v-list-item-content>
                                                    </v-list-item>
                                                </v-list-item-group>
                                            </v-list>
                                        </v-card>
                                    </v-col>
                                </v-row>
                            </div>
                            <div v-else>
                                {{ response }}
                            </div>
                        </v-alert>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<style scoped>
.fill-height {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>
