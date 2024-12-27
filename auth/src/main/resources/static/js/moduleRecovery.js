function isEmailValid(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

import {Application, Controller} from "https://unpkg.com/@hotwired/stimulus/dist/stimulus.js";

window.Stimulus = Application.start();

var emailInput = document.getElementById("email-input");
var errorEmail = document.getElementById("error-email");

Stimulus.register(
    "passwordRecovery",
    class extends Controller {
        static targets = ["email"];

        emailAction() {
            let email = this.emailTarget.value;
            if (!email && email === "") {
                errorEmail.innerText = "Email é obrigatório";
                emailInput.classList.add("error-input");
                errorEmail.classList.remove("hidden");
            } else if (!isEmailValid(email)) {
                errorEmail.innerText = "Digite um endereço de email válido";
                emailInput.classList.add("error-input");
                errorEmail.classList.remove("hidden");
            } else {
                emailInput.classList.remove("error-input");
                errorEmail.classList.add("hidden");
            }
        }
    }
);