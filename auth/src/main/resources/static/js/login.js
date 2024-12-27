let inputIcon = document.getElementById("icon-input");
let input = document.getElementById("senha");

inputIcon.addEventListener("click", () => {
    if (input.getAttribute("type") === "password") {
        inputIcon.innerText = "visibility_off";
        input.setAttribute("type", "text");
    } else {
        inputIcon.innerText = "visibility";
        input.setAttribute("type", "password");
    }
});
