<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <meta charset="UTF-8">
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            display: flex;
            height: 100vh;
        }

        .sidebar {
            width: 250px;
            background-color: #333;
            color: white;
            height: 100vh;
            flex-shrink: 0;
            transition: width 0.3s;
            overflow: hidden;
        }

        .sidebar.collapsed {
            width: 60px;
        }

        .sidebar h2 {
            text-align: center;
            padding: 20px 0;
        }

        .sidebar a {
            display: flex;
            align-items: center;
            padding: 14px 20px;
            color: white;
            text-decoration: none;
        }

        .sidebar a:hover {
            background-color: #444;
        }

        .sidebar a i {
            margin-right: 10px;
            min-width: 20px;
        }

        .sidebar.collapsed a span {
            display: none;
        }

        .toggle-btn {
            width: 100%;
            padding: 12px;
            background-color: #444;
            border: none;
            color: white;
            cursor: pointer;
            font-size: 18px;
        }

        .main {
            flex-grow: 1;
            padding: 30px;
            overflow-y: auto;
        }

        .section {
            display: none;
        }

        .section.active {
            display: block;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
        }

        th {
            background-color: #eee;
        }
    </style>
</head>
<body>

<!-- SIDEBAR -->
<div class="sidebar" id="sidebar">
    <button class="toggle-btn" onclick="toggleSidebar()">☰</button>
    <h2>Admin</h2>
    <a href="#" onclick="showSection('dashboard')"><i class='bx bxs-dashboard'></i><span>Dashboard</span></a>
    <a href="#" onclick="loadSection('manager-account', 'accounts')"><i class='bx bxs-user'></i><span>QL Tài khoản</span></a>
    <a href="#" onclick="loadSection('manager-product', 'products')"><i class='bx bxs-box'></i><span>QL Sản phẩm</span></a>
    <a href="#" onclick="loadSection('manager-order.jsp', 'orders')"><i class='bx bxs-cart'></i><span>QL Đơn hàng</span></a>
</div>

<!-- MAIN CONTENT -->
<div class="main">
    <!-- Dashboard content -->
    <div id="dashboard" class="section active">
        <h2>Xin chào, Quản trị viên!</h2>
        <p>Chào mừng bạn đến với hệ thống quản lý linh kiện điện tử.</p>

        <!-- ✅ Biểu đồ thống kê sản phẩm -->
        <h3>Thống kê số lượng sản phẩm theo danh mục</h3>
        <canvas id="productChart" width="600" height="300"></canvas>
    </div>

    <!-- Các phần load động -->
    <div id="accounts" class="section"></div>
    <div id="products" class="section"></div>
    <div id="orders" class="section"></div>
</div>

<!-- SCRIPT -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    function toggleSidebar() {
        const sidebar = document.getElementById('sidebar');
        sidebar.classList.toggle('collapsed');
    }

    function showSection(id) {
        const sections = document.querySelectorAll('.section');
        sections.forEach(section => section.classList.remove('active'));
        document.getElementById(id).classList.add('active');
    }

    function loadSection(url, id) {
        const sections = document.querySelectorAll('.section');
        sections.forEach(section => section.classList.remove('active'));

        fetch(url)
            .then(response => response.text())
            .then(html => {
                const container = document.getElementById(id);
                container.innerHTML = html;
                container.classList.add('active');

                // Reload scripts
                const scripts = container.querySelectorAll("script");
                scripts.forEach(oldScript => {
                    const newScript = document.createElement("script");
                    if (oldScript.src) {
                        newScript.src = oldScript.src;
                    } else {
                        newScript.textContent = oldScript.textContent;
                    }
                    document.body.appendChild(newScript);
                });
            });
    }

    // ✅ Load biểu đồ thống kê sản phẩm
    document.addEventListener("DOMContentLoaded", function () {
        fetch("product-stats")
            .then(response => response.json())
            .then(data => {
                const labels = Object.keys(data).map(label => {
                    if (label === "moive") return "Mới về";
                    if (label === "noibat") return "Nổi bật";
                    if (label === "phobien") return "Phổ biến";
                    return label;
                });
                const counts = Object.values(data);

                // ✅ Màu riêng cho từng loại
                const backgroundColors = Object.keys(data).map(label => {
                    if (label === "moive") return "rgba(255, 99, 132, 0.6)";    // đỏ
                    if (label === "noibat") return "rgba(54, 162, 235, 0.6)";    // xanh dương
                    if (label === "phobien") return "rgba(255, 206, 86, 0.6)";   // vàng
                    return "rgba(153, 102, 255, 0.6)";                           // tím
                });

                const borderColors = backgroundColors.map(color =>
                    color.replace('0.6', '1') // đổi độ trong suốt cho border
                );

                new Chart(document.getElementById("productChart"), {
                    type: "bar",
                    data: {
                        labels: labels,
                        datasets: [{
                            label: 'Số lượng sản phẩm',
                            data: counts,
                            backgroundColor: backgroundColors,
                            borderColor: borderColors,
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: { beginAtZero: true }
                        }
                    }
                });
            });
    });
</script>


</body>
</html>
