# SpringBoot-project

#### NonUserController: Không yêu cầu xác thực bằng header hoặc cookies
#### -login: Sau khi Post Request server sẽ tạo một cookies lưu jwt của người dùng tự động trong client để dùng cho các lần request tiếp theo mà không cần đính kèm. 
#### UserController: Xác thực bằng header hoặc cookies
#### -profile: dự vào header hoặc cookies lấy toàn bộ thông tin của user tương ứng
#### -logout: xoá toàn bộ cookies của client
 
<a href="http://localhost:8080/swagger-ui/index.html">http://localhost:8080/swagger-ui/index.html</a>
