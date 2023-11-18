/**
 * encrypt utils
 *
 * @author huii
 */
//@ts-ignore
import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'
import {getServerPublicKey} from "@/api/auth/security";

export const encryptFiled = async (field: string) => {
    const res = await getServerPublicKey();
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(res.data.publicKey)
    return encryptor.encrypt(field)
};

export const encryptFieldOt = async (field: string) => {
    try {
        const res = await getServerPublicKey();
        const key = res.data.publicKey
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace(/[\r\n]+/g, '')
            .trim();

        const publicKeyBytes = Uint8Array.from(atob(key), c => c.charCodeAt(0));
        const publicKey = await crypto.subtle.importKey(
            "spki",
            publicKeyBytes,
            {name: "RSA-OAEP", hash: "SHA-256"},
            false,
            ["encrypt"]
        );

        const buffer = new TextEncoder().encode(field);
        const encryptedBuffer = await crypto.subtle.encrypt(
            {name: "RSA-OAEP"},
            publicKey,
            buffer
        );

        //@ts-ignore
        return btoa(String.fromCharCode(...new Uint8Array(encryptedBuffer)));
    } catch (error) {
        throw error;
    }
}