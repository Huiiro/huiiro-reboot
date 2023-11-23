export interface AccountLogin {
    username: String,
    password: String,
    rememberMe: Boolean
}

export interface SmsLogin {
    sms: String,
    code: String,
    rememberMe: Boolean
}

export interface EmailLogin {
    email: String,
    code: String,
    rememberMe: Boolean
}