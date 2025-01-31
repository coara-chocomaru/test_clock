<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>デジタル時計</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #ff66b2, #ffccff);
            font-family: 'Poppins', sans-serif;
            overflow: hidden;
            opacity: 0;
            animation: fadeIn 2s forwards;
        }

        .container {
            position: relative;
            text-align: center;
        }

        /* 年号 */
        .year {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 18px;
            color: #fff;
            font-weight: 600;
        }

        /* 日付 */
        .date {
            font-size: 22px;
            color: #fff;
            font-weight: 600;
            margin-bottom: 10px;
            cursor: pointer;
            border-radius: 12px;
            display: inline-block;
            padding: 10px 20px;
            transition: transform 0.3s ease, background-color 0.3s ease;
        }

        .date:hover {
            background-color: rgba(255, 255, 255, 0.3);
        }

        /* 時計 */
        .clock {
            font-size: 80px;
            color: #fff;
            text-shadow: 3px 3px 10px rgba(0, 0, 0, 0.5);
            font-weight: 600;
            padding: 10px 20px;
            background-color: rgba(0, 0, 0, 0.3);
            border-radius: 15px;
        }

        /* 12/24切り替えボタン */
        .toggle-button {
            position: absolute;
            top: 10px;
            left: 10px;
            background-color: #ff66b2;
            color: white;
            padding: 10px 20px;
            border-radius: 30px;
            border: none;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
        }

        .toggle-button:hover {
            background-color: #ff3385;
        }

        @keyframes fadeIn {
            0% {
                opacity: 0;
            }
            100% {
                opacity: 1;
            }
        }

        @keyframes shake {
            0% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            50% { transform: translateX(5px); }
            75% { transform: translateX(-5px); }
            100% { transform: translateX(0); }
        }

    </style>
</head>
<body>
    <div class="container">
        <!-- 年号 -->
        <div class="year" id="year">2024</div>

        <!-- 日付 -->
        <div class="date" id="date" onclick="animateDate()"></div>

        <!-- 時計 -->
        <div class="clock" id="clock"></div>

        <!-- 12/24切り替えボタン -->
        <button class="toggle-button" onclick="toggleTimeFormat()">12/24</button>
    </div>

    <script>
        let is24Hour = false;
        const clockElement = document.getElementById('clock');
        const yearElement = document.getElementById('year');
        const dateElement = document.getElementById('date');

        function updateClock() {
            const now = new Date();
            const months = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
            const month = months[now.getMonth()];
            const day = now.getDate();
            const formattedDate = `${month} ${day}日`;

            // 日付を更新
            dateElement.textContent = formattedDate;

            let hours = now.getHours();
            let minutes = now.getMinutes();
            let seconds = now.getSeconds();
            let ampm = '';

            // 12時間制の場合の処理
            if (!is24Hour) {
                ampm = hours >= 12 ? 'PM' : 'AM';
                hours = hours % 12;
                hours = hours ? hours : 12;  // 0時を12に表示
            }

            hours = hours < 10 ? '0' + hours : hours;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            seconds = seconds < 10 ? '0' + seconds : seconds;

            clockElement.textContent = `${hours}:${minutes}:${seconds} ${ampm}`;
        }

        function toggleTimeFormat() {
            is24Hour = !is24Hour;
            updateClock();
        }

        function animateDate() {
            // 日付をタップした時のアニメーション
            dateElement.style.animation = 'shake 0.5s ease';
            setTimeout(() => {
                dateElement.style.animation = '';
            }, 500);
        }

        setInterval(updateClock, 1000);
        updateClock(); // 初回表示
    </script>
</body>
</html>
