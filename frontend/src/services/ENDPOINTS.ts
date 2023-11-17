const hostname = "https://openai-telegram-bot-egqt.onrender.com/api/v1/";
export const ENDPOINTS = {
    login:hostname+"auth/login",
    signUp:hostname+"auth/sign-up",
    allMessages:hostname+"chat-logs",
    sendMessage:hostname+"chat-logs/send-message",
    getUser:hostname+"user/profile",

    params:{
        mode: "cors" as RequestMode,
        headers:{"Content-Type":"application/json"}
    } as RequestInit
}