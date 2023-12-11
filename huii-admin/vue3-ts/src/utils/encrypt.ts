/**
 * encrypt utils
 *
 * @author huii
 */
//@ts-ignore
import JSEncrypt from 'jsencrypt/bin/jsencrypt'
import {getServerPublicKey} from "@/api/auth/security";
import {ref} from "vue";

let publicKey = ref<string | null>(null);
export const encryptFiled = async (field: string) => {
    if (publicKey.value === null) {
        const res = await getServerPublicKey();
        publicKey.value = res.data.publicKey;
    }

    const encryptor = new JSEncrypt();
    encryptor.setPublicKey(publicKey.value);
    return encryptor.encrypt(field);
};
