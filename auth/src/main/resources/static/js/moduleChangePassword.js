function isPassValid(input) {
    const regex = /^(?=.*[a-zA-Z])(?=.*\d).{7,}$/;
    return regex.test(input);
}

import {Application, Controller} from "https://unpkg.com/@hotwired/stimulus/dist/stimulus.js";

window.Stimulus = Application.start();

var noMatchs = document.getElementById("no-matchs");
var invalidPass = document.getElementById("invalid-pass");
let btnPassChange = document.getElementById("btn-pass-change");

Stimulus.register(
    "passwordChange",
    class extends Controller {
        static targets = ["password", "passwordRepeat", "btnPassChange"];

        passwordAction() {
            let password = this.passwordTarget.value;
            let passwordRepeat = this.passwordRepeatTarget.value;

            if (!isPassValid(password) || !isPassValid(passwordRepeat)) {
                btnPassChange.setAttribute("disabled", true);
                invalidPass.classList.remove("hidden");
            } else {
                invalidPass.classList.add("hidden");
            }

            if (password !== passwordRepeat) {
                btnPassChange.setAttribute("disabled", true);
                noMatchs.classList.remove("hidden");
            } else {
                noMatchs.classList.add("hidden");
            }

            if ((isPassValid(password) && isPassValid(passwordRepeat)) && (password === passwordRepeat)) {
                btnPassChange.removeAttribute("disabled");
            }
        }
    }
);