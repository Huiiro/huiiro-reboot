// @ts-nocheck
//***********
//富文本编辑器
//see： https://code-farmer-i.github.io/vue-markdown-editor/zh/
//***********

/**
 * v-md-editor 富文本编辑器
 * 轻量引入
 */
//轻量编辑器
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
//github主题
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
//github 高亮插件
import hljs from 'highlight.js';
//引入github主题
// VMdEditor.use(githubTheme, {
//     Hljs: hljs,
// });
//vuepress主题
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
//vuepress 高亮插件
import Prism from 'prismjs';
//emoji 插件
//使用：  <v-md-editor v-model="text" left-toolbar="undo redo | emoji" height="500px" />
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index';
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css';
//行号插件
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
//快速复制插件
//使用：<v-md-editor v-model="text" height="500px" @copy-code-success="handleCopyCodeSuccess" />
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
/**
 * v-md-editor 富文本编辑器
 * 阅览组件引入
 */
//使用：<v-md-preview :text="text"></v-md-preview>
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
//引入vuepress主题
VMdEditor.use(vuepressTheme, {
    Prism,
});

VMdEditor.use(createEmojiPlugin());

VMdEditor.use(createLineNumbertPlugin());

VMdEditor.use(createCopyCodePlugin());

/**
 * v-md-editor 富文本编辑器
 * 完整引入
 */
// import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
// import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
// import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
// import '@kangc/v-md-editor/lib/theme/style/github.css';
// import hljs from 'highlight.js';
// import Codemirror from 'codemirror';
// // mode
// import 'codemirror/mode/markdown/markdown';
// import 'codemirror/mode/javascript/javascript';
// import 'codemirror/mode/css/css';
// import 'codemirror/mode/htmlmixed/htmlmixed';
// import 'codemirror/mode/vue/vue';
// // edit
// import 'codemirror/addon/edit/closebrackets';
// import 'codemirror/addon/edit/closetag';
// import 'codemirror/addon/edit/matchbrackets';
// // placeholder
// import 'codemirror/addon/display/placeholder';
// // active-line
// import 'codemirror/addon/selection/active-line';
// // scrollbar
// import 'codemirror/addon/scroll/simplescrollbars';
// import 'codemirror/addon/scroll/simplescrollbars.css';
// // style
// import 'codemirror/lib/codemirror.css';
//
// VMdEditor.Codemirror = Codemirror;
// VMdEditor.use(githubTheme, {
//     Hljs: hljs,
// });
//引入
VMdPreview.use(githubTheme, {
    Hljs: hljs,
});
export default {VMdEditor, VMdPreview}
