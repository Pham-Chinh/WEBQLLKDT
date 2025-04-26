document.addEventListener("DOMContentLoaded", function () {
  const searchInput = document.getElementById("search-input");
  const suggestionBox = document.getElementById("suggestion-box");

  searchInput.addEventListener("input", function () {
    const keyword = searchInput.value.trim();
    if (keyword.length === 0) {
      suggestionBox.style.display = "none";
      suggestionBox.innerHTML = "";
      return;
    }

    fetch(`ajaxSearch.jsp?keyword=${encodeURIComponent(keyword)}`)
      .then((res) => res.text())
      .then((data) => {
        suggestionBox.innerHTML = data;
        suggestionBox.style.display = "block";
      });
  });

  // Ẩn khi click ra ngoài
  document.addEventListener("click", function (e) {
    if (!searchInput.contains(e.target) && !suggestionBox.contains(e.target)) {
      suggestionBox.style.display = "none";
    }
  });
});
