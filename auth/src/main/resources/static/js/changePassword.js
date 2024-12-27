let inputIconSenha = document.getElementById("icon-input-senha");
let input = document.getElementById("senha");
let inputIconSenha2 = document.getElementById("icon-input-senha2");
let input2 = document.getElementById("senha2");

inputIconSenha.addEventListener("click", () => {
    if (input.getAttribute("type") === "password") {
        inputIconSenha.innerText = "visibility_off";
        input.setAttribute("type", "text");
    } else {
        inputIconSenha.innerText = "visibility";
        input.setAttribute("type", "password");
    }
});

inputIconSenha2.addEventListener("click", () => {
    if (input2.getAttribute("type") === "password") {
        inputIconSenha2.innerText = "visibility_off";
        input2.setAttribute("type", "text");
    } else {
        inputIconSenha2.innerText = "visibility";
        input2.setAttribute("type", "password");
    }
});