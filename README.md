# Minecraft Advanced Surveillance System (MASS)

## 项目简介 (Project Overview)
Minecraft Advanced Surveillance System (MASS) 是一个为 Minecraft 服务器设计的高级监控插件，支持 Spigot 1.19.4 和 Arclight 服务端。通过 MASS，玩家和管理员可以在游戏中放置摄像头点位，实时监控服务器内的动态。

Minecraft Advanced Surveillance System (MASS) is an advanced surveillance plugin designed for Minecraft servers, supporting Spigot 1.19.4 and Arclight. With MASS, players and administrators can place camera nodes in-game to monitor server activities in real-time.

---

## 功能特性 (Features)
- **摄像头点位管理 (Camera Node Management)**
  - 支持 PTZ、半球、枪机三种摄像头类型。
  - 摄像头点位无法被非授权玩家破坏。
  - 摄像头可实时标注视野内玩家的名称、生命值、饱食度等信息。

- **实时监控 (Real-Time Monitoring)**
  - 支持通过网页查看监控画面。
  - 摄像头支持 RTSP 协议，可接入现实录像机。
  - 离线监控：即使玩家不在线，也能通过网页查看监控画面。

- **性能优化 (Performance Optimization)**
  - 按需加载区块：只有在有监控请求时才加载摄像头所在区块。
  - 硬件加速：利用 NVIDIA GPU 的 NVENC 编码器优化视频流推送。
  - 异步处理：所有玩家属性读取和视锥体计算均在异步线程中完成。

---

## 核心模块 (Core Modules)
1. **MASS-Spigot-Plugin**
   - 负责游戏内逻辑，包括摄像头点位数据库管理、异步扫描任务和 API 接口。
2. **MASS-Render-Engine**
   - 使用 Minestom 或 LWJGL 进行离线渲染，仅在有监控请求时加载对应区块。
3. **MASS-Stream-Gateway**
   - 集成 FFmpeg 和 NVENC，支持 RTSP 推流和 WebRTC 网页分发。

---

## 数据库设计 (Database Design)
- **表名 (Table Name)**: `mass_cameras`
- **字段 (Fields)**:
  - `id` (UUID): 摄像头唯一标识。
  - `x`, `y`, `z` (REAL): 摄像头坐标。
  - `world_name` (TEXT): 世界名称。
  - `type` (TEXT): 摄像头类型 (PTZ, DOME, BULLET)。
  - `yaw`, `pitch` (REAL): 摄像头的偏航角和俯仰角。
  - `isActive` (INTEGER): 是否激活。
  - `whitelist` (TEXT): 白名单玩家列表。

---

## API 接口 (API Endpoints)
- **`/api/cameras`**
  - 返回所有激活的摄像头点位的 JSON 数据。
- **`/api/player/{name}`**
  - 返回指定玩家的实时属性，包括生命值、饱食度、装备、坐标等。

---

## 安装与使用 (Installation & Usage)
1. **安装插件 (Install the Plugin)**
   - 将插件 JAR 文件放入服务器的 `plugins` 文件夹。
2. **配置数据库 (Configure Database)**
   - 默认使用 SQLite，可根据需要切换到 MySQL。
3. **启动服务器 (Start the Server)**
   - 启动 Minecraft 服务器，插件会自动创建所需的数据库表。
4. **使用指令 (Use Commands)**
   - `/mass create <type>`: 创建摄像头点位。
   - `/mass manage <id> add/remove <player>`: 管理摄像头白名单。

---

## 性能优化建议 (Performance Optimization Tips)
- 确保服务器启用了硬件加速 (NVIDIA GPU)。
- 使用异步线程处理高频任务，避免阻塞主线程。
- 仅在必要时加载摄像头所在区块，减少资源占用。

---

## 贡献 (Contributing)
欢迎对 MASS 项目提出建议或提交代码贡献！

Contributions to the MASS project are welcome! Feel free to submit issues or pull requests.

---

## 许可证 (License)
本项目基于 MIT 许可证开源。

This project is open-sourced under the MIT License.
