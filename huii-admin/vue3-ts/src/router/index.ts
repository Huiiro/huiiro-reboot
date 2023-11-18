import {createRouter, createWebHistory} from "vue-router";
import {constRoutes} from "@/router/routes.ts";

let router = createRouter({
    history: createWebHistory(),
    routes: constRoutes,
    scrollBehavior() {
        return {
            left: 0,
            top: 0
        }
    }
})
export default router;