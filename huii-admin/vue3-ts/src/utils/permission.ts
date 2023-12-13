/**
 * permission check
 *
 * @author huii
 */
import {useUserStore} from "@/store/modules/user.ts";

const userStore = useUserStore();
const systemPermission = "*:*:*";
/**
 * check has permission
 * @param permission
 */
export const checkPermission = (permission: string) => {
    if (userStore.permissions.some((p: string) => p === systemPermission)) {
        return true
    }
    return userStore.permissions.some((p: string) => p === permission);

}

/**
 * check has any permission
 * @param permissions
 */
export const checkPermissions = (permissions: string[]) => {
    if (userStore.permissions.some((p: string) => p === systemPermission)) {
        return true
    }
    return permissions.some((p) => userStore.permissions.includes(p));
}

/**
 * check has role
 * @param role
 */
export const checkRole = (role: string) => {
    if (userStore.permissions.some((p: string) => p === systemPermission)) {
        return true
    }
    return userStore.roles.some((p: string) => p === role);
}

/**
 * check has any role
 * @param roles
 */
export const checkRoles = (roles: string[]) => {
    if (userStore.permissions.some((p: string) => p === systemPermission)) {
        return true
    }
    return roles.some((r) => userStore.roles.includes(r));
}
