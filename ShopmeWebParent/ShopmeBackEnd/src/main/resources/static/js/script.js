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
document.getElementById('fileImage').addEventListener("change",(e)=>{
    let fileSize = e.target.files[0].size
    if(fileSize> 1024*1024){
        e.target.setCustomValidity("You must choose an image less than 1MB!")
        e.target.reportValidity()
    }else{
        e.target.setCustomValidity('')
        showImageThumbnail(e.target)
    }

})
document.getElementById("myButton").addEventListener("click",function (){
    window.location.href = "/ShopmeAdmin/users";

})

function showImageThumbnail(fileInput){
    let file = fileInput.files[0];

    if(file){
        let reader = new FileReader();
        reader.onload = (e)=>{
            document.getElementById('thumbnail').setAttribute("src",e.target.result)
        }
        reader.readAsDataURL(file);
    }
}

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
   // let csrfValue = document.querySelector("input[name='_csrf']").value;
    console.log(id.value + ' - ' + emailInput.value )
    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const isExisted = await checkEmailExists(id.value,emailInput.value);
        if (isExisted === "Ok")
            showModal("WARNING", emailInput.value+ " đã tồn tại" );
        else if(isExisted === "No")
            form.submit();
        else{}
    });
});

const checkEmailExists = async (id,email) => {

    try{
        let response = await fetch("http://localhost:8080/ShopmeAdmin/users/check_email", {
            method: "POST",
            headers: { "Content-Type": "application/json" ,},
                // 'X-XSRF-TOKEN': csrfValue},// Gửi CSRF token để server xác thực
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

    document.getElementById('logoutLink').addEventListener('click',async (e)=>{
        document.querySelector('form').submit()
    })
