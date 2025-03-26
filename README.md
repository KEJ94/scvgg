## 스타크래프트 리마스터 유저 정보 조회

### 환경
- Java17
- Spring boot
- Vue3
- Vuetify
- Docker

### 스타크래프트 클라이언트 웹 서버 포트 조회
Windows
```
(Get-NetTCPConnection -OwningProcess (Get-Process -Name StarCraft | Select-Object -ExpandProperty Id) | Where-Object {$_.State -eq "Listen"} | Sort-Object -Property LocalPort | Select-Object -First 1).LocalPort
```
MacOS
```
lsof -i -n -P | grep StarCraft
```

### Windows Analyzer Server
```js
const http = require('http');
const url = require('url');

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url);
    const pathName = parsedUrl.pathname;
    const pathParts = pathName.split('/').filter(part => part !== '');

    if (req.method === 'GET' && pathParts.length === 4 && pathParts[0] === 'api' && pathParts[1] === 'user') {
        const region = pathParts[2];
        const id = pathParts[3];

        console.log(`Region: ${region}, ID: ${id}`);

        // 요청할 외부 API URL
        const externalUrl = `http://127.0.0.1:53268/web-api/v2/aurora-profile-by-toon/${region}/${id}?request_flags=scr_profile`;

        // 외부 API 요청
        http.get(externalUrl, (apiRes) => {
            let data = '';

            // 응답 데이터 수집
            apiRes.on('data', (chunk) => {
                data += chunk;
            });

            // 응답 종료 후 클라이언트에 데이터 반환
            apiRes.on('end', () => {
                res.writeHead(200, { 'Content-Type': 'application/json' });
                res.end(data);
            });

        }).on('error', (err) => {
            console.error(`Error fetching data: ${err.message}`);
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Failed to fetch data');
        });

        return;
    }

    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Not Found');
});

const PORT = 3000;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
```

### Docker 실행
```
sudo docker-compose -f docker-compose-prod.yml build
sudo docker-compose -f docker-compose-prod.yml up
```

