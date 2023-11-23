import svgIcon from "@/components/svgIcon/Index.vue";
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

interface Components {
    [key: string]: any;
}

const components: Components = { svgIcon };

export default {
    install(app: any) {
        Object.keys(components).forEach(key => {
            app.component(key, components[key])
        })
        for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
            app.component(key, component)
        }
    }
}