const express = require('express');
const WebSocket = require('ws');
const cors = require('cors');
const axios = require('axios');

const app = express();
app.use(cors()); // CORS 문제 해결
app.use(express.json()); // JSON 데이터 파싱

const server = app.listen(3000, () => {
    console.log("✅ Node.js WebSocket 서버 실행 중 (포트: 3000)");
});

const wss = new WebSocket.Server({ server });

let rooms = {}; // 채팅방별 클라이언트 저장
let userCounts = {}; // 각 방마다 접속한 사용자 수 관리

// 🟢 채팅방 생성 API
app.post('/chat/room', async (req, res) => {
    const { name } = req.body;
    if (!name) {
        return res.status(400).json({ error: "채팅방 이름을 입력하세요." });
    }

    try {
        console.log(`🔵 채팅방 생성 요청: ${name}`);
        
        // Spring Boot 서버에 채팅방 생성 요청 전송
        const response = await axios.post('http://localhost:8080/chat/room', { name });

        console.log("🟢 채팅방 생성 성공:", response.data);
        res.json(response.data); // 생성된 채팅방 정보 반환
    } catch (error) {
        console.error("🔴 채팅방 생성 실패!", error.response?.data || error.message);
        res.status(500).json({ error: "채팅방 생성 중 오류 발생" });
    }
});

// 🟢 WebSocket 핸들링
wss.on('connection', (ws) => {
    console.log("🔵 WebSocket 연결됨!");

    ws.on('message', async (message) => {
        let data;
        try {
            data = JSON.parse(message);
        } catch (error) {
            console.error("🔴 JSON 파싱 오류:", error);
            return;
        }

        console.log("📩 메시지 수신:", data);

        if (data.type === "join") {
            const roomId = data.roomId;
            ws.roomId = roomId;

            if (!rooms[roomId]) {
                rooms[roomId] = [];
                userCounts[roomId] = 0; // 방이 새로 생성될 때 접속자 수 초기화
            }

            userCounts[roomId] += 1;
            ws.nickname = userCounts[roomId]; // 사용자의 고유번호를 nickname으로 설정
            rooms[roomId].push(ws);

            console.log(`👤 사용자 ${ws.nickname} (${roomId} 방에 접속)`);

            // ✅ Spring Boot에서 기존 채팅 내역 가져오기
            try {
                const response = await axios.get(`http://localhost:8080/chat/messages/${roomId}`);
                console.log("📜 [Node.js] 과거 채팅 내역 응답:", response.data);
                
                ws.send(JSON.stringify({
                    type: "history",
                    messages: response.data // ✅ DB에서 가져온 채팅 메시지 리스트
                }));
            } catch (error) {
                console.error("🔴 채팅 내역 불러오기 실패:", error.response?.data || error.message);
            }
            
            // ✅ 클라이언트에게 nickname 전달
            ws.send(JSON.stringify({
                type: "nickname",
                nickname: ws.nickname
            }));

        } else if (data.type === "chat") {
            const roomId = ws.roomId;
            if (!roomId) {
                console.warn("⚠️ 채팅 메시지를 보낼 방 ID가 없음!");
                return;
            }

            console.log(`💬 [${roomId} 방] ${ws.nickname}: ${data.message}`);

            // ✅ 같은 방의 모든 사용자에게 메시지 전송
            if (rooms[roomId]) {
                rooms[roomId].forEach(client => {
                    if (client.readyState === WebSocket.OPEN) {
                        console.log(`📡 메시지 브로드캐스트 → ${data.message}`);
                        client.send(JSON.stringify({
                            type: "chat",
                            sender: ws.nickname,
                            message: data.message,
                            time: data.time
                        }));
                    }
                });
            }

            // ✅ Spring Boot 서버에 메시지 저장 요청
            try {
                await axios.post('http://localhost:8080/chat/message', {
                    chatRoomId: roomId,
                    sender: ws.nickname,
                    message: data.message
                });
                console.log("✅ 메시지 저장 성공");
            } catch (error) {
                console.error("🔴 메시지 저장 실패:", error.response?.data || error.message);
            }
        }
    });

    ws.on('close', () => {
        console.log(`❌ 클라이언트 연결 해제됨!`);
        const roomId = ws.roomId;

        if (roomId && rooms[roomId]) {
            rooms[roomId] = rooms[roomId].filter(client => client !== ws);
            console.log(`👤 사용자 ${ws.nickname} (${roomId} 방) 퇴장`);

            // ✅ 모든 사용자가 방을 떠났다면 userCounts 초기화
            if (rooms[roomId].length === 0) {
                delete userCounts[roomId];
                delete rooms[roomId];
                console.log(`🚪 방 ${roomId} 제거됨`);
            }
        }
    });
});
