import '@mdi/font/css/materialdesignicons.css';  // mdi 아이콘 스타일
import 'vuetify/styles';  // Vuetify 스타일

import { createVuetify } from 'vuetify';  // Vuetify 객체 생성
import * as components from 'vuetify/components';  // Vuetify 컴포넌트 가져오기
import * as directives from 'vuetify/directives';  // Vuetify 지시자 가져오기

export default createVuetify({
    components,
    directives,
});
