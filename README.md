# 📝 Task Management App (Taskify)

> **Mô tả:** Ứng dụng quản lý công việc cá nhân, phát triển từ To-Do List đơn giản đến hệ thống Kanban Board hoàn chỉnh (Fullstack).
>
> **Mục tiêu học tập:** Nắm vững ReactJS, State Management, Backend (NodeJS/Express), Database và DevOps.

---

## 🚀 Lộ Trình Phát Triển (Development Roadmap)

Dự án được chia thành 5 cấp độ (Levels) để nâng cấp dần kỹ năng và tính năng.

### ✅ Level 1: React Fundamentals & CRUD (Đã hoàn thành)

**Mục tiêu:** Xây dựng nền tảng Frontend vững chắc, hiểu về Component và State.

- **Tính năng:**
  - [x] Thêm công việc mới (Add Task).
  - [x] Hiển thị danh sách công việc.
  - [x] Sửa tiêu đề công việc (Edit).
  - [x] Xoá công việc (Delete).
  - [x] Đánh dấu hoàn thành/chưa hoàn thành (Toggle Status).
  - [x] Bộ lọc: Tất cả / Hoàn thành / Đang làm.
  - [x] Lưu dữ liệu vào `localStorage` (không mất khi F5).
- **Kỹ thuật sử dụng:**
  - React (Vite).
  - Hooks: `useState`, `useEffect`.
  - Props drilling & Form handling.
  - SCSS Modules & Global Variables (CSS Variables).

---

### 🚧 Level 2: UI/UX & Kanban Board (Đang thực hiện)

**Mục tiêu:** Nâng cấp giao diện hiện đại và thay đổi cách tương tác người dùng.

- **Tính năng dự kiến:**
  - [x] **Drag & Drop (Kéo thả):** Kéo task giữa các cột (Todo ➝ Doing ➝ Done).
  - [x] **Giao diện Kanban:** Chia màn hình thành 3 cột rõ ràng.
  - [x] **UI Makeover:** Sử dụng Global Styles, biến màu sắc chuẩn (SCSS), thêm Animation nhẹ.
  - [x] **Modal:** Form sửa task hiện dưới dạng Popup thay vì inline.
- **Kỹ thuật mới:**
  - Thư viện: `dnd-kit` hoặc `react-beautiful-dnd`.
  - CSS Flexbox/Grid nâng cao.

---

### 📅 Level 3: Backend Integration (Spring Boot Start)

**Mục tiêu:** Xây dựng RESTful API chuẩn mực với Java Spring Boot và kết nối với Frontend.

- **Tính năng dự kiến:**
  - [x] Thiết kế Database Schema (Entity Relationship).
  - [x] Tạo API lấy danh sách Task (GET /api/tasks).
  - [x] API Thêm/Sửa/Xoá Task.
  - [ ] Xử lý Exception (Lỗi) chuẩn trong Spring Boot (@ControllerAdvice).
  - [ ] Cấu hình CORS (để React gọi được API từ port khác).
- **Kỹ thuật mới (Java ecosystem):**
  - **Core:** Java 17+, Spring Boot 3.x.
  - **Database:** MySQL hoặc PostgreSQL.
  - **ORM:** Spring Data JPA (Hibernate).
  - **Tool:** Postman (Test API), Maven/Gradle.

---

### 📅 Level 4: Advanced Security & Business Logic

**Mục tiêu:** Bảo mật ứng dụng chặt chẽ và xử lý logic phức tạp.

- **Tính năng dự kiến:**
  - [x] **Spring Security:** Cấu hình bảo mật cho hệ thống.
  - [x] **JWT (JSON Web Token):** Xác thực người dùng (Login/Register).
  - [x] **Validation:** Kiểm tra dữ liệu đầu vào chặt chẽ (@Valid, @NotNull...).
  - [x] **Pagination:** Phân trang danh sách task (Pageable).
- **Kỹ thuật mới:**
  - Spring Security + JWT Filter.
  - Bean Validation.
  - Mapper (MapStruct) để chuyển đổi Entity <-> DTO.

---

### 📅 Level 5: Professional DevOps & Testing

**Mục tiêu:** Đóng gói và triển khai ứng dụng Fullstack.

- **Tính năng dự kiến:**
  - [ ] **Unit Test:** JUnit 5 + Mockito (Test Service layer).
  - [ ] **Integration Test:** Test Controller và Database.
  - [ ] **Dockerize:**
    - Container 1: React App (Nginx).
    - Container 2: Spring Boot App.
    - Container 3: Database.
  - [ ] **CI/CD:** Jenkins hoặc GitHub Actions.

---

## 🏗 Kiến Trúc Hệ Thống (Architecture)

Mô hình 3 lớp (3-Layer Architecture) điển hình sẽ áp dụng:

1.  **Frontend (React):** Gửi HTTP Request.
2.  **Controller Layer (Spring Web):** Nhận request, validate dữ liệu.
3.  **Service Layer (Business Logic):** Xử lý logic nghiệp vụ chính.
4.  **Repository Layer (Spring Data JPA):** Giao tiếp trực tiếp với Database.
