import svgIcon from "@/components/svgIcon/Index.vue";

interface Components {
    [key: string]: any;
}

const components: Components = { svgIcon };

export default {
    install(app: any) {
        Object.keys(components).forEach(key => {
            app.component(key, components[key])
        })
    }
}