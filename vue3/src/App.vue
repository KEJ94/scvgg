<script setup>
import {computed, ref} from 'vue';
import axios from 'axios';

const searchQuery = ref('');
const response = ref(null);
const loading = ref(false);
const errorMessage = ref('');
const selectedRegion = ref('30'); // 기본값은 'KR'
let profileImageUrl = ref('');
const showModal = ref(false); // 모달 상태 관리

const searchUser = async () => {
    if (!searchQuery.value) return;

    loading.value = true;
    errorMessage.value = '';
    response.value = null;

    try {
        const defaultAvatarUrl = 'https://scrassets.classic.blizzard.com/avatar-icons/S1/d2edd0800865e3a5e0c0c86898010f90.png';

        const res = await axios.get(`http://localhost:8080/api/user/${selectedRegion.value}/${searchQuery.value}`);
        response.value = res.data;

        // toon 이름 일치하는 프로필 찾기
        const profileImage = response.value.profiles.find(
            profile => profile.toon.toLowerCase() === searchQuery.value.toLowerCase()
        )?.avatar_id;

        // 해당 avatar_id로 URL 찾기
        const matched = response.value.avatars_framed[profileImage];

        // 결과 없으면 기본 이미지로 대체
        profileImageUrl.value = matched?.url || defaultAvatarUrl;

        showModal.value = true;

    } catch (error) {
        console.error('서버 요청 실패:', error);
        errorMessage.value = '검색 결과가 없습니다.';
    } finally {
        loading.value = false;
    }
};

// 'undecided' 상태 필터링
const filteredGameResults = computed(() => {
    if (!response.value) return [];

    return response.value.game_results.map(game => ({
        ...game,
        players: game.players.filter(player => player.result !== 'undecided')
    }));
});

const getPlayerClass = (result) => {
    if (result === 'win') {
        return 'win';  // 승리
    } else if (result === 'loss') {
        return 'loss';  // 패배
    }
    return '';  // 기본값은 빈 문자열
};

const getGameResult = (result) => {
    if (result === 'win') {
        return '승리';  // 승리
    } else if (result === 'loss') {
        return '패배';  // 패배
    }
    return '';  // 기본값은 빈 문자열
};

const getGateway = (gatewayId) => {
    if (gatewayId === 10) {
        return 'U.S West';
    } else if (gatewayId === 11) {
        return 'U.S East';
    } else if (gatewayId === 20) {
        return 'Europe';
    } else if (gatewayId === 30) {
        return 'Korea';
    } else if (gatewayId === 45) {
        return 'Asia';
    }
    return '';  // 기본값은 빈 문자열
};

const getRace = (race) => {
    if(race === "terran") return "테란"
    else if(race === "protoss") return "프로토스"
    else if(race === "zerg") return "저그"
    else return "랜덤"
}

</script>

<template>
    <div class="sc-container">
        <div class="sc-panel">
            <h1 class="sc-title">스타크래프트 1 리마스터<br>전적 검색</h1>
            <div class="search-container">
                <input
                    v-model="searchQuery"
                    class="sc-input"
                    placeholder="아이디 입력 후 엔터"
                    @keyup.enter="searchUser"
                />
                <select v-model="selectedRegion" class="sc-select">
                    <option value="10">U.S West</option>
                    <option value="11">U.S East</option>
                    <option value="20">Europe</option>
                    <option value="30">Korea</option>
                    <option value="45">Asia</option>
                </select>
            </div>
            <button class="sc-button" @click="searchUser" :disabled="loading">
                {{ loading ? '검색 중...' : '검색' }}
            </button>

            <div v-if="errorMessage" class="sc-error">{{ errorMessage }}</div>

            <!-- 모달 열기 -->
            <div v-if="showModal" class="modal-overlay" @click="showModal = false">
                <div class="modal-layout" @click.stop>
                    <div class="modal-content">
                        <img
                            :src="profileImageUrl"
                            class="avatar-image"
                            style="width:100px;"
                        />
                        <h3 class="sc-subtitle">배틀태그 : <span style="color:white">{{ response.battle_tag }}</span></h3>
                        <br/>
                        <div class="sc-card-container">
                            <div v-for="(toon, index) in response.toons" :key="index" class="sc-card">
                                <p><strong>아이디 : </strong> <span style="color:white">{{ toon.toon }}</span></p>
                                <p><strong>게이트웨이 : </strong> {{ getGateway(toon.gateway_id) }}</p>
                                <p><strong>마지막 게임 : </strong> {{ toon.games_last_week }}주 전</p>
                            </div>
                        </div>
                        <br/>
                        <div v-if="response.game_results.length > 0">
                            <div v-for="(game, index) in filteredGameResults" :key="index" class="sc-card">
                                <p><strong>최근 게임 {{ index + 1 }}</strong></p>
                                <ul>
                                    <li v-for="(player, pIndex) in game.players" :key="pIndex">
                                        <span :class="getPlayerClass(player.result)">
                                            {{ getRace(player.attributes.race) }} {{ player.toon }} {{getGameResult(player.result) }}
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <!-- 닫기 버튼을 모달 내 하단에 고정 -->
                    <div class="modal-footer">
                        <button class="sc-button" @click="showModal = false">닫기</button>
                    </div>
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
    padding-bottom: 10px;
}

.search-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.sc-input {
    width: 74%;
    padding: 10px;
    background: #0c1624;
    border: 2px solid #0074cc;
    color: #00ccff;
    text-align: center;
    border-radius: 5px;
}

.sc-select {
    width: 25%;
    padding: 10px;
    background: #0c1624;
    border: 2px solid #0074cc;
    color: #00ccff;
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

.sc-card-container {
    display: flex;
    flex-wrap: wrap;
    gap: 19px; /* 카드 사이의 간격 */
}

.sc-card-container .sc-card {
    width: calc(50% - 10px); /* 한 줄에 두 개 배치 */
    box-sizing: border-box;
}


.sc-card {
    background: #0c1624;
    border: 2px solid #0074cc;
    padding: 10px;
    border-radius: 5px;
    text-align: left;
}

.sc-card ul {
    list-style-type: none;
    padding-left: 20px;
}

.sc-card li {
    margin: 5px 0;
}

.sc-card span.win {
    color: #66ff66; /* Win - Green */
}

.sc-card span.loss {
    color: #ff4b4b; /* Loss - Red */
}

.sc-card span.undecided {
    color: #aaaaaa; /* Undecided - Gray */
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    background: #1b2838;
    padding: 10px;
    width: 100%;
    height: 101%;
    max-width: 600px;
    color: #00ccff;
    text-align: center;
    max-height: 80vh; /* 화면 높이를 넘어가지 않도록 설정 */
    overflow-y: auto; /* 스크롤을 추가 */
    position: relative; /* 자식 요소인 footer를 고정하기 위해 position 지정 */
}

.modal-layout {
    height:100%;
    background: #1b2838;
    padding: 20px;
    border-radius: 8px;
    width: 100%;
    max-width: 600px;
    box-shadow: 0 0 20px rgba(0, 116, 204, 0.8);
    color: #00ccff;
    text-align: center;
    max-height: 80vh; /* 화면 높이를 넘어가지 않도록 설정 */
    position: relative; /* 자식 요소인 footer를 고정하기 위해 position 지정 */
}

.modal-footer {
    position: absolute; /* 절대 위치로 설정 */
    bottom: -60px; /* 모달 하단에서 20px */
    left: 0;
    width: 100%;
    text-align: center;
}

.modal-footer button {
    width: 100%; /* 버튼 너비 100%로 설정 */
    padding: 10px 0; /* 세로 여백 조정 */
    background: #0044aa;
    border: 2px solid #00ccff;
    color: #fff;
    font-weight: bold;
    cursor: pointer;
    transition: 0.3s;
}

</style>
