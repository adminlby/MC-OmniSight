# 👁️ OmniSight
### Minecraft 高级全景监控系统 | Advanced Surveillance System for Minecraft

OmniSight 是一款专为 Minecraft 服务器设计的高性能、工业级监控插件。它打破了传统游戏内监控的限制，支持 **物理渲染推流** 与 **RTSP 协议驱动**，允许玩家通过游戏内点位或外部网页实时掌握服务器动态。

---

## ✨ 功能特性 (Key Features)

### 📸 智能摄像头点位管理
* **多样化形态**：支持 PTZ（云台）、DOME（半球）、BULLET（枪机）三种专业级监控模型。
* **增强现实感 (AR)**：自动捕捉并实时标注视野内玩家的 **生命值、饱食度、装备及名称**。
* **权限保护**：非授权玩家无法破坏或访问摄像头，保障监控网路安全。

### 🌐 跨平台实时监控
* **多端联动**：支持通过 Web 浏览器或接入现实中的专业录像机 (NVR/VMS)。
* **RTSP 协议支持**：标准流媒体协议分发，实现真正的“场外监控”。
* **全时段覆盖**：即使操作员不在游戏内，也能通过网页端实时调阅画面。

### 🚀 极致性能表现
* **动态切片加载**：仅在监控请求激活时加载对应区块，大幅降低资源损耗。
* **NVENC 硬件加速**：深度集成 NVIDIA GPU 硬件编码，实现高清、低延迟的视频推送。
* **多线程分流**：视锥体（Frustum）计算与玩家属性抓取完全由异步线程处理，零阻塞主线程。

---

## 🛠️ 系统架构 (Core Architecture)

OmniSight 由三个核心组件协同工作：

| 模块名称       | 核心职能                                   | 技术栈         |
|----------------|----------------------------------------|--------------|
| CameraNode     | 表示摄像头对象，管理摄像头的属性和状态         | Java         |
| DatabaseManager | 管理与数据库（SQLite/MySQL）的交互，确保数据持久化 | Java, HikariCP |
| MASSApiServer  | 提供内置 Web 服务器，供外部程序获取实时数据      | Java, com.sun.net.httpserver |

---

## 💾 数据库设计 (Database Schema)  
默认支持 **SQLite**（轻量级）与 **MySQL**（高并发）切换。  

| 字段名         | 类型         | 描述                           |  
| :------------- | :----------- | :----------------------------- |  
| `id`           | UUID         | 摄像头唯一标识符               |  
| `x`, `y`, `z`  | REAL         | 摄像头在世界中的精准坐标       |  
| `world_name`   | TEXT         | 所在维度/世界名称              |  
| `type`         | ENUM         | 监控类型（PTZ, DOME, BULLET） |  
| `yaw`, `pitch` | REAL         | 摄像头的偏航角与俯仰角         |  
| `whitelist`    | JSON/TEXT    | 授权访问的玩家 UUID 或名称列表 |  

---

## 📡 API 接口说明 (Endpoints)  

| 路径              | 方法   | 描述                                         |  
| :---------------- | :----- | :------------------------------------------- |  
| `/api/cameras`    | `GET`  | 获取所有在线/激活状态的摄像头详细信息（JSON 格式） |  
| `/api/player/{name}` | `GET` | 实时获取指定玩家的坐标、生命值、饱食度及装备数据 |  

--- 

## 🚀 快速开始 (Quick Start)

### 1. 安装与部署
1. 下载最新版 `OmniSight.jar` 放入服务器 `plugins` 文件夹。
2. 确保环境已安装 **FFmpeg** 并具备 **NVIDIA 驱动**（若需硬件加速）。
3. 启动服务器，系统将自动生成 `config.yml` 与本地数据库。

### 2. 常用指令  
* `/omni create <type>` - 在当前位置创建一个摄像头，`<type>` 支持类型包括：PTZ, DOME, BULLET。  
* `/omni manage <id> add <player>` - 为指定摄像头（`<id>`）添加玩家（`<player>`）到白名单。  
* `/omni manage <id> remove <player>` - 从指定摄像头（`<id>`）的白名单中移除玩家（`<player>`）。  
* `/omni list` - 列出所有已部署摄像头的状态及详细信息。

--- 

## ⚖️ 开源协议 (License)
本项目基于 **MIT License** 开源。

---

**想了解更多或参与开发？**
欢迎提交 Issue 或 Pull Request 来完善 OmniSight！
