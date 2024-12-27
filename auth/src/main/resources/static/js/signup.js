let inputIcon = document.getElementById("icon-input");
let input = document.getElementById("senha");
let termsSeller = document.getElementById("terms-seller");
let termsBuyer = document.getElementById("terms-buyer");
let createAccountGroup = document.getElementById("create-account-group");

inputIcon.addEventListener("click", () => {
    if (input.getAttribute("type") === "password") {
        inputIcon.innerText = "visibility_off";
        input.setAttribute("type", "text");
    } else {
        inputIcon.innerText = "visibility";
        input.setAttribute("type", "password");
    }
});

createAccountGroup.addEventListener("click", (event) => {
    if (event.target.classList.contains("btn-create-account")) {
        document.querySelectorAll(".btn-create-account").forEach((button) => button.classList.remove("active"));
        event.target.classList.add("active");

        if (event.target.innerText.includes("Vender")) {
            termsSeller.classList.remove("hidden");
            termsBuyer.classList.add("hidden");
        } else {
            termsBuyer.classList.remove("hidden");
            termsSeller.classList.add("hidden");
        }
    }
});