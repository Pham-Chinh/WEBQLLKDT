function openAddModal() {
    document.getElementById("addProduct").style.display = "block";
}

function openEditModal(id, name, desc, price, img, label) {
    document.getElementById("edit-id").value = id;
    document.getElementById("edit-name").value = name;
    document.getElementById("edit-description").value = desc;
    document.getElementById("edit-price").value = price;
    document.getElementById("current-image").src = img;
    document.getElementById("edit-label").value = label;
    document.getElementById("editProduct").style.display = "block";
}

function closeModal(id) {
    document.getElementById(id).style.display = "none";
}

function previewAddImage(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
            const img = document.getElementById("previewAdd");
            img.src = e.target.result;
            img.style.display = "block";
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function submitForm(formId) {
    const form = document.getElementById(formId);
    const formData = new FormData(form);

    fetch('manager-product', {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(html => {
        document.getElementById('products').innerHTML = html;
        closeModal('addProduct');
        closeModal('editProduct');
    });
}

function deleteProduct(id) {
    const formData = new FormData();
    formData.append("action", "delete");
    formData.append("id", id);

    fetch("manager-product", {
        method: "POST",
        body: formData
    })
    .then(response => response.text())
    .then(html => {
        document.getElementById('products').innerHTML = html;
    });
}

function submitSearch(form) {
    const formData = new FormData(form);
    fetch("manager-product?" + new URLSearchParams(formData), {
        method: "GET"
    })
    .then(res => res.text())
    .then(html => {
        document.getElementById('products').innerHTML = html;
    });
}
