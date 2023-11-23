/**
 * encrypt utils
 *
 * @author huii
 */
//@ts-ignore
import JSEncrypt from 'jsencrypt/bin/jsencrypt'
import {getServerPublicKey} from "@/api/auth/security";

export const encryptFiled = async (field: string) => {
    const res = await getServerPublicKey();
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(res.data.publicKey)
    return encryptor.encrypt(field)
};
