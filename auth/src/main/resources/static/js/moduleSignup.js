import {Application, Controller} from "https://unpkg.com/@hotwired/stimulus/dist/stimulus.js";

function isPassValid(input) {
    const regex = /^(?=.*[a-zA-Z])(?=.*\d).{7,}$/;
    return regex.test(input);
}

window.Stimulus = Application.start();

var emailInput = document.getElementById("email-input");
var errorEmail = document.getElementById("error-email");
var passwordInput = document.getElementById("password-input");
var errorPassword = document.getElementById("error-password");
var nameInput = document.getElementById("name-input");
var errorName = document.getElementById("error-name");
var errorTerms = document.getElementById("error-terms");

Stimulus.register(
    "signup",
    class extends Controller {
        static targets = ["email", "password", "name", "terms"];

        nameAction() {
            let name = this.nameTarget.value;
            if (!name && name === "") {
                nameInput.classList.add("error-input");
                errorName.classList.remove("hidden");
            } else {
                nameInput.classList.remove("error-input");
                errorName.classList.add("hidden");
            }
        }

        emailAction() {
            let email = this.emailTarget.value;
            if (!email && email === "") {
                emailInput.classList.add("error-input");
                errorEmail.classList.remove("hidden");
            } else {
                emailInput.classList.remove("error-input");
                errorEmail.classList.add("hidden");
            }
        }

        passwordAction() {
            let password = this.passwordTarget.value;
            if (!password && password === "") {
                errorPassword.innerText = "Senha é obrigatória";
                passwordInput.classList.add("error-input");
                errorPassword.classList.remove("hidden");
            } else if (!isPassValid(password)) {
                errorPassword.innerText = "No mínimo 7 caracteres, com ao menos uma letra e um número.";
                passwordInput.classList.add("error-input");
                errorPassword.classList.remove("hidden");
            } else {
                passwordInput.classList.remove("error-input");
                errorPassword.classList.add("hidden");
                errorPassword.innerText = "Senha é obrigatória";
            }
        }

        termsAction() {
            let terms = this.termsTarget.checked;
            if (!terms) {
                errorTerms.classList.remove("hidden");
            } else {
                errorTerms.classList.add("hidden");
            }
        }
    }
);