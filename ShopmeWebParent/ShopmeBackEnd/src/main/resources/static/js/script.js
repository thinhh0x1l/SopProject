function checkPasswordMatch() {
    const password = document.getElementById("password");
    const confirmPass = document.getElementById("confirmPass");
    const submit = document.getElementById("sub");
    if (confirmPass.value !== password.value) {
        confirmPass.setCustomValidity("Passwords do not match.");
        submit.disabled = true;
    } else {
        confirmPass.setCustomValidity('');
        submit.disabled = false;
    }
}
document.getElementById("myButton").addEventListener("click",function (){
    window.location.href = "/ShopmeAdmin/users";
})


const showModal = (title, message) => {
    document.getElementById("modalTitle").innerText = title;
    document.getElementById("modalBody").innerText = message;

    let modal = new bootstrap.Modal(document.getElementById("modalDialog"));
    modal.show();
};
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("emailForm");
    const emailInput = document.getElementById("email");
    const id = document.getElementById("id")
    let csrfValue = document.querySelector("input[name='_csrf']").value;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const isExisted = await checkEmailExists(id.value,emailInput.value,csrfValue);
        if (isExisted === "Ok")
            showModal("WARNING", emailInput.value+ " đã tồn tại" );
        else if(isExisted === "No")
            form.submit();
        else{}
    });
});
const checkEmailExists = async (id,email,csrfValue) => {
    try{
        let response = await fetch("http://localhost:8080/ShopmeAdmin/users/check_email", {
            method: "POST",
            headers: { "Content-Type": "application/json" ,
                "X-CSRF-TOKEN": csrfValue  },// Gửi CSRF token để server xác thực
            body: JSON.stringify({ id,email })
        })
        if(!response.ok)
            throw new Error(`Lỗi mạng! Mã lỗi: ${response.status} (${response.statusText})`);
        let data = await response.text()
        return data === "EXISTED"  ? "Ok": "No";
    }catch (Error) {
        showModal("ERROR", Error.message || "Lỗi kết nối đến server!");
        return false;
    }
};