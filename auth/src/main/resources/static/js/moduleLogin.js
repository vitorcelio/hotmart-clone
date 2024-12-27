import {Application, Controller} from "https://unpkg.com/@hotwired/stimulus/dist/stimulus.js";

window.Stimulus = Application.start();

var emailInput = document.getElementById("email-input");
var errorEmail = document.getElementById("error-email");
var passwordInput = document.getElementById("password-input");
var errorPassword = document.getElementById("error-password");

Stimulus.register(
    "login",
    class extends Controller {
        static targets = ["email", "password"];

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
                passwordInput.classList.add("error-input");
                errorPassword.classList.remove("hidden");
            } else {
                passwordInput.classList.remove("error-input");
                errorPassword.classList.add("hidden");
            }
        }
    }
);